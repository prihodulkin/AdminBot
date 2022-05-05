package com.admin_bot.common

class Validators{
    companion object{
        fun validatePassword(password: String):Boolean{
            val regex = Regex( "^(?=.*[\\p{Ll}])(?=.*[\\p{Lu}])(?=.*\\d).{8,}$")
            return regex.containsMatchIn(password)
        }
    }
}
