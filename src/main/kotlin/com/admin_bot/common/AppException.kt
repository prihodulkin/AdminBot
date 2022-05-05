package com.admin_bot.common

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
class WrongRequestArgumentsException(message: String) : AppException(HttpStatusCode.BadRequest, message)