package com.admin_bot.common.async

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class StreamTest {
    internal class TestSubscriber(stream: ListenableStream<Int>) {
        var value = 0

        init {
            stream.listen {
                value = event
            }
        }
    }

    internal class TestSuspendSubscriber(stream: ListenableStream<Int>) {
        var value = 0

        init {
            stream.listen {
                delay(1000L)
                value = event
            }
        }
    }

    @Test
    fun testStreamListening() = runBlocking {
        val stream = Stream<Int>()
        val subscriber1 = TestSubscriber(stream)
        val subscriber2 = TestSubscriber(stream)
        assertEquals(0, subscriber1.value)
        assertEquals(0, subscriber2.value)
        stream.add(1)
        delay(10)
        assertEquals(1, subscriber1.value)
        assertEquals(1, subscriber2.value)
    }

    @Test
    fun testStreamSuspendListening() = runBlocking {
        val stream = Stream<Int>()
        val subscriber1 = TestSuspendSubscriber(stream)
        val subscriber2 = TestSuspendSubscriber(stream)
        assertEquals(0, subscriber1.value)
        assertEquals(0, subscriber2.value)
        stream.add(1)
        delay(1010)
        assertEquals(1, subscriber1.value)
        assertEquals(1, subscriber2.value)
    }
}