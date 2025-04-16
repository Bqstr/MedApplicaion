package io.oitech.med_application.chatbot

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class ChatbotApiTest {

    private val testCases = listOf(
        "I have a sore throat and fever.",
        "I feel nauseous and have stomach cramps.",
        "I’ve been dizzy and lightheaded since morning.",
        "There’s a rash on my skin and it's itchy."
    )

    @Test
    fun testChatbotResponses() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        for ((index, input) in testCases.withIndex()) {
            val latch = CountDownLatch(1)
            val start = System.currentTimeMillis()

            Log.i("ChatbotTest", "🧪 Test ${index + 1}: \"$input\"")

            GlobalScope.launch {
                ChatGPTApi().getResponse(input) { response ->
                    val duration = System.currentTimeMillis() - start
                    if (response != null) {
                        Log.i("ChatbotTest", "Response Time: ${duration}ms")
                        Log.i("ChatbotTest", "Response:\n$response")
                    } else {
                        Log.e("ChatbotTest", "Failed to get response")
                    }
                    latch.countDown()
                }
            }

            val success = latch.await(20, TimeUnit.SECONDS)
            if (!success) {
                Log.e("ChatbotTest", "Test ${index + 1} timed out")
            }
        }
    }
}