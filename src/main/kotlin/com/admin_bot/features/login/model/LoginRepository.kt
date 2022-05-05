package com.admin_bot.features.login.model

import com.admin_bot.common.ResponseText
import com.admin_bot.common.WrongRequestArgumentsException
import com.admin_bot.features.login.data.LoginParams
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

abstract class LoginRepository {
    /**
     * Check a correctness of the [loginParams] and try to log in.
     * Throws [IllegalArgumentException] in case of incorrect [loginParams]
     * Returns botId if login was success and null in the other case
     */
    suspend fun login(loginParams: LoginParams): Int? = coroutineScope {
        val hasEmail = loginParams.email != null
        val hasAccessToken = loginParams.token != null
        if (hasEmail && hasAccessToken) {
            throw  WrongRequestArgumentsException(ResponseText.useOnlyEmailOrAccessTokenForLogin)
        } else if (hasEmail) {
            val result = async { loginWithEmail(loginParams.email!!, loginParams.password) }
            return@coroutineScope result.await()
        } else if (hasAccessToken) {
            val result = async { loginWithAccessToken(loginParams.token!!, loginParams.password) }
            return@coroutineScope result.await()
        } else {
            throw WrongRequestArgumentsException(ResponseText.useEmailOrAccessTokenForLogin)
        }
    }

    protected abstract suspend fun loginWithEmail(email: String, password: String): Int?
    protected abstract suspend fun loginWithAccessToken(accessToken: String, password: String): Int?
}

