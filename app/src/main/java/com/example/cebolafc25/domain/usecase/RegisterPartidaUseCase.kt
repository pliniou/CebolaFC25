package com.example.cebolafc25.domain.usecase

import com.example.cebolafc25.data.model.PartidaEntity
import com.example.cebolafc25.data.repository.PartidaRepository
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class RegisterPartidaUseCase @Inject constructor(
    private val partidaRepository: PartidaRepository
) {
    suspend operator fun invoke(
        jogador1Id: UUID,
        jogador2Id: UUID,
        time1Nome: String,
        time2Nome: String,
        placar1: Int,
        placar2: Int
    ) {
        // Validações de negócio podem ser adicionadas aqui
        if (jogador1Id == jogador2Id) {
            throw IllegalArgumentException("Os jogadores devem ser diferentes.")
        }

        val novaPartida = PartidaEntity(
            data = LocalDate.now(),
            jogador1Id = jogador1Id,
            jogador2Id = jogador2Id,
            time1Nome = time1Nome.trim(),
            time2Nome = time2Nome.trim(),
            placar1 = placar1,
            placar2 = placar2
        )
        partidaRepository.insertPartidaWithValidation(novaPartida)
    }
}