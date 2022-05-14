package com.admin_bot.features.login.route

import com.admin_bot.config.ResponseText
import com.admin_bot.common.errors.handleCommonErrors
import com.admin_bot.environment.AppEnvironment
import com.admin_bot.features.login.data.LoginParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.loginRoute(appEnvironment: AppEnvironment) {
    val loginRepository = appEnvironment.loginRepository
    val jwtAuthenticator = appEnvironment.jwtAuthenticator
    route("/login") {
        post {
            handleCommonErrors {
                val loginParams = call.receive<LoginParams>()
                val botId = loginRepository.login(loginParams)
                if(botId==null){
                    call.respond(HttpStatusCode.Unauthorized, ResponseText.loginFailed)
                    return@handleCommonErrors
                } else {
                    val serverConfig = appEnvironment.serverConfig
                    val authTokens = jwtAuthenticator.generateTokens(serverConfig, botId)
                    call.respond(HttpStatusCode.OK, authTokens)
                }
            }
        }
    }
}