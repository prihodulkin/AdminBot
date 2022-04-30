package com.admin_bot.environment.config

class ResponseText {
    companion object{
        const val incorrectJson = "An incorrect json was received"
        const val internalError = "An internal error has occurred"
        const val incorrectPassword = "Password should contains at least 8 symbols including at least one digit, one upper case letter and one lowercase letter"
        const val accessTokenIsAlreadyUsed = "The submitted access token is already in use by another bot"
        const val emailAlreadyUsed = "The submitted email is already in use by another bot"
        const val accessTokenValidationFailed = "Token is not valid or has expired"
        const val loginFailed = "Login has failed because of wrong email (accessToken) or password"
        const val useOnlyEmailOrAccessTokenForLogin = "You shouldn't use both email and access token for login"
        const val useEmailOrAccessTokenForLogin = "You should send 'email' or 'accessToken' parameters"
        const val incorrectRefreshToken = "Refresh token is incorrect"
        const val refreshTokenIsExpired =  "Refresh token is expired"
    }
}