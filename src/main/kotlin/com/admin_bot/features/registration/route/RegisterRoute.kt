package com.admin_bot.features.registration.route

import com.admin_bot.common.ResponseText
import com.admin_bot.common.BadRequestException
import com.admin_bot.common.handleCommonErrors
import com.admin_bot.environment.AppEnvironment
import com.admin_bot.features.registration.data.RegisterParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


fun Route.registerRoute(appEnvironment: AppEnvironment) {
    val registrationRepository = appEnvironment.registrationManager
    val jwtAuthenticator = appEnvironment.jwtAuthenticator
    val validator = appEnvironment.passwordValidator
    route("/register") {
        post {
            handleCommonErrors {
                val registerParams = call.receive<RegisterParams>()
                if(!validator(registerParams.password)){
                    throw BadRequestException(ResponseText.incorrectPassword)
                }
                val botId = withContext(Dispatchers.Default) {
                    registrationRepository.register(registerParams)
                }
                if (botId == null) {
                    call.respond(HttpStatusCode.Conflict, ResponseText.accessTokenIsAlreadyUsed)
                } else {
                    val serverConfig = appEnvironment.serverConfig
                    val authTokens = jwtAuthenticator.generateTokens(serverConfig, botId)
                    call.respond(HttpStatusCode.Created, authTokens)
                }
            }
        }
    }
}