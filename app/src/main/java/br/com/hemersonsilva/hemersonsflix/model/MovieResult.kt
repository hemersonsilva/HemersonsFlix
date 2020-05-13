package br.com.hemersonsilva.hemersonsflix.model

import com.google.gson.annotations.SerializedName

data class MovieResult(
    @SerializedName( "results")
    val listMovies: MutableList<Movie>
)