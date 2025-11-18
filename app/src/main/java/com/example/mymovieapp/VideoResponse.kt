package com.example.mymovieapp

data class VideoResponse(
    val id: Int,
    val results: List<VideoItem>
)

data class VideoItem(
    val key: String,
    val site: String,
    val type: String
)
