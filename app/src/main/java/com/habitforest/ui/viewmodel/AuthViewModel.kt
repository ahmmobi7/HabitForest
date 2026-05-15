package com.habitforest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitforest.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import android.util.Log

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
        Log.d("AuthViewModel", "signInAnonymously() called")
        viewModelScope.launch {
            _state.value = AuthState.Loading
            repo.signInAnonymously()
                .onSuccess { 
                    Log.d("AuthViewModel", "Sign-in successful")
                    _state.value = AuthState.Success 
                }
                .onFailure { 
                    val message = if (it.message?.contains("provider_disabled", ignoreCase = true) == true) {
                        "Anonymous auth is disabled in Supabase dashboard."
                    } else {
                        it.message ?: "Sign-in failed"
                    }
                    Log.e("AuthViewModel", "Sign-in failed: $message", it)
                    _state.value = AuthState.Error(message)
                }
        }
    }
}
