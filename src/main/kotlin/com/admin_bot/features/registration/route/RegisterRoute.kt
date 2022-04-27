package com.admin_bot.features.registration.route

import com.admin_bot.environment.AppEnvironment
import com.admin_bot.features.registration.data.RegisterParams
import com.admin_bot.helpers.ResponseText
import com.admin_bot.helpers.handleCommonErrors
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


fun Route.registerRoute(appEnvironment: AppEnvironment) {
    val registrationRepository = appEnvironment.registrationRepository
    val validator = appEnvironment.passwordValidator
    route("/register") {
        post {
            handleCommonErrors {
                val registerParams = call.receive<RegisterParams>()
                if(!validator(registerParams.password)){
                    call.respond(HttpStatusCode.BadRequest, ResponseText.incorrectPassword)
                    return@handleCommonErrors
                }
                val id = withContext(Dispatchers.Default) {
                    registrationRepository.register(registerParams)
                }
                if (id == null) {
                    call.respond(HttpStatusCode.Conflict, ResponseText.accessTokenIsAlreadyUsed)
                } else {
                    call.respond(HttpStatusCode.Created)
                }
            }
        }
    }
}