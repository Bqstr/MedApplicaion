package io.oitech.med_application.chatbot

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class LocalHealthApi(private val context: Context) {

    private val diseaseSymptomMap = mutableMapOf<String, List<String>>()
    private val diseasePrecautionMap = mutableMapOf<String, List<String>>()

    init {
        loadSymptomDataset()
        loadPrecautionDataset()
    }

    private fun loadSymptomDataset() {
        val inputStream = context.assets.open("dataset.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.readLine() // skip header
        reader.forEachLine { line ->
            val parts = line.split(",").map { it.trim() }
            val disease = parts[0]
            val symptoms = parts.drop(1).filter { it.isNotEmpty() }
            diseaseSymptomMap[disease] = symptoms
        }
    }

    private fun loadPrecautionDataset() {
        val inputStream = context.assets.open("symptom_precaution.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.readLine() // skip header
        reader.forEachLine { line ->
            val parts = line.split(",").map { it.trim() }
            val disease = parts[0]
            val precautions = parts.drop(1).filter { it.isNotEmpty() }
            diseasePrecautionMap[disease] = precautions
        }
    }

    fun getDiseaseAndPrecautions(symptoms: List<String>): Pair<String?, List<String>?> {
        val bestMatch = diseaseSymptomMap.maxByOrNull { entry ->
            entry.value.count { symptoms.contains(it) }
        }

        val disease = bestMatch?.key
        val precautions = diseasePrecautionMap[disease]
        return Pair(disease, precautions)
    }

    fun getAllSymptoms(): List<String> {
        return diseaseSymptomMap.values.flatten().distinct()
    }
}