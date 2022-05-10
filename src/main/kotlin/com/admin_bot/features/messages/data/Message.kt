package com.admin_bot.features.messages.data

import com.admin_bot.features.classification.data.MessageCategory
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant


@Serializable
data class Message(
    val id: Long,
    val text: String,
    val userId: Long,
    val chatId: Long,
    val createdAt: Instant,
    val botClassification: MessageCategory,
    val adminClassification: MessageCategory,
    )