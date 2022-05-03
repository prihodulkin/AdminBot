package com.admin_bot.plugins.mocks.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authCheckRoute() {
    route("/auth_check"){
        authenticate("auth-jwt") {
          get{
              call.respond(HttpStatusCode.OK)
          }
        }
    }
}