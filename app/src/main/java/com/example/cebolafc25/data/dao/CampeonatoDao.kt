package com.example.cebolafc25.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cebolafc25.data.model.CampeonatoEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CampeonatoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampeonato(campeonato: CampeonatoEntity)

    @Update
    suspend fun updateCampeonato(campeonato: CampeonatoEntity)

    @Query("SELECT * FROM campeonatos WHERE id = :campeonatoId")
    fun getCampeonatoById(campeonatoId: UUID): Flow<CampeonatoEntity?>

    @Query("SELECT * FROM campeonatos ORDER BY dataCriacao DESC")
    fun getAllCampeonatos(): Flow<List<CampeonatoEntity>>

    @Query("DELETE FROM campeonatos WHERE id = :campeonatoId")
    suspend fun deleteCampeonatoById(campeonatoId: UUID)
}