package br.com.hemersonsilva.hemersonsflix.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import br.com.hemersonsilva.hemersonsflix.R
import br.com.hemersonsilva.hemersonsflix.constants.BASE_URL_POSTER_PATH
import br.com.hemersonsilva.hemersonsflix.constants.MOVIE_KEY_ID
import br.com.hemersonsilva.hemersonsflix.extensions.hideProgressBar
import br.com.hemersonsilva.hemersonsflix.extensions.showMessage
import br.com.hemersonsilva.hemersonsflix.extensions.showProgress
import br.com.hemersonsilva.hemersonsflix.model.Movie
import br.com.hemersonsilva.hemersonsflix.ui.recyclerview.adapter.MovieAdapter
import br.com.hemersonsilva.movies.database.AppDatabase
import br.com.hemersonsilva.hemersonsflix.repository.MovieRepository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie.*

class MovieActivity : AppCompatActivity() {

    private val movieId: Int by lazy {
        intent.getIntExtra(MOVIE_KEY_ID, 0)
    }

    private val repository by lazy {
        MovieRepository(AppDatabase.getInstance(this).movieDAO)
    }

    private val adapter by lazy {
        MovieAdapter(
            context = this
        ) {
            favorited = false
            refreshScreen(it.movieId)
        }
    }

    private lateinit var similarMovies: MutableList<Movie>
    private lateinit var movie: Movie
    private var favorited: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        configToolBar()
        configRecyclerView()
        checkMovieId()
        refreshScreen(movieId)
        initListenerButons()
    }

    private fun refreshScreen(movieId: Int) {
        getMovieDetails(movieId)
        getSimilarMovies(movieId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.movie_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.movie_menu_favorite -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configToolBar() {
        setSupportActionBar(activity_movie_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        supportActionBar?.title = null
    }

    private fun configRecyclerView() {
        activity_movie_recycler_view_similar.adapter = adapter
        activity_movie_recycler_view_similar.layoutManager = GridLayoutManager(this, 4)
    }

    private fun checkMovieId() {
        if (movieId == 0) {
            showMessage(getString(R.string.movie_not_found))
            finish()
        }
    }

    private fun checkFavoritedMovie(movieId: Int) {
        repository.internalSearchById(movieId) {
            if (it != null) {
                favorited = it.favorited
                setButton(favorited)
            } else {
                setButton(favorited)
            }
        }
    }

    private fun setButton(favorited: Boolean) {
        activity_movie_button_favorite.setCompoundDrawablesWithIntrinsicBounds(
            0, isImage(favorited), 0, 0
        )
        activity_movie_button_favorite.text = isTitle(favorited)
    }

    private fun isTitle(favorited: Boolean): String {
        if (favorited) {
            return getString(R.string.remove_from_list)
        }
        return getString(R.string.my_list)
    }

    private fun isImage(favorited: Boolean): Int {
        if (favorited) {
            return R.drawable.ic_remove_black_24dp
        }
        return R.drawable.ic_add_black_24dp
    }

    private fun isMessage(favorited: Boolean): String {
        if (favorited) {
            return getString(R.string.movie_added_from_list)
        }
        return getString(R.string.movie_removed_from_list)
    }

    private fun setViews(movie: Movie) {
        activity_movie_text_view_title.text = movie.title
        activity_movie_text_view_desc.text = movie.descreption
        activity_my_list_text_view_vote_average.text = movie.voteAverage.toString()
        Picasso.get().load(BASE_URL_POSTER_PATH + movie.backdropPath)
            .into(activity_movie_image_view_backdrop_path)
    }

    private fun initListenerButons() {
        initListenerFavoriteButton()
        initListenerShareButton()
        initListenerDownloadButton()
    }

    private fun initListenerFavoriteButton() {
        activity_movie_button_favorite.setOnClickListener {

            if (favorited) {
                movie.favorited = favorited
                repository.internalRemove(movie) {
                    favorited = false
                    setButton(favorited)
                    showMessage(isMessage(favorited))
                }
            } else {
                favorited = true
                movie.favorited = favorited
                repository.internalSave(movie) {
                    setButton(favorited)
                    showMessage(isMessage(favorited))
                }
            }
        }
    }

    private fun initListenerShareButton() {
        activity_movie_button_share.setOnClickListener {
            showMessage(getString(R.string.in_development))
        }
    }

    private fun initListenerDownloadButton() {
        activity_movie_button_download.setOnClickListener {
            showMessage(getString(R.string.in_development))
        }
    }

    private fun getMovieDetails(movieId: Int) {
        showProgress(activity_movie_progress_bar)
        repository.getApiMovieDetails(
            movieId,
            onSucess = {
                if (it != null) {
                    movie = it
                    checkFavoritedMovie(movieId)
                    setViews(movie)
                }
            }, onFailure = {
                showMessage(getString(R.string.movie_not_found))
            },
            onComplete = {
                hideProgressBar(activity_movie_progress_bar)
            })
    }

    private fun getSimilarMovies(movieId: Int) {
        showProgress(activity_movie_progress_bar)
        repository.getApiSimilarMovies(
            movieId,
            onSuccess = {
                if (it != null) {
                    similarMovies = it.listMovies
                    adapter.update(similarMovies)
                }
            }, onFailure = {
                showMessage(getString(R.string.movies_not_found))
            }, onComplete = {
                hideProgressBar(activity_movie_progress_bar)
            }
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
