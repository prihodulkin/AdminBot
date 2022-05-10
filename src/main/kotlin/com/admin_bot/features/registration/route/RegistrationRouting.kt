package com.admin_bot.features.registration.route

import com.admin_bot.environment.AppEnvironment
import io.ktor.server.routing.*

fun Route.registrationRouting(appEnvironment: AppEnvironment){
    registerRoute(appEnvironment)
    registerCompleteRoute(appEnvironment)
}