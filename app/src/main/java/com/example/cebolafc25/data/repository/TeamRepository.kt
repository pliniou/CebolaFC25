package com.example.cebolafc25.data.repository

import android.content.Context
import com.example.cebolafc25.data.model.TimeEntity
import com.example.cebolafc25.util.TeamLoader
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val allTeams: List<TimeEntity> by lazy {
        TeamLoader.loadTeamsFromJson(context)
    }

    private val allLeagues: List<String> by lazy {
        allTeams.map { it.liga }.distinct().sorted()
    }

    fun getLeagues(): List<String> {
        return allLeagues
    }

    fun getTeamsForLeague(leagueName: String): List<TimeEntity> {
        return allTeams.filter { it.liga == leagueName }.sortedBy { it.nome }
    }
}