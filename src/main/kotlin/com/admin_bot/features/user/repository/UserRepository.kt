package com.admin_bot.features.user.repository

import com.admin_bot.features.user.data.UserDescription
import com.admin_bot.features.user.data.UserInfo


interface UserRepository {
    suspend fun getUserInfo(UserId: Int): UserInfo
    suspend fun getAllUsersInfo(): List<UserInfo>
    suspend fun getAllUsersInfo(botId: Int): List<UserInfo>

    suspend fun addUser(userDescription: UserDescription)
    suspend fun deleteUserInfo(UserId: Int)
    suspend fun clear()
}