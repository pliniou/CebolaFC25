package com.example.cebolafc25.util

import android.content.Context
import com.example.cebolafc25.data.model.TimeEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Representa a estrutura de uma liga no arquivo JSON
data class LeagueJson(val leagueName: String, val teams: List<TeamJson>)

// Representa a estrutura de um time no arquivo JSON (agora mais simples)
data class TeamJson(val teamName: String)

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

        // Usa Gson para converter a string JSON em uma lista de objetos LeagueJson
        val listType = object : TypeToken<List<LeagueJson>>() {}.type
        val leagueJsons: List<LeagueJson> = Gson().fromJson(jsonString, listType)

        // Converte a estrutura aninhada (LeagueJson -> TeamJson) para uma lista plana de TimeEntity
        return leagueJsons.flatMap { league ->
            league.teams.map { team ->
                TimeEntity(
                    nome = team.teamName,
                    liga = league.leagueName,
                    emblemaLocal = "" // Campo de emblema mantido para futura implementação
                )
            }
        }
    }
}