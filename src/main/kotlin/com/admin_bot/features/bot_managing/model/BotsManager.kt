package com.admin_bot.features.bot_managing.model


import com.admin_bot.common.async.ListenableStream
import com.admin_bot.common.async.StreamTransformer
import com.admin_bot.common.errors.logError
import com.admin_bot.features.bot.model.BotFactory
import com.admin_bot.features.bot_managing.data.BotActionConfigChange
import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.features.classification.model.ClassifierRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.slf4j.ILoggerFactory
import org.slf4j.LoggerFactory


private data class BotRunData(
    val controller: BotController,
    val configChangesTransformer:
    StreamTransformer<Pair<Long, BotActionConfigChange>, BotActionConfigChange>,
) {
    fun dispose() {
        configChangesTransformer.dispose()
        controller.dispose()
    }
}

@OptIn(DelicateCoroutinesApi::class)
class BotsManager(
    private val botInfoRepository: BotInfoRepository,
    private val botFactory: BotFactory,
    private val configChanges: ListenableStream<Pair<Long, BotActionConfigChange>>,
    private val classifierRepository: ClassifierRepository,
    private val loggerFactory: ILoggerFactory,
) {
    private val runData = mutableMapOf<Long, BotRunData>()
    private val logger = loggerFactory.getLogger(BotsManager::class.java.name)

    private val configChangesSubscription = configChanges.listen {
        logger.logError {
            val botId = event.first
            val configChange = event.second
            val botInfo = botInfoRepository.getInfo(botId)
            val config = configChange.apply(botInfo.actionConfig)
            if (configChange.enabled == true) {
                val newBotInfo = botInfo.copy(actionConfig = config)
                runBot(newBotInfo)
            } else if (configChange.enabled == false) {
                disposeRunData(botId)
            }
            botInfoRepository.saveConfig(botId, config)
        }
    }

    init {
        GlobalScope.launch {
            runBots()
        }
    }

    private suspend fun runBots() {
        val botInfoItems = botInfoRepository.getAllInfo()
        for (botInfo in botInfoItems) {
            if (botInfo.actionConfig.enabled) {
                runBot(botInfo)
            }
        }
    }

    private suspend fun runBot(botInfo: BotInfo) = coroutineScope {
        logger.logError {
            val botId = botInfo.id
            val bot = botFactory.createBot(botInfo)
            val classifier = classifierRepository.getClassifier(botInfo.actionConfig.classifierType, botId)
            val configChangesTransformer =
                StreamTransformer(
                    stream = configChanges,
                    filteringCondition = { pair -> pair.first == botInfo.id },
                    mapper = { pair -> pair.second }
                )
            val controller = BotController(
                botId = botId,
                actionConfig = botInfo.actionConfig,
                actionsFactory = OnMessageActionsCachingProxyFactory(bot.actionsFactory),
                messagesStream = bot.messageReceiver.messages,
                configStream = configChangesTransformer,
                classifierRepository = classifierRepository,
                classifier = classifier,
                logger = LoggerFactory.getLogger(BotController::class.java.name)
            )
            runData[botInfo.id] = BotRunData(controller, configChangesTransformer)
        }
    }

    private fun disposeRunData(botId: Long) {
        val data = runData[botId]
        data?.dispose()
        runData.remove(botId)
    }

    fun dispose() {
        configChangesSubscription.close()
        for (pair in runData) {
            pair.value.dispose()
        }
        runData.clear()
    }
}

