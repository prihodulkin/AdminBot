package com.admin_bot.environment.config

class ResponseText {
    companion object{
        const val incorrectJson = "An incorrect json was received"
        const val internalError = "An internal error has occurred"
        const val incorrectPassword = "Password should contains at least 8 symbols including at least one digit, one upper case letter and one lowercase letter"
        const val accessTokenIsAlreadyUsed = "The submitted access token is already in use by another bot"
        const val emailAlreadyUsed = "The submitted email is already in use by another bot"
        const val accessTokenValidationFailed = "Token is not valid or has expired"
    }
}