package com.admin_bot.features.bot_managing.model

import com.admin_bot.common.async.ListenableStream
import com.admin_bot.common.logging.logError
import com.admin_bot.features.bot.model.OnMessageActionType
import com.admin_bot.features.bot.model.OnMessageActionsFactory
import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.bot_managing.data.BotActionConfigChange
import com.admin_bot.features.classification.model.Classifier
import com.admin_bot.features.classification.model.ClassifierRepository
import com.admin_bot.features.messages.data.Message
import org.slf4j.Logger

class BotController(
    private val botId: Long,
    private var actionConfig: BotActionConfig,
    private val actionsFactory: OnMessageActionsFactory,
    private val classifierRepository: ClassifierRepository,
    private var classifier: Classifier,
    private val logger: Logger,
    messagesStream: ListenableStream<Message>,
    configStream: ListenableStream<BotActionConfigChange>,
) {

    private val configSubscription = configStream.listen {
        logger.logError {
            val newActionConfig = event.apply(actionConfig)
            if (newActionConfig.classifierType != actionConfig.classifierType) {
                classifier = classifierRepository.getClassifier(newActionConfig.classifierType, botId)
            }
            actionConfig = newActionConfig

        }
    }

    private val messagesSubscription = messagesStream.listen {
        logger.logError {
            val universalActionType = actionConfig.universalActionType
            if (universalActionType != OnMessageActionType.NONE) {
                executeActionOfExactType(universalActionType, event)
            } else {
                val actionType = actionConfig.chatsActionTypes[event.chatId]
                if (actionType != null) {
                    executeActionOfExactType(actionType, event)
                }
            }
        }
    }

    private suspend fun executeActionOfExactType(
        actionType: OnMessageActionType,
        message: Message
    ) {
        if (!classifier.isAcceptable(message)) {
            val action = actionsFactory.getAction(actionType)
            logger.logError {
                action.execute(message, actionConfig)
            }
        }
    }

    /**
     * Needed to be called when controller isn't used
     */
    fun dispose() {
        configSubscription.close()
        messagesSubscription.close()
    }
}