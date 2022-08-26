package com.example.moviesapp.api

import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MoviesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Repository {
    private val retrofitClient: RetrofitClient

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitClient = retrofit.create(RetrofitClient::class.java)
    }

    fun getTrendingMovies(
        page: Int = 1,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        retrofitClient.getPopularMovies(page = page)
            .enqueue(object : Callback<MoviesResponse> {
                override fun onResponse(
                    call: Call<MoviesResponse>,
                    response: Response<MoviesResponse>
                ) {
                    // response is the JSON file
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else
                            onError.invoke()
                    } else
                        onError.invoke()
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }
}