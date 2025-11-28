package com.example.mymovieapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.example.mymovieapp.databinding.ActivityMovieDetailsBinding



class MovieDetailsActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var actorsAdapter: ActorsAdapter
    private lateinit var apiService: ApiService

    private val apiKey = "fbb1ae6016c7313d0fdf0bb5784c5716"

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var userId: String? = null

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonFavorite.isEnabled = false

        // ✅ Anonymous sign-in for Firebase
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnSuccessListener {
                userId = it.user?.uid
                binding.buttonFavorite.isEnabled = true
                //loadFavoriteState(movieId)
                Log.d("FirebaseAuth", "Signed in with UID: $userId")
            }
            .addOnFailureListener {
                Log.e("FirebaseAuth", "Sign-in failed", it)
            }

        // Setup RecyclerView for actors
        actorsAdapter = ActorsAdapter()
        binding.recyclerActors.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerActors.adapter = actorsAdapter

        // Setup Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        // Get movieId from intent
        val movieId = intent.getIntExtra("movie_id", -1)
        if (movieId == -1) {
            finish()
            return
        }

        // Fetch movie details
        fetchMovieDetails(movieId)
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonFavorite.isEnabled = false

        // 1️⃣ جيبي movieId الأول
        val movieId = intent.getIntExtra("movie_id", -1)
        if (movieId == -1) {
            finish()
            return
        }

        // 2️⃣ Firebase login
        //FirebaseAuth.getInstance().signInAnonymously()

            //.addOnSuccessListener {
                //userId = it.user?.uid
        userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            // user مش مسجل دخول
            binding.buttonFavorite.isEnabled = false
        } else {
            // user مسجل دخول
            binding.buttonFavorite.isEnabled = true
            loadFavoriteState(movieId)
        }
               // Log.d("FirebaseAuth", "Signed in with UID: $userId")
            //}
           // .addOnFailureListener {
               // Log.e("FirebaseAuth", "Sign-in failed", it)
           // }

        // باقي الإعدادات
        actorsAdapter = ActorsAdapter()
        binding.recyclerActors.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerActors.adapter = actorsAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        // 4️⃣ Fetch movie details
        fetchMovieDetails(movieId)

        // 5️⃣ favorite button toggle
      /*  binding.buttonFavorite.setOnClickListener {
            toggleFavorite(movieId,movie)
        }*/
    }


    private fun fetchMovieDetails(movieId: Int) {
        lifecycleScope.launch {
            try {
                val movie = apiService.getMovieDetails(movieId, apiKey)

                // Log response
                Log.d("API_DATA", "Movie = $movie")

                // Bind UI
                binding.textTitle.text = movie.title ?: ""
                binding.textOverview.text = movie.overview ?: ""

                Glide.with(this@MovieDetailsActivity)
                    .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                    .placeholder(android.R.color.darker_gray)
                    .into(binding.imagePoster)

                binding.ratingBar.rating = movie.vote_average?.toFloat() ?: 0f
                binding.textUserRating.text = "${movie.vote_average ?: 0}/10"

                // Fetch credits
                fetchMovieCredits(movieId)
                binding.buttonFavorite.setOnClickListener {
                    toggleFavorite(movieId,movie)
                }
                // Firebase favorites + ratings
                loadFavoriteState(movieId)

                binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                    saveRating(movieId, rating)
                }

            } catch (e: Exception) {
                Log.e("MovieDetails", "Error fetching movie details", e)
            }
        }
    }


    private fun fetchMovieCredits(movieId: Int) {
        lifecycleScope.launch {
            try {
                val credits = apiService.getMovieCredits(movieId, apiKey)
                Log.d("API_DATA", "Credits = $credits")

                credits.cast?.let { castList ->
                    actorsAdapter.submitList(
                        castList.map { actor ->
                            Actor(actor.name, actor.profile_path)
                        }
                    )
                }

            } catch (e: Exception) {
                Log.e("MovieDetails", "Error fetching credits", e)
            }
        }
    }


    private fun loadFavoriteState(movieId: Int) {
        val currentUserId = userId ?: return
        val favRef = firestore.collection("users")
            .document(currentUserId)
            .collection("favorites")
            .document(movieId.toString())

        favRef.get().addOnSuccessListener { doc ->
            val isFavorite = doc.exists()
            binding.buttonFavorite.text = if (isFavorite)
                getString(R.string.action_remove_from_favorites)
            else
                getString(R.string.action_add_to_favorites)
        }
    }

    private fun toggleFavorite(movieId: Int , movie: MovieDetails) {
        val currentUserId = userId ?: return
        val favRef = firestore.collection("users")
            .document(currentUserId)
            .collection("favorites")
            .document(movieId.toString())

        favRef.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                favRef.delete()
                binding.buttonFavorite.text = getString(R.string.action_add_to_favorites)
            } else {
                val data = mapOf(
                    "id" to movieId,
                    "title" to movie.title,
                    "poster" to movie.poster_path,
                    "vote" to movie.vote_average
                )

                favRef.set(data)
               // favRef.set(mapOf("favorite" to true))
                binding.buttonFavorite.text = getString(R.string.action_remove_from_favorites)
            }
        }
    }

    private fun saveRating(movieId: Int, rating: Float) {
        val currentUserId = userId ?: return
        val ratingsRef = firestore.collection("ratings")
            .document(movieId.toString())

        ratingsRef.update(currentUserId, rating)
            .addOnSuccessListener {
                binding.textUserRating.text = "$rating/5"
            }
            .addOnFailureListener {
                ratingsRef.set(mapOf(userId to rating))
            }
    }
}