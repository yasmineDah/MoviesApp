package com.example.moviesapp.model

import com.example.moviesapp.model.Movie
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("page") val page: Int = 0,
    @SerializedName("results") val movies: List<Movie> = listOf()
)