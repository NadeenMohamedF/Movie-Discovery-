package com.example.mymovieapp.NavHost

import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mymovieapp.Data.AuthMode
import com.example.mymovieapp.Screens.AuthScreen
import com.example.mymovieapp.Screens.HomeScreen
import com.example.mymovieapp.ViewModel.AuthViewModel
import com.example.mymovieapp.Screens.WelcomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost(navController: NavHostController) {
    var authMode by rememberSaveable { mutableStateOf(AuthMode.SIGNIN) }
    val start = if (FirebaseAuth.getInstance().currentUser != null) "home" else "welcome"
    val authVM: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = start) {
        composable("welcome") {
            WelcomeScreen { mode ->
                authMode = mode
                navController.navigate("auth")
            }}
        composable("auth") {


            AuthScreen(
                mode = authMode,
                state = authVM.uiState,
                onEmailChange = authVM::onEmailChange,
                onPasswordChange = authVM::onPasswordChange,
                onConfirmPasswordChange = authVM::onConfirmPasswordChange,
                onSubmit = {
                    if (authMode == AuthMode.SIGNIN) {
                        authVM.signIn(
                            onSuccess = {
                                navController.navigate("home") {
                                    popUpTo("welcome") { inclusive = true }
                                }
                            },
                            onError = {}
                        )
                    } else {
                        authVM.signUp(
                            onSuccess = {
                                navController.navigate("home") {
                                    popUpTo("welcome") { inclusive = true }
                                }
                            },
                            onError = {}
                        )
                    }
                },
                onSwitchMode = {
                    authVM.clearFields()
                    authMode = if (authMode == AuthMode.SIGNIN) AuthMode.SIGN_UP else AuthMode.SIGNIN
                }
            )
        }

        composable("home") {
            HomeScreen(onSignOut = {
                authVM.signOut()
                navController.navigate("welcome") {
                    popUpTo("welcome") { inclusive = true }
                }
            })
        }
    }
}




