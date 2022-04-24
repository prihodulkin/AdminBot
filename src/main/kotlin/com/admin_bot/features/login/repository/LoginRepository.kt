package com.admin_bot.features.login.repository

import com.admin_bot.features.login.data.LoginParams
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

abstract class LoginRepository {
    suspend fun login(loginParams: LoginParams): Boolean = coroutineScope {
        val hasEmail = loginParams.email != null
        val hasAccessToken = loginParams.accessToken != null
        if (hasEmail && hasAccessToken) {
            throw IllegalArgumentException("You shouldn't use both email and access token for login")
        } else if (hasEmail) {
            val result = async { loginWithEmail(loginParams.email!!, loginParams.password) }
            return@coroutineScope result.await()
        } else if (hasAccessToken) {
            val result = async { loginWithAccessToken(loginParams.accessToken!!, loginParams.password) }
            return@coroutineScope result.await()
        } else {
            throw IllegalArgumentException("You should send 'email' or 'accessToken' parameters")
        }
    }

    abstract suspend fun clear()
    protected abstract suspend fun loginWithEmail(email: String, password: String): Boolean
    protected abstract suspend fun loginWithAccessToken(accessToken: String, password: String): Boolean
}