package com.example.cebolafc25.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "times")
data class TimeEntity(
    @PrimaryKey val nome: String, // Nome do time (será a PK)
    val liga: String, // Liga do time
    val emblemaLocal: String // Caminho local para o emblema do time (ex: "time_real_madrid.png")
)