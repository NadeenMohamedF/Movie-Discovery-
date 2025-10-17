package com.example.mymovieapp.ViewModel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mymovieapp.Data.AuthUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    var uiState by mutableStateOf(AuthUiState())
    // private set

    fun onEmailChange(new: String) { uiState = uiState.copy(email = new) }
    fun onPasswordChange(new: String) {
        if (new.length<=20) {
            uiState = uiState.copy(password = new)
        }
    }
    fun onConfirmPasswordChange(new: String) {
        if(new.length<=20) {
            uiState = uiState.copy(confirmPassword = new)
        }
    }

    fun signIn(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val email = uiState.email.trim()
        val pass = uiState.password

        if (email.isEmpty() || pass.isEmpty()) {
            val msg = "Please fill in all fields"
            uiState = uiState.copy(errorMessage = msg)
            onError(msg)
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            uiState = uiState.copy(isLoading = false)
            if (task.isSuccessful) {
                uiState = uiState.copy(user = auth.currentUser)
                onSuccess()
            } else {
                val msg =if( task.exception?.message ?.contains("no user record", ignoreCase = true)==true)
                    "Account doesn’t exist. Please sign up first."
                else
                    "Login failed. Check your credentials."
                uiState = uiState.copy(errorMessage = msg)
                onError(msg)
            }
        }
    }

    fun signUp(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val email = uiState.email.trim()
        val pass = uiState.password
        val confirm = uiState.confirmPassword



        if (email.isEmpty() || pass.isEmpty() ) {
            val msg = "Please fill all fields"
            uiState = uiState.copy(errorMessage = msg)
            onError(msg)
            return
        }

        if (pass != confirm) {
            val msg = "Passwords do not match"
            uiState = uiState.copy(errorMessage = msg)
            onError(msg)
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            uiState = uiState.copy(isLoading = false)
            if (task.isSuccessful) {
                uiState = uiState.copy(user = auth.currentUser)
                onSuccess()
            } else {
                val msg = task.exception?.message ?: "Sign up failed"
                uiState = uiState.copy(errorMessage = msg)
                onError(msg)
            }
        }
    }

    fun clearFields() {
        uiState= uiState.copy(
            email = "",
            password = "",
            confirmPassword = "",
            errorMessage = ""
        )
    }
    fun signOut() {
        auth.signOut()
        clearFields()
    }


}