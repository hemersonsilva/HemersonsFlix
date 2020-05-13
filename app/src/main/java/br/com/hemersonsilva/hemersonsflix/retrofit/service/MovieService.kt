package br.com.hemersonsilva.hemersonsflix.retrofit.service

import br.com.hemersonsilva.hemersonsflix.model.Movie
import br.com.hemersonsilva.hemersonsflix.model.MovieResult
import retrofit2.Call
import retrofit2.http.*

interface MovieService {
    @GET("movie/popular")
    fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MovieResult>

    @GET("movie/{id}")
    fun getMovieById(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<Movie>

    @GET("movie/{id}/similar")
    fun getSimilarMovies(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MovieResult>

}
