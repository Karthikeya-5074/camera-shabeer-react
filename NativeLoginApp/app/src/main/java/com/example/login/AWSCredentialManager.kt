package com.example.login

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobile.client.AWSMobileClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AWSCredentialManager(context: Context) {
    private val awsConfig = AWSConfiguration(context)
    private val credentialsProvider = CognitoCachingCredentialsProvider(
        context,
        awsConfig
    )

    private val _sessionToken = MutableStateFlow<String?>(null)
    val sessionToken: StateFlow<String?> = _sessionToken.asStateFlow()

    suspend fun getCredentials() {
        val creds = credentialsProvider.credentials
        _sessionToken.value = creds.sessionToken
    }

    suspend fun refreshCredentials() {
        credentialsProvider.refresh()
        getCredentials()
    }
}
