package com.example.mymovieapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
//import kotlinx.android.synthetic.main.activity_home_page.*

class HomePage : AppCompatActivity() {
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var upcomingAdapter: MoviesAdapter
    private lateinit var upcomingRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)

        val Movies_recyclerView = findViewById<RecyclerView>(R.id.movies_recyclerView)
        Movies_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
         moviesAdapter = MoviesAdapter(emptyList())
        Movies_recyclerView.adapter = moviesAdapter

        val favorite = findViewById<ImageView>(R.id.favorite_icon)
        val settings = findViewById<ImageView>(R.id.settings_icon)
        val profile = findViewById<ImageView>(R.id.profile_icon)

        favorite.setOnClickListener {
            Toast.makeText(this, "favorite clicked", Toast.LENGTH_SHORT).show()
            // Example: navigate to home or scroll to top
        }

        settings.setOnClickListener {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show()
            // Example: open search screen
        }

        profile.setOnClickListener {
            Toast.makeText(this, "profile clicked", Toast.LENGTH_SHORT).show()
            // Example: navigate to profile screen
        }

        setupUpcomingRecyclerView()
        fetchMovies()
        fetchUpcomingMovies()

    }
    private fun fetchMovies() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        val call = api.getNowPlayingMovies("fbb1ae6016c7313d0fdf0bb5784c5716")

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results ?: emptyList()
                    moviesAdapter.updateMovies(movies)

                    // 🎞 Fill image slider with movie backdrops
                    val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
                    val sliderList = movies.map {
                        //sliderModel is from slider liberary
                        SlideModel("https://image.tmdb.org/t/p/w500${it.backdrop_path}", it.title, ScaleTypes.CENTER_CROP)
                    }
                    imageSlider.setImageList(sliderList)
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun setupUpcomingRecyclerView() {
        upcomingRecyclerView = findViewById(R.id.upcoming_recyclerView)
        upcomingAdapter = MoviesAdapter(emptyList())
        upcomingRecyclerView.adapter = upcomingAdapter
        upcomingRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun fetchUpcomingMovies() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        val call = api.getUpcomingMovies("fbb1ae6016c7313d0fdf0bb5784c5716")
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movieList = response.body()?.results ?: emptyList()
                    upcomingAdapter.updateMovies(movieList)
                } else {
                    Log.e("API_ERROR", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e("API_ERROR", "Failed to fetch upcoming movies: ${t.message}")
            }
        })
    }

}