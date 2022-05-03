package com.admin_bot.runner

import com.admin_bot.environment.AppEnvironment
import com.admin_bot.environment.config.configureJwtAuthentication
import com.admin_bot.features.authentification.route.refreshRoute
import com.admin_bot.features.login.route.loginRoute
import com.admin_bot.features.registration.route.registrationRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*


open class AppRunner {
    fun Application.run(appEnvironment: AppEnvironment) {
        install(ContentNegotiation) {
            json()
        }
        configureJwtAuthentication(appEnvironment)
        routing {
            configureRouting(appEnvironment)
        }
    }

    open fun Route.configureRouting(appEnvironment: AppEnvironment) {
        configureBaseRouting(appEnvironment)
    }

    protected fun Route.configureBaseRouting(appEnvironment: AppEnvironment) {
        registrationRouting(appEnvironment)
        loginRoute(appEnvironment)
        refreshRoute(appEnvironment)
    }
}