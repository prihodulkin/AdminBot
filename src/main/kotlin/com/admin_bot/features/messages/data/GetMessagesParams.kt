package com.admin_bot.features.messages.data

import io.ktor.http.*

data class GetMessagesParams(val pageNumber: Int?, val pageSize: Int?) {
    constructor(parameters: Parameters) : this(
        pageNumber = parameters["pageNumber"]?.toInt(),
        pageSize = parameters["pageSize"]?.toInt()
    )
}