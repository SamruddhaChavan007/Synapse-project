package com.example.synapse


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.synapse.feature.auth.signin.SignInScreen
import com.example.synapse.feature.auth.signup.SignUpScreen
import com.example.synapse.feature.home.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable

fun MainApp() {
    Surface(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        val currentuser = FirebaseAuth.getInstance().currentUser

        val start = if (currentuser != null) "home" else "login"



        NavHost(navController = navController, startDestination = start) {

            composable("login") {
                SignInScreen(navController)
            }
            composable("signup") {
                SignUpScreen(navController)
            }
            composable("home") {
                HomeScreen(navController)
            }
        }
    }
}