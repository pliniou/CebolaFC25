package com.example.cebolafc25.data.di

import android.content.Context
import androidx.room.Room
import com.example.cebolafc25.data.database.AppDatabase
import com.example.cebolafc25.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "eafc25-database"
        )
            // Em produção, uma estratégia de migração real deve ser implementada
            // em vez de fallbackToDestructiveMigration().
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideJogadorRepository(db: AppDatabase): JogadorRepository {
        return JogadorRepositoryImpl(db.jogadorDao())
    }

    @Provides
    @Singleton
    fun providePartidaRepository(db: AppDatabase): PartidaRepository {
        return PartidaRepositoryImpl(db.partidaDao())
    }

    @Provides
    @Singleton
    fun provideCampeonatoRepository(db: AppDatabase): CampeonatoRepository {
        return CampeonatoRepositoryImpl(db.campeonatoDao())
    }
}