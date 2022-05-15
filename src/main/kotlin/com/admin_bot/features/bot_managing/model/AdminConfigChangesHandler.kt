package com.admin_bot.features.bot_managing.model

import com.admin_bot.common.async.ListenableStream
import com.admin_bot.common.async.Stream
import com.admin_bot.features.bot_managing.data.BotActionConfigChange

class AdminConfigChangesHandler {
    private val pConfigChanges = Stream<Pair<Long, BotActionConfigChange>>()
    val configChanges: ListenableStream<Pair<Long, BotActionConfigChange>>
        get() = pConfigChanges

    fun changeConfig(botId: Long, configChange: BotActionConfigChange) =
        pConfigChanges.add(botId to configChange)

}