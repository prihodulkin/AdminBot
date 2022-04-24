package com.admin_bot.features.classification.data

import kotlinx.serialization.Serializable

@Serializable
enum class MessageCategory {
    GOOD,
    BAD,
    NONE
}