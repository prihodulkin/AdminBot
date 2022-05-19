package com.admin_bot.features.bot_managing.route

import com.admin_bot.common.errors.handleCommonErrors
import com.admin_bot.config.botId
import com.admin_bot.environment.AppEnvironment
import com.admin_bot.features.bot_managing.data.BotActionConfigChange
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.botConfigRoute(appEnvironment: AppEnvironment) {
    val configChangesHandler = appEnvironment.adminConfigChangesHandler
    route("bot/config") {
        authenticate("auth-jwt") {
            post {
                handleCommonErrors {
                    val actionConfigChange = call.receive<BotActionConfigChange>()
                    val botId = call.botId()
                    configChangesHandler.changeConfig(botId, actionConfigChange)
                }
            }
        }
    }
}