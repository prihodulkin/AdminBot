package com.admin_bot.features.registration.route

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
    val registrationRepository = appEnvironment.registrationRepository
    route("/register") {
        post {
            val registerParams = call.receive<RegisterParams>()
            val id = withContext(Dispatchers.Default) {
                registrationRepository.register(registerParams)
            }
            if (id == null) {
                call.respond(HttpStatusCode.PreconditionFailed)
            } else {
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}