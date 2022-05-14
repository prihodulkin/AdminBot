package com.admin_bot.common.async

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Sends [EventType] events to [Listener] listeners
 */
open class Listenable<EventType: Any> {

    private val listeners = hashSetOf<Listener<EventType>>()

    @OptIn(DelicateCoroutinesApi::class)
    protected fun notifyListeners(event: EventType) {
        for (listener in listeners) {
            GlobalScope.launch {
                listener.onEvent(event)
            }
        }
    }

    fun addListener(listener: Listener<EventType>) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener<EventType>) {
        listeners.remove(listener)
    }

}

/**
 * Listen [EventType] events from [Listenable]
 */
interface Listener<EventType: Any> {
   suspend fun onEvent(event: EventType)
}