package com.niyaj.poposroom.features.common.event

sealed interface UiState<out T> {
    object Loading: UiState<Nothing>
    object Empty: UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
}