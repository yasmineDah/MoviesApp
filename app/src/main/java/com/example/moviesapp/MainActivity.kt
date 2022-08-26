package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.api.Repository
import com.example.moviesapp.databinding.ActivityMainBinding
import com.example.moviesapp.model.Movie

class MainActivity : AppCompatActivity() {

    private lateinit var moviesRecyclerView: RecyclerView
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var moviesLm: LinearLayoutManager
    private var moviesPage = 1 // to load more movies
    private var i = 0

    //data binding to gain time at runtime
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()

        moviesRecyclerView = binding.rvmoviesImages

        moviesLm = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        moviesRecyclerView.layoutManager = moviesLm

        moviesAdapter = MoviesAdapter(this, mutableListOf())
        moviesRecyclerView.adapter = moviesAdapter

        getMovies()
    }

    private fun fetchMovies(movies: List<Movie>) {
        if(i == 0){
            binding.apply {
                movieTitle.text = movies[0].title
                movieReleaseDate.text = movies[0].releaseDate
                movieOverview.text = movies[0].overview
                i++
            }
        }
        moviesAdapter.addWhileScrolling(movies)
        loadMoreMoviesOnScrollListener()
    }

    private fun onError() {
        Toast.makeText(this, getString(R.string.fetching_error), Toast.LENGTH_LONG)
            .show()
    }

    private fun getMovies() {
        Repository.getTrendingMovies(
            moviesPage,
            ::fetchMovies,
            ::onError
        )
    }

    private fun loadMoreMoviesOnScrollListener() {
        // load next page's movies while scrolling on recyclerView
        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val nbItems = moviesLm.itemCount
                val visibleItems = moviesLm.childCount
                val firstItem = moviesLm.findFirstVisibleItemPosition()

                if (firstItem + visibleItems >= nbItems / 2) {
                    moviesRecyclerView.removeOnScrollListener(this)
                    moviesPage++
                    getMovies()
                }
            }
        })
    }
}