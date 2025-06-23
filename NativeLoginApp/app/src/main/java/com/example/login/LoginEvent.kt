package com.example.login

sealed class LoginEvent {
    data class PhoneChanged(val phone: String) : LoginEvent()
    object SendOtp : LoginEvent()
}
