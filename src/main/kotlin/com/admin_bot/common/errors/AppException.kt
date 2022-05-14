package com.admin_bot.common.errors

import com.admin_bot.config.ResponseText
import io.ktor.http.*

/**
 * Exception that contains [statusCode] [HttpStatusCode].
 * [message] used for HTTP response body.
 */
open class AppException(open val statusCode: HttpStatusCode, message: String) : Exception(message)

/**
 * Exception used when something is expired
 */
class ExpiredException(statusCode: HttpStatusCode, message: String) : AppException(statusCode, message)

/**
 * Exception used when some arguments are wrong
 */
class BadRequestException(message: String) : AppException(HttpStatusCode.BadRequest, message)

/**
 * Exception used when some id was incorrect
 */
class IncorrectIdException() : AppException(HttpStatusCode.BadRequest, ResponseText.internalError)