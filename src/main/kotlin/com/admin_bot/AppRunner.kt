package com.admin_bot

import com.admin_bot.environment.AppEnvironment
import com.admin_bot.environment.MockEnvironment
import com.admin_bot.features.registration.route.registrationRouting
import com.admin_bot.plugins.mocks.database.MockDatabase
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun Application.run(appEnvironment: AppEnvironment) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        registrationRouting(appEnvironment)
    }

}

fun Application.runWithMockEnvironment(mockDatabase: MockDatabase) {
    val mockEnvironment = MockEnvironment(mockDatabase)
    run(mockEnvironment)
}
