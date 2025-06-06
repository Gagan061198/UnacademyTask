package com.example.testtemplate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T> : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<T>>(UiState.Initial)
    val uiState: StateFlow<UiState<T>> = _uiState.asStateFlow()

    fun setSuccess(data: T) {
        _uiState.value = UiState.Success(data)
    }


    private fun setLoading() {
        _uiState.value = UiState.Loading
    }

    fun setError(error: String) {
        _uiState.value = UiState.Error(error)
    }


    fun launchWithLoading(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                setLoading()
                block()
            } catch (e: Exception) {
                setError(e.message ?: "An error occurred")
            }
        }
    }
}

sealed class UiState<out T> {
    object Initial : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
} 