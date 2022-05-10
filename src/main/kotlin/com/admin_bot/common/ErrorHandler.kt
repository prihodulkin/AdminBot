package com.admin_bot.common

import com.admin_bot.common.AppException
import com.admin_bot.common.ResponseText

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
    } catch (e: AppException) {
        call.respond(e.statusCode, e.message ?: "")
    } catch (e: Exception) {
        call.respond(HttpStatusCode.InternalServerError, ResponseText.internalError)
    }
}