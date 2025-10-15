package com.example.mymovieapp.NavHost



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mymovieapp.Screens.AuthMode
import com.example.mymovieapp.Screens.AuthScreen
import com.example.mymovieapp.Screens.HomeScreen
import com.example.mymovieapp.ViewModel.AuthViewModel
import com.example.mymovieapp.Screens.WelcomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost(navController: NavHostController) {
    val start = if (FirebaseAuth.getInstance().currentUser != null) "home" else "welcome"
    val authVM: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = start) {
        composable("welcome") {
            WelcomeScreen(onGetStarted = { navController.navigate("auth") })
        }
        composable("auth") {
            var mode by remember { mutableStateOf(AuthMode.LOGIN) }

            AuthScreen(
                mode = mode,
                state = authVM.uiState,
                onEmailChange = authVM::onEmailChange,
                onPasswordChange = authVM::onPasswordChange,
                onConfirmPasswordChange = authVM::onConfirmPasswordChange,
                onSubmit = {
                    if (mode == AuthMode.LOGIN) {
                        authVM.signIn(
                            onSuccess = { navController.navigate("home") { popUpTo("welcome") { inclusive = true } } },
                            onError = {}
                        )
                    } else {
                        authVM.signUp(
                            onSuccess = { navController.navigate("home") { popUpTo("welcome") { inclusive = true } }},
                            onError = {}
                        )
                    }
                },
                onSwitchMode = {
                     authVM.clearFields()
                    mode = if (mode == AuthMode.LOGIN) AuthMode.SIGN_UP else AuthMode.LOGIN
                }
            )
        }
        composable("home") {
            HomeScreen(onSignOut = {
//                authVM.signOut()
                navController.navigate("welcome") {
                    popUpTo("welcome") { inclusive = true }
                }
            })
        }
    }
}





