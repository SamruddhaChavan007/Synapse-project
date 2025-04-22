package com.example.synapse.feature.auth.signin


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.synapse.ui.theme.Pacifico
import com.example.synapse.ui.theme.Roboto


@Composable
fun SignInScreen(navController: NavController) {
    val viewModel: SignInViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()
    val backgroundColor = Color(0xFF007A56)
    var showpassword by remember {
        mutableStateOf(value = false)
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {
        when(uiState.value) {
            is SignInState.Success -> {
                navController.navigate("home")
            }
            is SignInState.Error -> {
                //Handle Error
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .background(backgroundColor)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Synapse",
                fontSize = 70.sp,
                fontFamily = Pacifico,
                color = Color.White
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = "CONNECT • CHAT • EVOLVE",
                fontSize = 15.sp,
                fontFamily = Roboto,
                color = Color.White
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                //placeholder = { Text(text = "Email")},
                label = { Text(text = "Email") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                )
                )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                //visualTransformation = PasswordVisualTransformation(),
                visualTransformation = if (showpassword) {
                    VisualTransformation.None
                }
                        else {
                    PasswordVisualTransformation()
                },
                //placeholder = {Text(text = "Password")},
                label = { Text(text = "Password") },
                trailingIcon = {
                    if (showpassword) {
                        IconButton(onClick = {showpassword = false}) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "hide_password",
                                tint = Color.Black
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { showpassword = true}) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "show_password",
                                tint = Color.Black
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                )
                )
            Spacer(modifier = Modifier.size(16.dp))
            if (uiState.value == SignInState.Loading) {
                CircularProgressIndicator()
            }
            else {
                Button(
                    onClick = { viewModel.signIn(email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.White,
                        disabledContentColor = Color.Black
                    ),
                    enabled = email.isNotEmpty() && password.isNotEmpty() && (uiState.value == SignInState.Nothing || uiState.value == SignInState.Error)
                ) {
                    Text(text = "Sign In")
                }
                TextButton(onClick = { navController.navigate("signup") }) {
                    Text(text = "Don't have an account? Sign Up", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignInScreen() {
    SignInScreen(navController = rememberNavController())
}