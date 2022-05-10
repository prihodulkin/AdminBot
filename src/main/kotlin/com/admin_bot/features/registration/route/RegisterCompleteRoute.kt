package com.admin_bot.features.registration.route

import com.admin_bot.common.ResponseText
import com.admin_bot.common.Validators
import com.admin_bot.common.WrongRequestArgumentsException
import com.admin_bot.common.handleCommonErrors
import com.admin_bot.config.botId
import com.admin_bot.environment.AppEnvironment
import com.admin_bot.features.registration.data.CompleteRegistrationParams
import com.admin_bot.features.registration.model.EmailVerifier
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerCompleteRoute(appEnvironment: AppEnvironment) {
   val emailVerifier = appEnvironment.emailVerifier
    route("/register/complete") {
        authenticate("auth-jwt") {
            post{
                handleCommonErrors {
                    val email = call.receive<CompleteRegistrationParams>().email
                    if (Validators.validateEmail(email)){
                        val botId = call.botId()
                        emailVerifier.generateAndSendOtp(botId, email)
                        call.respond(HttpStatusCode.OK, ResponseText.otpWasSent(email))
                    } else{
                        throw WrongRequestArgumentsException(ResponseText.incorrectEmailFormat)
                    }
                }
            }
        }
    }
}