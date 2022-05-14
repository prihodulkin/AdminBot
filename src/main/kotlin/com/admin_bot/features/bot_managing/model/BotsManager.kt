package com.admin_bot.features.bot_managing.model


import com.admin_bot.common.async.ListenableStream
import com.admin_bot.common.async.StreamTransformer
import com.admin_bot.features.bot.model.BotFactory
import com.admin_bot.features.bot_managing.data.BotActionConfigChange
import com.admin_bot.features.bot_managing.data.BotInfo
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
) {
    private val runData = mutableMapOf<Long, BotRunData>()
    private val configChangesSubscription = configChanges.listen {
        val botId = event.first
        val configChange = event.second
        val botInfo = botInfoRepository.getInfo(botId)
        if (configChange.enabled == true || !runData.containsKey(botId)) {
            runBot(botInfo)
        } else if (configChange.enabled == false) {
            disposeRunData(botId)
        }
        val config = configChange.apply(botInfo.actionConfig)
        botInfoRepository.saveConfig(botId, config)
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

    private fun runBot(botInfo: BotInfo) {
        val bot = botFactory.createBot(botInfo)
        val configChangesTransformer =
            StreamTransformer<Pair<Long, BotActionConfigChange>, BotActionConfigChange>(
                stream = configChanges,
                filteringCondition = { pair -> pair.first == botInfo.id },
                mapper = { pair -> pair.second }
            )
        val controller = BotController(
            actionConfig = botInfo.actionConfig,
            actionsFactory = bot.actionsFactory,
            messagesStream = bot.messageReceiver.messages,
            configStream = configChangesTransformer
        )
        runData[botInfo.id] = BotRunData(controller, configChangesTransformer)
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

