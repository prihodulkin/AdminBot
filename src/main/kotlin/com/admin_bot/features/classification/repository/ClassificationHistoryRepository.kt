package com.admin_bot.features.classification.repository

import com.admin_bot.features.classification.data.MessageCategory

interface ClassificationHistoryRepository {
    suspend fun saveAdminDecision(messageId: Int, category: MessageCategory)
    suspend fun saveBotDecision(messageId: Int, category: MessageCategory)
}