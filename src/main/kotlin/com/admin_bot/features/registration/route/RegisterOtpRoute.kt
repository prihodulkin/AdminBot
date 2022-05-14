package com.admin_bot.features.registration.route

import com.admin_bot.config.ResponseText
import com.admin_bot.common.errors.BadRequestException
import com.admin_bot.common.errors.handleCommonErrors
import com.admin_bot.config.botId
import com.admin_bot.environment.AppEnvironment
import com.admin_bot.features.registration.data.EmailConfirmationParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerOtpRoute(appEnvironment: AppEnvironment) {
    val emailVerifier = appEnvironment.emailVerifier
    val registrationManager = appEnvironment.registrationManager
    route("/register/otp") {
        authenticate("auth-jwt") {
            post{
                handleCommonErrors {
                    val otp = call.receive<EmailConfirmationParams>().otp
                    val botId = call.botId()
                    val email = emailVerifier.verifyOtp(botId, otp)
                    if(email!=null){
                        call.respond(HttpStatusCode.OK, ResponseText.emailWasConfirmed)
                        registrationManager.completeRegistration(botId, email)
                    } else{
                        throw BadRequestException(ResponseText.incorrectOtp)
                    }
                }
            }
        }
    }
}