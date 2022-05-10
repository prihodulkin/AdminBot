package com.admin_bot.features.messages.data

import io.ktor.http.*

data class GetMessagesParams(val pageNumber: Long?, val pageSize: Long?) {
    constructor(parameters: Parameters) : this(
        pageNumber = parameters["pageNumber"]?.toLong(),
        pageSize = parameters["pageSize"]?.toLong()
    )
}