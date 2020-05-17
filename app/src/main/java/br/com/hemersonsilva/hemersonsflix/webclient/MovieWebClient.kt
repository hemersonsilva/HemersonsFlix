package br.com.hemersonsilva.hemersonsflix.webclient

import br.com.hemersonsilva.hemersonsflix.model.Movie
import br.com.hemersonsilva.hemersonsflix.model.MovieResult
import br.com.hemersonsilva.hemersonsflix.retrofit.AppRetrofit
import br.com.hemersonsilva.hemersonsflix.retrofit.service.MovieService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val API_KEY = "92c82e0f2b6f63bdfef174c5f118b65c"
private const val LANGUAGE = "pt-BR"
private const val REQUEST_NOT_SUCCESSFUL = "Requisição não sucedida"

class MovieWebClient(
    private val service: MovieService = AppRetrofit().movieService
) {
    private fun <T> executeRequest(
        call: Call<T>,
        onSuccess: (newMovies: T?) -> Unit,
        onFailure: (error: String?) -> Unit,
        onComplete: () -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                    onComplete()
                } else {
                    onFailure(REQUEST_NOT_SUCCESSFUL)
                    onComplete()
                }
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailure(t.message)
                onComplete()
            }
        })
    }

    fun getMovies(
        onSuccess: (MovieResult?) -> Unit,
        onFailure: (error: String?) -> Unit,
        onComplete: () -> Unit,
        page: Int
    ) {
        executeRequest(
            service.getMovies(API_KEY,LANGUAGE, page),
            onSuccess,
            onFailure,
            onComplete
        )
    }

    fun getMovieDetails(
        movieId: Int,
        onSuccess: (Movie?) -> Unit,
        onFailure: (error: String?) -> Unit,
        onComplete: () -> Unit
    ) {
        executeRequest(
            service.getMovieById(movieId, API_KEY, LANGUAGE),
            onSuccess,
            onFailure,
            onComplete
        )
    }

    fun getSimilarMovies(
        movieId: Int,
        onSuccess: (MovieResult?) -> Unit,
        onFailure: (error: String?) -> Unit,
        onComplete: () -> Unit
    ) {
        executeRequest(
            service.getSimilarMovies(movieId, API_KEY, LANGUAGE),
            onSuccess,
            onFailure,
            onComplete
        )
    }

    fun searchMovies(
        query: String,
        onSuccess: (MovieResult?) -> Unit,
        onFailure: (error: String?) -> Unit,
        onComplete: () -> Unit
    ) {
        executeRequest(
            service.searchMovies(query = query, apiKey = API_KEY, language = LANGUAGE),
            onSuccess,
            onFailure,
            onComplete
        )
    }
}
