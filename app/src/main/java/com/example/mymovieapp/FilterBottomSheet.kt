package com.example.mymovieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val actionBtn = view.findViewById<Button>(R.id.action_btn)
        val comedyBtn = view.findViewById<Button>(R.id.comedy_btn)
        val dramaBtn = view.findViewById<Button>(R.id.drama_btn)
        val horrorBtn = view.findViewById<Button>(R.id.horror_btn)
        val romanceBtn = view.findViewById<Button>(R.id.romantic_btn)

        actionBtn.setOnClickListener { sendGenre("28") }    // Action
        comedyBtn.setOnClickListener { sendGenre("35") }   // Comedy
        dramaBtn.setOnClickListener { sendGenre("18") }    // Drama
        horrorBtn.setOnClickListener { sendGenre("27") }    // Horror
        romanceBtn.setOnClickListener { sendGenre("10749") }  // romance
    }

    private fun sendGenre(genreId: String) {
        (activity as? HomePage)?.loadMoviesByGenre(genreId)
        dismiss()
    }

}
