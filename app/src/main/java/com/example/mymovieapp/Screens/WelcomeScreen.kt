package com.example.mymovieapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymovieapp.R
import com.example.mymovieapp.ui.theme.Black
import com.example.mymovieapp.ui.theme.CoolGray
import com.example.mymovieapp.ui.theme.OrangeCrayola
import com.example.mymovieapp.ui.theme.White


@Composable
fun WelcomeScreen(onGetStarted: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()
        .background(color =  Black)) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.intro_pic),
                contentDescription = "Intro picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Column (modifier = Modifier.padding(start = 24.dp,top=24.dp)){
                Text(
                    text = "Discover Your",
                    color = White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Favourite Movies",
                    color = OrangeCrayola,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
                Text(
                    text = "Watch Now or Watch Later",
                    color = White,
                    fontSize = 28.sp,

                )
                Text(
                    text = "you can browse movies and shows by\n" +
                            "genre, search for specific title, or\n" +
                            "check out our recommendation for you",
                    color = CoolGray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 16.dp)

                    )
                Button(onClick = onGetStarted,
                    modifier = Modifier
                    .width(300.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF7329),
                        contentColor = White)
                    ) {
                       Text(
                           text = "Get Started",
                           fontSize = 16.sp
                       )

                }



            }
        }
}
}
