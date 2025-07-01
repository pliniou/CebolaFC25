package com.example.cebolafc25.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cebolafc25.data.converter.Converters
import com.example.cebolafc25.data.dao.CampeonatoDao
import com.example.cebolafc25.data.dao.JogadorDao
import com.example.cebolafc25.data.dao.ParticipanteCampeonatoDao
import com.example.cebolafc25.data.dao.PartidaCampeonatoCrossRefDao
import com.example.cebolafc25.data.dao.PartidaDao
import com.example.cebolafc25.data.dao.TimeDao
import com.example.cebolafc25.data.model.CampeonatoEntity
import com.example.cebolafc25.data.model.JogadorEntity
import com.example.cebolafc25.data.model.ParticipanteCampeonato
import com.example.cebolafc25.data.model.PartidaCampeonatoCrossRef
import com.example.cebolafc25.data.model.PartidaEntity
import com.example.cebolafc25.data.model.TimeEntity

@Database(
    entities = [
        JogadorEntity::class,
        TimeEntity::class,
        PartidaEntity::class,
        CampeonatoEntity::class,
        ParticipanteCampeonato::class,
        PartidaCampeonatoCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jogadorDao(): JogadorDao
    abstract fun timeDao(): TimeDao
    abstract fun partidaDao(): PartidaDao
    abstract fun campeonatoDao(): CampeonatoDao
    abstract fun participanteCampeonatoDao(): ParticipanteCampeonatoDao
    abstract fun partidaCampeonatoCrossRefDao(): PartidaCampeonatoCrossRefDao
}