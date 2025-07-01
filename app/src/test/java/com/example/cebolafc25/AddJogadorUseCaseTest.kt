package com.example.cebolafc25

import com.example.cebolafc25.data.model.JogadorEntity
import com.example.cebolafc25.data.repository.JogadorRepository
import com.example.cebolafc25.domain.usecase.AddJogadorUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddJogadorUseCaseTest {

    private val mockRepository: JogadorRepository = mockk(relaxed = true)
    private val addJogadorUseCase = AddJogadorUseCase(mockRepository)

    @Test
    fun `invoke with non-blank name should insert jogador`() = runTest {
        // Given
        val nomeJogador = "Zico"

        // When
        addJogadorUseCase(nomeJogador)

        // Then
        coVerify { mockRepository.insertJogador(any<JogadorEntity>()) }
    }

    @Test
    fun `invoke with blank name should not insert jogador`() = runTest {
        // Given
        val nomeJogador = "  "

        // When
        addJogadorUseCase(nomeJogador)

        // Then
        coVerify(exactly = 0) { mockRepository.insertJogador(any()) }
    }
}