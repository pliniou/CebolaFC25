package com.example.cebolafc25.data.repository

import android.content.Context
import com.example.cebolafc25.data.model.TimeEntity
import com.example.cebolafc25.util.TeamLoader
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // MELHORIA: A lista de times agora é carregada de forma assíncrona para não bloquear a thread principal.
    private var allTeams: List<TimeEntity>? = null
    private var allLeagues: List<String>? = null

    // Garante que os dados sejam carregados apenas uma vez de forma segura.
    private suspend fun ensureDataLoaded() {
        if (allTeams == null) {
            withContext(Dispatchers.IO) { // Executa a leitura do arquivo em uma thread de I/O.
                val loadedTeams = TeamLoader.loadTeamsFromJson(context)
                allTeams = loadedTeams
                allLeagues = loadedTeams.map { it.liga }.distinct().sorted()
            }
        }
    }

    suspend fun getLeagues(): List<String> {
        ensureDataLoaded()
        return allLeagues ?: emptyList()
    }

    suspend fun getTeamsForLeague(leagueName: String): List<TimeEntity> {
        ensureDataLoaded()
        return allTeams?.filter { it.liga == leagueName }?.sortedBy { it.nome } ?: emptyList()
    }

    // Função para obter a liga de um time específico, útil para inicializar o estado no ViewModel.
    suspend fun getLeagueForTeam(teamName: String): String? {
        ensureDataLoaded()
        return allTeams?.find { it.nome == teamName }?.liga
    }
}