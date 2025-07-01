package com.example.cebolafc25.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cebolafc25.data.model.PartidaCampeonatoCrossRef
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface PartidaCampeonatoCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: PartidaCampeonatoCrossRef)

    @Query("SELECT partidaId FROM PartidaCampeonatoCrossRef WHERE campeonatoId = :campeonatoId")
    fun getPartidasByCampeonato(campeonatoId: UUID): Flow<List<UUID>>

    @Query("DELETE FROM PartidaCampeonatoCrossRef WHERE campeonatoId = :campeonatoId AND partidaId = :partidaId")
    suspend fun deleteCrossRef(campeonatoId: UUID, partidaId: UUID)
}