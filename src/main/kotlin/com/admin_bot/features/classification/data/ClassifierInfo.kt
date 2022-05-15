package com.admin_bot.features.classification.data

enum class ClassifierType {
    SUBSTRING,
    REGEX,
    ML
}

data class ClassifierInfo(val classifierType: ClassifierType)