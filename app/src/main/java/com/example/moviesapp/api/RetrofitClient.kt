package com.example.moviesapp.api

import com.example.moviesapp.model.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitClient {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = "c9856d0cb57c3f14bf75bdc6c063b8f3"
    ): Call<MoviesResponse>
}