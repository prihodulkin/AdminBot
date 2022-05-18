package com.admin_bot.common.errors

import io.ktor.util.logging.*
import org.slf4j.Logger

suspend fun Logger.logError(callback: suspend ()->Unit){
    try{
        callback()
    } catch (e: Exception){
        error(e)
    }
}