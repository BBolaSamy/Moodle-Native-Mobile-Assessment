package com.itcorner.moodleandroid.domain

sealed interface ViewState<out T> {
    data object Idle : ViewState<Nothing>
    data object Loading : ViewState<Nothing>
    data class Success<T>(val data: T) : ViewState<T>
    data class Empty(val message: String) : ViewState<Nothing>
    data class Error(val message: String) : ViewState<Nothing>
}
