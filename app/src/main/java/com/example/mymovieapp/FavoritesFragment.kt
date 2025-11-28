package com.example.mymovieapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.example.mymovieapp.databinding.FragmentFavoritesBinding
import com.google.firebase.auth.FirebaseAuth

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var userId: String? = null
    private lateinit var adapter: FavoritesAdapter  // نفس Adapter بتاع الأفلام عندك

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = FirebaseAuth.getInstance().currentUser?.uid

        adapter = FavoritesAdapter(emptyList())
        binding.recyclerFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavorites.adapter = adapter


        loadFavorites()
    }

    private fun loadFavorites() {
        val uid = userId ?: return

        firestore.collection("users")
            .document(uid)
            .collection("favorites")
            .get()
            .addOnSuccessListener { result ->

                val movies = result.documents.map { doc ->
                    Movie(
                        id = doc.getLong("id")?.toInt() ?: 0,
                        title = doc.getString("title") ?: "",
                        poster_path = doc.getString("poster") ?: "",
                        backdrop_path = "",
                        vote_average = doc.getDouble("vote") ?: 0.0,
                        overview = "",
                        release_date = ""
                    )
                }

                adapter.updateMovies(movies)
            }
    }
}
