package com.admin_bot.common.async

/**
 * Stream of [EventType] events.
 * [listen] method returns [StreamSubscription] used to
 * react on each event.
 */
interface ListenableStream<EventType : Any> {
    /**
     * Returns [StreamSubscription] with [onEvent] callback which reacts on appearing
     * events in this [Stream]
     */
    fun listen(
        onEvent: suspend StreamSubscription<EventType>.EventContainer.() -> Unit
    ): StreamSubscription<EventType>
}

/**
 * Stream of [EventType] events. [add] method adds event for subscribers.
 * [listen] method returns [StreamSubscription] used to
 * react on each event.
 */
class Stream<EventType : Any> : ListenableStream<EventType> {
    private val notifier = Notifier<EventType>()

    /**
     * Returns [StreamSubscription] with [onEvent] callback which reacts on appearing
     * events in this [Stream]
     */
    override fun listen(
        onEvent: suspend StreamSubscription<EventType>.EventContainer.() -> Unit
    ): StreamSubscription<EventType> {
        val streamSubscription = StreamSubscription(onEvent, notifier)
        notifier.addListener(streamSubscription)
        return streamSubscription
    }

    /**
    Add event to this [Stream]
     */
    fun add(event: EventType) {
        notifier.notify(event)
    }
}

/**
 * Subscription on events from [Stream].
 */
class StreamSubscription<EventType : Any>(
    private val onEvent: suspend StreamSubscription<EventType>.EventContainer.() -> Unit,
    private val listenable: Listenable<EventType>
) : Listener<EventType> {
    inner class EventContainer(
        private val getEvent: () -> EventType
    ) {
        val event: EventType
            get() = getEvent()
    }

    private lateinit var event: EventType

    private val eventContainer = EventContainer { event }

    /**
     * Called when [Stream] adds event
     */
    override suspend fun onEvent(event: EventType) {
        this.event = event
        onEvent.invoke(eventContainer)
    }

    /**
     * Needed to call after this [StreamSubscription] isn't used
     */
    fun close() {
        listenable.removeListener(this)
    }
}

