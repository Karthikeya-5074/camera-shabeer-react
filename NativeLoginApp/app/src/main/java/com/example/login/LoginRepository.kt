package com.example.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {
    @POST("send-otp")
    suspend fun sendOtp(@Body body: Map<String, String>)
}

class LoginRepository(private val credentialManager: AWSCredentialManager) {
    private val api: LoginApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://example.com/") // placeholder
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(LoginApi::class.java)
    }

    suspend fun sendOtp(phone: String) = withContext(Dispatchers.IO) {
        val token = credentialManager.sessionToken.value ?: ""
        // Normally you'd add dynamic headers with an interceptor
        api.sendOtp(mapOf("phone" to phone))
    }
}
