package com.admin_bot

import com.admin_bot.environment.AppEnvironment
import com.admin_bot.features.login.route.loginRoute
import com.admin_bot.features.registration.route.registrationRouting
import com.admin_bot.plugins.mocks.MockEnvironment
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.run(appEnvironment: AppEnvironment) {
    install(ContentNegotiation) {
        json()
    }
    val config = appEnvironment.serverConfig.jwtConfig
    val secret = config.hs256Secret
    val issuer = config.issuer
    val audience = config.audience
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
    routing {
        registrationRouting(appEnvironment)
        loginRoute(appEnvironment)
    }

}

fun Application.runWithMockEnvironment(mockDatabase: MockDatabase) {
    val mockEnvironment = MockEnvironment(mockDatabase)
    run(mockEnvironment)
}
