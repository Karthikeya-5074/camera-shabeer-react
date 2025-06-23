package com.example.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel(), navigateOtp: () -> Unit) {
    val state = viewModel.state.collectAsState().value

    if (state is LoginState.Success) {
        navigateOtp()
    }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = viewModel.phone,
            onValueChange = { viewModel.onEvent(LoginEvent.PhoneChanged(it)) },
            label = { Text("Phone") },
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.onEvent(LoginEvent.SendOtp) }) {
            if (state is LoginState.Loading) {
                CircularProgressIndicator(Modifier.size(24.dp))
            } else {
                Text("Send OTP")
            }
        }
        if (state is LoginState.Error) {
            Text(state.message, color = MaterialTheme.colors.error)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(navigateOtp = {})
}
