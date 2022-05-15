package com.admin_bot.features.classification.model

interface ClassificationHistoryRepository {
    suspend fun saveAdminDecision(messageId: Long, isAcceptable: Boolean)
    suspend fun saveBotDecision(messageId: Long, isAcceptable: Boolean)
}