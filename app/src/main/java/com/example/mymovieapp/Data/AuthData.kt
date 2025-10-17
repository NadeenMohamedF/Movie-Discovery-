package com.example.mymovieapp.Data

import com.google.firebase.auth.FirebaseUser

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val user: FirebaseUser? = null
)

enum class AuthMode {
    SIGNIN,
    SIGN_UP
}