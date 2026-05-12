package com.habitforest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitforest.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle    : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: HabitRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state = _state.asStateFlow()

    val isLoggedIn: Boolean get() = repo.isLoggedIn()

    fun signInAnonymously() {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            repo.signInAnonymously()
                .onSuccess { _state.value = AuthState.Success }
                .onFailure { _state.value = AuthState.Error(it.message ?: "Sign-in failed") }
        }
    }
}
