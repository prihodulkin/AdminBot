package com.admin_bot.features.authentification.route

import com.admin_bot.config.ResponseText
import com.admin_bot.common.errors.handleCommonErrors
import com.admin_bot.environment.AppEnvironment
import com.admin_bot.features.authentification.data.JwtRefreshParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.refreshRoute(appEnvironment: AppEnvironment) {
    val serverConfig = appEnvironment.serverConfig
    val jwtAuthenticator = appEnvironment.jwtAuthenticator
    route("/jwt_refresh") {
        post {
            handleCommonErrors {
                val refreshParams = call.receive<JwtRefreshParams>()
                val checkResult =
                    jwtAuthenticator.validateRefreshTokenAndRemoveOnSuccess(refreshParams.refreshToken)
                if (checkResult.first) {
                    val botId = checkResult.second!!
                    val authTokens = jwtAuthenticator.generateTokens(serverConfig, botId)
                    call.respond(HttpStatusCode.OK, authTokens)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, ResponseText.incorrectRefreshToken)
                }
            }
        }
    }
}