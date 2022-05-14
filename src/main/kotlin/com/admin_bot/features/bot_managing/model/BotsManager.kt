package com.admin_bot.features.bot_managing.model


import com.admin_bot.common.async.ListenableStream

import com.admin_bot.features.bot_managing.data.BotActionConfigChange


class BotsManager(
    private val botActionConfigRepository: BotActionConfigRepository,
    configChanges: ListenableStream<Pair<Int, BotActionConfigChange>>,
    ) {
    val controllers = mutableMapOf<Long, BotController>()

}

