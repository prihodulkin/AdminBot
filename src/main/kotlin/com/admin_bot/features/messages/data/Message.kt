package com.admin_bot.features.messages.data

import com.admin_bot.features.classify.data.Category
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant


@Serializable
data class Message(
    val id: Int,
    val text: String,
    val userId: Int,
    val chatId: Int,
    val createdAt: Instant,
    val botClassification: Category,
    val adminClassification: Category,
    )