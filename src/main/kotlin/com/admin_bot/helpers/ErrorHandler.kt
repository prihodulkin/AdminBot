package com.admin_bot.helpers

import com.admin_bot.environment.config.ResponseText
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.SerializationException

suspend fun PipelineContext<*, ApplicationCall>.handleCommonErrors(
    body: suspend () -> Unit
) {
    try {
        body()
    } catch (e: SerializationException) {
        call.respond(HttpStatusCode.BadRequest, ResponseText.incorrectJson)
    } catch (e: Exception){
        call.respond(HttpStatusCode.InternalServerError, ResponseText.internalError)
    }
}