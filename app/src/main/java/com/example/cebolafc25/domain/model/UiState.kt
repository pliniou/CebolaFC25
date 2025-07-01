package com.example.cebolafc25.domain.model

/**
 * Representa os diferentes estados possíveis para a interface do usuário.
 * É uma classe selada genérica para ser reutilizada em vários ViewModels.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String?) : UiState<Nothing>
}