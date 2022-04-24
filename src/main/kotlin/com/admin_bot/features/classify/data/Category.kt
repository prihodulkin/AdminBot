package com.admin_bot.features.classify.data

import kotlinx.serialization.Serializable

@Serializable
enum class Category {
    GOOD,
    BAD,
    NONE
}