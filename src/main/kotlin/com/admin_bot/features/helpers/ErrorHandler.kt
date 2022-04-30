package com.admin_bot.features.helpers

import com.admin_bot.environment.config.ResponseText
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.SerializationException
import java.lang.IllegalArgumentException

suspend fun PipelineContext<*, ApplicationCall>.handleCommonErrors(
    body: suspend () -> Unit
) {
    try {
        body()
    } catch (e: SerializationException) {
        call.respond(HttpStatusCode.BadRequest, ResponseText.incorrectJson)

    } catch (e: IllegalArgumentException){
        call.respond(HttpStatusCode.BadRequest, e.message ?:"")
    }
    catch (e: Exception){
        call.respond(HttpStatusCode.InternalServerError, ResponseText.internalError)
    }
}