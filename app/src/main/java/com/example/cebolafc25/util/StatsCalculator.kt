package com.example.cebolafc25.util

import com.example.cebolafc25.data.model.JogadorEntity
import com.example.cebolafc25.data.model.PartidaEntity
import com.example.cebolafc25.domain.model.EstatisticasJogador

object StatsCalculator {
    fun calculate(jogadores: List<JogadorEntity>, partidas: List<PartidaEntity>): List<EstatisticasJogador> {
        return jogadores.map { jogador ->
            var vitorias = 0
            var empates = 0
            var derrotas = 0
            var golsPro = 0
            var golsContra = 0

            val partidasDoJogador = partidas.filter { it.jogador1Id == jogador.id || it.jogador2Id == jogador.id }

            partidasDoJogador.forEach { partida ->
                val (placarJogador, placarAdversario) = if (partida.jogador1Id == jogador.id) {
                    golsPro += partida.placar1
                    golsContra += partida.placar2
                    partida.placar1 to partida.placar2
                } else { // Jogador é J2
                    golsPro += partida.placar2
                    golsContra += partida.placar1
                    partida.placar2 to partida.placar1
                }

                when {
                    placarJogador > placarAdversario -> vitorias++
                    placarJogador < placarAdversario -> derrotas++
                    else -> empates++
                }
            }

            EstatisticasJogador(
                jogador = jogador,
                jogos = partidasDoJogador.size,
                vitorias = vitorias,
                empates = empates,
                derrotas = derrotas,
                golsPro = golsPro,
                golsContra = golsContra
            )
        }.sortedWith(compareByDescending<EstatisticasJogador> { it.pontos }.thenByDescending { it.saldoGols }.thenByDescending { it.golsPro })
    }
}