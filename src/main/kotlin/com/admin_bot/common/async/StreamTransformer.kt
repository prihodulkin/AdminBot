package com.admin_bot.common.async

class StreamTransformer<InEventType : Any, OutEventType : Any>(
    stream: ListenableStream<InEventType>,
    filteringCondition: (InEventType) -> Boolean,
    mapper: (InEventType) -> OutEventType,
    ) : ListenableStream<OutEventType> {

    private val transformedStream = Stream<OutEventType>()
    private val subscription = stream.listen {
        if (filteringCondition(event)) {
            transformedStream.add(mapper(event))
        }
    }

    override fun listen(
        onEvent: suspend StreamSubscription<OutEventType>.EventContainer.() -> Unit
    ): StreamSubscription<OutEventType> = transformedStream.listen(onEvent)

    fun dispose() = subscription.close()

}