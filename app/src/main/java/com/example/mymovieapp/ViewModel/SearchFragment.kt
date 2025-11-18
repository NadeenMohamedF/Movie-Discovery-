package com.example.mymovieapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var api: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchEditText = view.findViewById(R.id.search_editText)
        recyclerView = view.findViewById(R.id.search_recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        moviesAdapter = MoviesAdapter(emptyList())
        recyclerView.adapter = moviesAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()

                if (query.isNotBlank()) {
                    //searchMovies(query)
                    loadMoviesAndFilter(query)

                } else {
                    moviesAdapter.updateMovies(emptyList())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        //  RECEIVE INITIAL QUERY FROM HOMEPAGE
      val initialQuery = arguments?.getString("query") ?: ""


        if (initialQuery.isNotEmpty()) {
            searchEditText.setText(initialQuery)

            searchMovies(initialQuery)
        }

        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().findViewById<View>(R.id.search_fragment_container).visibility = View.GONE
    }

    private fun loadMoviesAndFilter(query: String) {
        val call = api.discoverMovies("fbb1ae6016c7313d0fdf0bb5784c5716", "popularity.desc")

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val allMovies = response.body()?.results ?: emptyList()

                    val filtered = allMovies.filter {
                        it.title.contains(query, ignoreCase = true)
                    }

                    moviesAdapter.updateMovies(filtered)
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {}
        })
    }


    private fun searchMovies(query: String) {
        val call = api.searchMovies("fbb1ae6016c7313d0fdf0bb5784c5716", query)

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results ?: emptyList()
                    moviesAdapter.updateMovies(movies)
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    companion object {
        fun newInstance(query: String): SearchFragment {
            val fragment = SearchFragment()
            val args = Bundle()
            args.putString("query", query)
            fragment.arguments = args
            return fragment
        }
    }


}
