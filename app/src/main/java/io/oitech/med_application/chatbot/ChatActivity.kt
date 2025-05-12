package io.oitech.med_application.chatbot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import io.oitech.med_application.R

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button

    private val chatMessages = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        chatAdapter = ChatAdapter(chatMessages)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        loadChatHistory()

        val userName = "Beknur"

        if (!ChatbotGreetingManager.hasGreetedToday(this)) {
            val greeting = ChatbotGreetingManager.generateGreeting(userName)
            addBotMessage(greeting)
            ChatbotGreetingManager.markGreetedToday(this)
        }

        sendButton.setOnClickListener {
            val userMessage = messageInput.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                sendMessage(userMessage)
                messageInput.text?.clear()
            }
        }

//        messageInput = findViewById(R.id.messageInput)
//        sendButton = findViewById(R.id.sendButton)
//        chatHistory = findViewById(R.id.chatHistory)
//
//        sendButton.setOnClickListener {
//            val userMessage = messageInput.text.toString()
//            chatHistory.append("\nВы: $userMessage")
//
//            ChatGPTApi().getResponse(userMessage) { response ->
//                runOnUiThread {
//                    chatHistory.append("\nChatGPT: ${response ?: "Ошибка"}")
//                }
//            }
//        }
    }

//    private fun sendMessage(message: String) {
//        chatMessages.add(ChatMessage(message, isUser = true))
//        chatMessages.add(ChatMessage("AI Assistant: I'm here to help!", isUser = false)) // Simulated AI response
//        chatAdapter.notifyDataSetChanged()
//        chatRecyclerView.scrollToPosition(chatMessages.size - 1)
//    }

    private fun addBotMessage(message: String) {
        chatMessages.add(ChatMessage(message, isUser = false))
        chatAdapter.notifyDataSetChanged()
        chatRecyclerView.scrollToPosition(chatMessages.size - 1)
        chatRecyclerView.post {
            chatRecyclerView.scrollToPosition(chatMessages.size - 1)
        }
        saveChatHistory()
    }


//    private fun sendMessage(message: String) {
//        chatMessages.add(ChatMessage(message, isUser = true))
//        chatAdapter.notifyDataSetChanged()
//        chatRecyclerView.post {
//            chatRecyclerView.scrollToPosition(chatMessages.size - 1)
//        }
//        saveChatHistory()
//
//        ChatGPTApi().getResponse(message) { response ->
//            runOnUiThread {
//                if (response != null) {
//                    addBotMessage(response)
//                } else {
//                    Toast.makeText(this, "Failed to get response", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun sendMessage(message: String) {
        chatMessages.add(ChatMessage(message, isUser = true))
        chatAdapter.notifyDataSetChanged()
        chatRecyclerView.post {
            chatRecyclerView.scrollToPosition(chatMessages.size - 1)
        }
        saveChatHistory()

        if (!isInternetAvailable()) {
            Toast.makeText(this, "No internet connection. Please check your network.", Toast.LENGTH_SHORT).show()
            return
        }

        val typingMessage = ChatMessage("🤖 Chatbot is thinking...", isUser = false)
        chatMessages.add(typingMessage)
        chatAdapter.notifyDataSetChanged()
        chatRecyclerView.scrollToPosition(chatMessages.size - 1)

        ChatGPTApi().getResponse(message) { response ->
            runOnUiThread {
                chatMessages.remove(typingMessage)

                if (response != null) {
                    addBotMessage(response)
                } else {
                    addBotMessage("❌ Failed to get a response. Please try again.")
                }

                saveChatHistory()
            }
        }
    }

    private fun saveChatHistory() {
        val sharedPreferences = getSharedPreferences("chat_history", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(chatMessages)  // chatList is your List<ChatMessage>
        editor.putString("history", json)
        editor.apply()
    }

    private fun loadChatHistory() {
        val sharedPreferences = getSharedPreferences("chat_history", MODE_PRIVATE)
        val json = sharedPreferences.getString("history", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<ChatMessage>>() {}.type
            val savedList: MutableList<ChatMessage> = Gson().fromJson(json, type)
            chatMessages.addAll(savedList)
            chatAdapter.notifyDataSetChanged()
            chatRecyclerView.scrollToPosition(chatMessages.size - 1)
        }
    }
}