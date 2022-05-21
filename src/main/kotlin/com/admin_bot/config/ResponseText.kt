package com.admin_bot.config

class ResponseText {
    companion object{
        const val incorrectJson = "An incorrect json was received"
        const val internalError = "An internal error has occurred"
        const val incorrectPassword =
            "Password should contains at least 8 symbols including at least one digit, one upper case letter and one lowercase letter"
        const val accessTokenIsAlreadyUsed = "The submitted access token is already in use by another bot"
        const val emailIsAlreadyUsed = "The submitted email is already in use by another bot"
        const val loginFailed = "Login has failed because of wrong email (accessToken) or password"
        const val useOnlyEmailOrAccessTokenForLogin = "You shouldn't use both email and access token for login"
        const val useEmailOrAccessTokenForLogin = "You should send 'email' or 'accessToken' parameters"
        const val incorrectRefreshToken = "Refresh token is incorrect"
        const val refreshTokenIsExpired = "Refresh token is expired"
        const val incorrectEmailFormat = "Incorrect email format"
        const val otpWasExpired = "One-time password was expired"
        fun otpWasSent(email: String): String {
            return "One-time password was sent to $email"
        }
        const val emailWasConfirmed = "Email was confirmed"
        const val incorrectOtp = "Incorrect otp was sent"
        const val configChangeSuccess = "Bot configuration was changed with success"
    }
}