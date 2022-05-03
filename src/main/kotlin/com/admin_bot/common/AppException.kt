package com.admin_bot.common

import io.ktor.http.*

/**
 * Exception that contains [statusCode] [HttpStatusCode].
 * [message] used for HTTP response body.
 */
open class AppException : Exception {
    open val statusCode: HttpStatusCode

    constructor(statusCode: HttpStatusCode) : super() {
        this.statusCode = statusCode
    }

    constructor(statusCode: HttpStatusCode, message: String) : super(message) {
        this.statusCode = statusCode
    }

    constructor(statusCode: HttpStatusCode, message: String, cause: Throwable) : super(message, cause) {
        this.statusCode = statusCode
    }

    constructor(statusCode: HttpStatusCode, cause: Throwable) : super(cause) {
        this.statusCode = statusCode
    }

}

/**
 * Exception used when something is expired
 */
class ExpiredException : AppException {
    constructor(statusCode: HttpStatusCode) : super(statusCode)
    constructor(statusCode: HttpStatusCode, message: String) : super(statusCode, message)
    constructor(statusCode: HttpStatusCode, message: String, cause: Throwable) : super(statusCode, message, cause)
    constructor(statusCode: HttpStatusCode, cause: Throwable) : super(statusCode, cause)
}