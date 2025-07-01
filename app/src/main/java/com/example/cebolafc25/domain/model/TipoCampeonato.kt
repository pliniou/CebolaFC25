package com.example.cebolafc25.domain.model

sealed class TipoCampeonato(val displayName: String) {
    data object PontosCorridos : TipoCampeonato("Pontos Corridos")
    data object MataMata : TipoCampeonato("Mata-mata")
    data object GruposEMataMata : TipoCampeonato("Fase de Grupos + Mata-mata")

    companion object {
        fun getAllTypes(): List<TipoCampeonato> = listOf(PontosCorridos, MataMata, GruposEMataMata)
        fun fromDisplayName(name: String): TipoCampeonato? = getAllTypes().find { it.displayName == name }
    }
}