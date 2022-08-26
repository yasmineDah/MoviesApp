package com.example.moviesapp

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MoviePalette
import com.squareup.picasso.Picasso

class MoviesAdapter(private val context: MainActivity, private var movies: MutableList<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieImage: ImageView = itemView.findViewById(R.id.movieImage)
        val movieRating: TextView = itemView.findViewById(R.id.movieRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie = movies[position]
        //picasso is much better than glide regarding to library size and images loading speed
        Picasso.get().load("https://image.tmdb.org/t/p/w500${currentMovie.posterPath}")
            .placeholder(R.drawable.loading).into(holder.movieImage)
        holder.movieRating.text = currentMovie.voteAverage.toString()
        holder.itemView.setOnClickListener {
            displayData(currentMovie)
        }
    }

    override fun getItemCount() = movies.size


    private fun displayData(currentMovie: Movie) {
        context.binding.apply {
            movieTitle.text = currentMovie.title
            movieReleaseDate.text = currentMovie.releaseDate
            movieOverview.text = currentMovie.overview
        }
        setBackgroundColors("https://image.tmdb.org/t/p/w500${currentMovie.posterPath}")
    }


    fun addWhileScrolling(moviesA: List<Movie>) {
        movies.addAll(moviesA)
        notifyItemRangeInserted(
            movies.size,
            movies.size - 1
        )
    }


    private fun setBackgroundColors(url: String) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate {
                            val color = it?.darkMutedSwatch?.rgb ?: 0
                            context.binding.clRoot.setBackgroundColor(color)
                            context.binding.palette = MoviePalette(color)

                            context.binding.movieTitle.setTextColor(it?.lightMutedSwatch?.rgb ?: 0)
                            context.binding.movieOverview.setTextColor(
                                it?.lightMutedSwatch?.rgb ?: 0
                            )
                            context.binding.movieReleaseDate.setTextColor(
                                it?.lightMutedSwatch?.rgb ?: 0
                            )
                        }
                }
            })
    }
}