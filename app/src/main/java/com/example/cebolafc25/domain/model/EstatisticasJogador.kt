package com.example.cebolafc25.domain.model

import com.example.cebolafc25.data.model.JogadorEntity

data class EstatisticasJogador(
    val jogador: JogadorEntity,
    val jogos: Int,
    val vitorias: Int,
    val empates: Int,
    val derrotas: Int,
    val golsPro: Int,
    val golsContra: Int
) {
    val pontos: Int
        get() = (vitorias * 3) + empates

    val saldoGols: Int
        get() = golsPro - golsContra
}