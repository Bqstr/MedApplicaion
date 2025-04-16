package io.oitech.med_application.chatbot

object SymptomDiseaseHelper {
    fun parseSymptoms(input: String): List<String> {
        return input.split(",").map { it.trim().lowercase() }
    }
}