package com.example.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect = _effect.asSharedFlow()

    var phone: String = ""
        private set

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.PhoneChanged -> phone = event.phone
            LoginEvent.SendOtp -> sendOtp()
        }
    }

    private fun sendOtp() {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                repository.sendOtp(phone)
                _state.value = LoginState.Success
                _effect.emit(LoginEffect.NavigateOtp)
            } catch (e: Exception) {
                _state.value = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
