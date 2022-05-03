package com.admin_bot.config

import com.admin_bot.environment.AppEnvironment
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureJwtAuthentication(appEnvironment: AppEnvironment){
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
            validate { credential -> null
                if (credential.payload.getClaim("botId").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, ResponseText.loginFailed)
            }
        }
    }
}