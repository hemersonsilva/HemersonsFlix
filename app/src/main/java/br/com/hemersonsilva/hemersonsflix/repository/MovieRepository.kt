package br.com.hemersonsilva.hemersonsflix.repository

import br.com.hemersonsilva.hemersonsflix.asynctask.BaseAsyncTask
import br.com.hemersonsilva.hemersonsflix.database.dao.MovieDAO
import br.com.hemersonsilva.hemersonsflix.model.Movie
import br.com.hemersonsilva.hemersonsflix.model.MovieResult
import br.com.hemersonsilva.hemersonsflix.webclient.MovieWebClient

class MovieRepository(
    private val dao: MovieDAO,
    private val webclient: MovieWebClient = MovieWebClient()
) {
    fun getMovies(
        page: Int,
        onSuccess: (MovieResult?) -> Unit,
        onFailure: (error: String?) -> Unit,
        onComplete: () -> Unit
    ) = getApi(page = page, onSuccess = onSuccess, onFailure = onFailure, onComplete = onComplete)

    private fun getApi(
        page: Int,
        onSuccess: (MovieResult?) -> Unit,
        onFailure: (error: String?) -> Unit,
        onComplete: () -> Unit
    ) = webclient.getMovies(page = page, onSuccess = onSuccess, onFailure = onFailure, onComplete = onComplete)

    fun getApiMovieDetails(
        movieId: Int,
        onSucess: (Movie?) -> Unit,
        onFailure: (error: String?) -> Unit,
        onComplete: () -> Unit
    ) = webclient.getMovieDetails(movieId = movieId, onSuccess = onSucess, onFailure = onFailure, onComplete = onComplete)

    fun getApiSimilarMovies(
        movieId: Int,
        onSuccess: (MovieResult?) -> Unit,
        onFailure: (error: String?) -> Unit,
        onComplete: () -> Unit
    ) = webclient.getSimilarMovies(movieId = movieId, onSuccess = onSuccess, onFailure = onFailure, onComplete = onComplete)

    fun searchMovies(
        query: String,
        onSuccess: (MovieResult?) -> Unit,
        onFailure: (error: String?) -> Unit,
        onComplete: () -> Unit
    ) = webclient.searchMovies(query = query, onSuccess = onSuccess, onFailure = onFailure, onComplete = onComplete)

    fun internalSave(
        movie: Movie,
        onSuccess: (newMovie: Movie) -> Unit
    ) {
        BaseAsyncTask(whenStart = {
            dao.save(movie)
            dao.searchById(movie.movieId)
        }, whenFinish = { movieFound ->
            movieFound?.let {
                onSuccess(it)
            }
        }).execute()
    }

    fun internalSearch(
        onSuccess: (MutableList<Movie>) -> Unit
    ) {
        BaseAsyncTask(whenStart = {
            dao.searchAll()
        }, whenFinish = onSuccess)
            .execute()
    }

    fun internalSearchById(
        movieId: Int,
        onSuccess: (movie: Movie?) -> Unit
    ) {
        BaseAsyncTask(whenStart = {
            dao.searchById(movieId)
        }, whenFinish = onSuccess)
            .execute()
    }

    fun internalRemove(
        movie: Movie,
        onSuccess: () -> Unit
    ) {
        BaseAsyncTask(whenStart = {
            dao.remove(movie)
        }, whenFinish = {
            onSuccess()
        }).execute()
    }
}
