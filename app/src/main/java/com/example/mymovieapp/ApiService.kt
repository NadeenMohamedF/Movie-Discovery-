package com.example.mymovieapp

//is the piece that tells Retrofit how to talk to the TMDB API.
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface ApiService {
    // For "Now Playing" movies endpoint
    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<MovieResponse>
    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>
}