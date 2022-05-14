package com.admin_bot.common.async

class Notifier<EventType: Any>: Listenable<EventType>() {
    fun notify(event: EventType){
        notifyListeners(event)
    }
}