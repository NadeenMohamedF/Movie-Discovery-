package com.example.mymovieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ActorsAdapter : RecyclerView.Adapter<ActorsAdapter.ActorViewHolder>() {

    private var actors: List<Actor> = emptyList()

    fun submitList(list: List<Actor>) {
        actors = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actor, parent, false)
        return ActorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val actor = actors[position]

        holder.actorName.text = actor.name ?: "Unknown"

        Glide.with(holder.actorImage.context)
            .load("https://image.tmdb.org/t/p/w500${actor.profile_path}")
            // if no actor photo
            .placeholder(android.R.color.darker_gray)
            .into(holder.actorImage)
    }

    override fun getItemCount(): Int = actors.size

    class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val actorImage: ImageView = itemView.findViewById(R.id.imageActor)
        val actorName: TextView = itemView.findViewById(R.id.textActorName)
    }
}
