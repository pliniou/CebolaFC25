package com.example.cebolafc25.domain.usecase

import com.example.cebolafc25.data.model.PartidaEntity
import com.example.cebolafc25.data.repository.PartidaRepository
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class GenerateMatchesUseCase @Inject constructor(
    private val partidaRepository: PartidaRepository
) {
    /**
     * Gera e insere todas as partidas para um campeonato de pontos corridos (todos contra todos).
     * @param campeonatoId O ID do campeonato ao qual as partidas pertencerão.
     * @param jogadoresIds A lista de IDs dos jogadores participantes.
     */
    suspend operator fun invoke(campeonatoId: UUID, jogadoresIds: List<UUID>) {
        // A lógica do algoritmo "round-robin" é usada para gerar os confrontos.
        // Garante que cada jogador enfrente todos os outros uma única vez.
        val partidasGeradas = mutableListOf<PartidaEntity>()
        
        if (jogadoresIds.size < 2) return

        for (i in 0 until jogadoresIds.size) {
            for (j in i + 1 until jogadoresIds.size) {
                val jogador1Id = jogadoresIds[i]
                val jogador2Id = jogadoresIds[j]

                partidasGeradas.add(
                    PartidaEntity(
                        data = LocalDate.now(), // A data pode ser ajustada ou deixada para depois
                        jogador1Id = jogador1Id,
                        jogador2Id = jogador2Id,
                        time1Nome = "A definir", // O time será definido quando o resultado for registrado
                        time2Nome = "A definir",
                        placar1 = 0, // Placares iniciais
                        placar2 = 0,
                        campeonatoId = campeonatoId, // Vínculo direto com o campeonato
                        partidaFinalizada = false // Novo campo para indicar se a partida já ocorreu
                    )
                )
            }
        }
        
        // Insere todas as partidas geradas no banco de dados
        partidasGeradas.forEach { partida ->
            partidaRepository.insertPartidaWithValidation(partida)
        }
    }
}