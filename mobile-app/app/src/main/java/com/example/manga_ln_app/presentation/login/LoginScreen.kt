package com.example.manga_ln_app.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = hiltViewModel(),
    onAuthSelf: (String) -> Unit,
    onReturn: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            state.loggedInRole?.let { onAuthSelf(it) }
        }
    }

    LoginScreen(
        state = state,
        onAction = { action ->
            viewModel.onAction(action)
        },
        onReturn = onReturn
    )
}


@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onReturn: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = state.username,
            onValueChange = { onAction(LoginAction.OnUsernameChange(it)) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onAction(LoginAction.OnPasswordChange(it)) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onAction(LoginAction.OnLogin) },
                enabled = !state.isLoading
            ) {
                Text("Login")
            }

            Button(
                onClick = { onAction(LoginAction.OnRegister) },
                enabled = !state.isLoading
            ) {
                Text("Register")
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp)
            )
        }

        state.error?.let { error ->
            Dialog(
                onDismissRequest = { onReturn() }
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(150.dp, 350.dp)
                        .heightIn(75.dp, 500.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(Color.White)
                        .padding(16.dp)

                ){
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .wrapContentHeight()
                    )
                }
            }
        }

        if( state.isLoggedIn and state.isRegistered) {
            Dialog(
                onDismissRequest = { onReturn() }
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(150.dp, 350.dp)
                        .heightIn(75.dp, 500.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(Color.White)
                        .padding(16.dp)

                ){
                    Text(
                        text = "You have successfully registered! Now you can log in and browse",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .wrapContentHeight()
                    )
                }
            }
        }
    }
}
