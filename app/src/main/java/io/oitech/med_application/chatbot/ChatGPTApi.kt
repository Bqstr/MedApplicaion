package io.oitech.med_application.chatbot

import android.util.Log
import io.oitech.med_application.BuildConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ChatGPTApi {

    private val client = OkHttpClient()
    private val apiKey = BuildConfig.OPENAI_API_KEY

    fun getResponse(userMessage: String, callback: (String?) -> Unit) {
        val mediaType = "application/json".toMediaTypeOrNull()

        val systemMessage = JSONObject()
            .put("role", "system")
            .put("content", """
                You are a certified medical assistant chatbot.

                Only respond to questions clearly related to physical symptoms. 
                If the question is not about symptoms, reply: 
                "Sorry, I can only help with health-related questions based on your symptoms."

                When answering, use this format:

                **Possible Disease:** Influenza (Flu) or Viral Upper Respiratory Infection

                **Reasoning:** Your symptoms, including fever, body aches, and sore throat, are commonly associated with viral infections such as influenza or other upper respiratory infections. These illnesses are typically caused by viruses like influenza A/B or rhinoviruses. The body's immune response to the infection often leads to fatigue, chills, and muscle discomfort.
                
                These viruses spread through airborne droplets, which means even brief close contact with an infected person can lead to transmission. Symptoms often start suddenly and may last for several days, depending on your immune system and overall health.
                
                **Precautions:**
                - 🛏 **Rest:** Your body needs energy to fight off the virus. Make sure to rest adequately.
                - 💧 **Stay Hydrated:** Drink plenty of water, clear soups, and herbal teas to prevent dehydration.
                - 🌡 **Manage Fever:** Use over-the-counter medications like acetaminophen or ibuprofen to control fever and body aches (only as advised).
                - 🤧 **Cover Your Coughs/Sneezes:** Use tissues or your elbow to prevent spreading the virus.
                - 🧼 **Wash Hands Regularly:** Frequent handwashing helps stop further transmission.
                - 🏡 **Stay Isolated:** Avoid going to work, school, or crowded areas until your symptoms resolve.
                
                **Disclaimer:** This response is not a medical diagnosis. While the symptoms described suggest a possible flu or viral infection, only a licensed healthcare provider can give a proper diagnosis after a physical examination or lab test. If symptoms worsen or you experience breathing difficulties, seek immediate medical attention.
            """.trimIndent())

        Log.d("OPEN_AI_KEY", apiKey)

        val userMessageObj = JSONObject()
            .put("role", "user")
            .put("content", userMessage)

        val messagesArray = JSONArray()
            .put(systemMessage)
            .put(userMessageObj)

        val requestBodyJson = JSONObject()
            .put("model", "gpt-4o-2024-08-06")
            .put("messages", messagesArray)

        val requestBody = requestBodyJson.toString().toRequestBody(mediaType)

        Log.d("asdfkjhaskldlasdf",apiKey)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Network error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string()
                    callback("Error ${response.code}: $errorBody")
                    return
                }

                response.body?.string()?.let {
                    try {
                        val json = JSONObject(it)
                        val reply = json.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content")

                        callback(reply.trim())
                    } catch (e: Exception) {
                        callback("Parsing error: ${e.message}")
                    }
                } ?: callback("Empty response from server")
            }
        })
    }
}