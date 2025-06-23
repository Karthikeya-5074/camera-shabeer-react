package com.example.login

sealed class LoginEffect {
    object NavigateOtp : LoginEffect()
}
