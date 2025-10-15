package com.example.mymovieapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymovieapp.ViewModel.AuthUiState
import com.example.mymovieapp.R
import com.example.mymovieapp.ui.theme.Black
import com.example.mymovieapp.ui.theme.SemiBlack
import com.example.mymovieapp.ui.theme.White

enum class AuthMode { LOGIN, SIGN_UP }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    mode: AuthMode,
    state: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onSwitchMode: () -> Unit
) {
   // val title = if (mode == AuthMode.LOGIN) "Login" else "Sign Up"
    val buttonText = if (mode == AuthMode.LOGIN) "Sign In" else "Create Account"
    val switchText = if (mode == AuthMode.LOGIN)
        "Don't have an account? Sign Up"
    else
        "Already have an account? Login"


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = SemiBlack)
    ) {

        Image(
            painter = painterResource(R.drawable.intro_pic),
            contentDescription = "Welcome photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.58f)
               // .alpha(0.8f)
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = Black)
                .padding(16.dp)

            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = { Text("E-mail"
                        , color = White) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = { Text("Password",
                          color = White) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onSubmit,
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC95010),
                    contentColor = Color.White
                )
            ) {
                Text(
                    buttonText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            state.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Text(
                    switchText,
                    fontSize = 16.sp,
                    color = Color(0xFFE1D3D3),
                    modifier = Modifier.clickable{onSwitchMode()}
                )
//                Spacer(modifier = Modifier.width(10.dp))
//                Text(
//                    "Sign up now",
//                    color = Color(0xFFFF6D1D),
//                    fontSize = 16.sp
//                )
            }
        }
    }
}


