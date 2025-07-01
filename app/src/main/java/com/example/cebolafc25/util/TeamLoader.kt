package com.example.cebolafc25.util

import android.content.Context
import com.example.cebolafc25.data.model.TimeEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Representa a estrutura de um time no arquivo JSON
data class TeamJson(val nome: String, val liga: String, val emblemaLocal: String)

object TeamLoader {
    // Carrega a lista de times de um arquivo JSON nos assets
    fun loadTeamsFromJson(context: Context, fileName: String = "teams.json"): List<TimeEntity> {
        val jsonString: String
        try {
            // Abre o arquivo JSON dos assets
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            jsonString = String(buffer, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList() // Retorna lista vazia em caso de erro
        }

        // Usa Gson para converter a string JSON em uma lista de objetos TeamJson
        val listType = object : TypeToken<List<TeamJson>>() {}.type
        val teamJsons: List<TeamJson> = Gson().fromJson(jsonString, listType)

        // Converte TeamJson para TimeEntity
        return teamJsons.map {
            TimeEntity(nome = it.nome, liga = it.liga, emblemaLocal = it.emblemaLocal)
        }
    }
}