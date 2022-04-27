package com.admin_bot

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*

fun ApplicationTestBuilder.createJsonClient(): HttpClient {
    return createClient {
        install(ContentNegotiation) {
            json()
        }
    }
}



