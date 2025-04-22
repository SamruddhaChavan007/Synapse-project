package com.example.synapse.feature.auth.signup

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
fun SignUpScreen(navController: NavController) {
    val viewModel: SignUpViewModel = hiltViewModel()
    val backgroundColor = Color(0xFF007A56)
    var showpassword by remember {
        mutableStateOf(value = false)
    }
    var confirm_showpassword by remember {
        mutableStateOf(value = false)
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }
    var confirm_password by remember {
        mutableStateOf("")
    }

    val uiState = viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {
        when (uiState.value) {
            is SignUpState.Success -> {
                navController.navigate("home")
            }

            is SignUpState.Error -> {
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
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                //placeholder = { Text(text = "Username")},
                label = { Text(text = "Username") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                ))
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
                ))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
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
                ))
            OutlinedTextField(
                value = confirm_password,
                onValueChange = { confirm_password = it },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (confirm_showpassword) {
                    VisualTransformation.None
                }
                else {
                    PasswordVisualTransformation()
                },
                //placeholder = {Text(text = "Password")},
                label = { Text(text = "Confirm Password") },
                trailingIcon = {
                    if (confirm_showpassword) {
                        IconButton(onClick = {confirm_showpassword = false}) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "hide_password",
                                tint = Color.Black
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { confirm_showpassword = true}) {
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
                ),
                isError = password.length > 0 && password != confirm_password && password.isNotEmpty()
            )
            Spacer(modifier = Modifier.size(16.dp))
            if (uiState.value == SignUpState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        viewModel.signUp(username, email, password)
                    }, modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.White,
                        disabledContentColor = Color.Black
                    ),
                    enabled = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirm_password.isNotEmpty()
                ) {
                    Text(text = "Sign In")
                }
                TextButton(
                    onClick = { navController.popBackStack() }) {
                    Text(text = "Already have an account? Sign In")
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen(navController = rememberNavController())
}