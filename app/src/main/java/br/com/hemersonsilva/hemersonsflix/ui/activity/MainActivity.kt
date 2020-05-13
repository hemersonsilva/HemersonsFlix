package br.com.hemersonsilva.hemersonsflix.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.hemersonsilva.hemersonsflix.R
import br.com.hemersonsilva.hemersonsflix.constants.MOVIE_KEY_ID
import br.com.hemersonsilva.hemersonsflix.extensions.showMessage
import br.com.hemersonsilva.hemersonsflix.model.Movie
import br.com.hemersonsilva.hemersonsflix.ui.recyclerview.adapter.MovieAdapter
import br.com.hemersonsilva.movies.database.AppDatabase
import br.com.hemersonsilva.hemersonsflix.extensions.hideProgressBar
import br.com.hemersonsilva.hemersonsflix.extensions.showProgress
import br.com.hemersonsilva.hemersonsflix.repository.MovieRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val repository by lazy {
        MovieRepository(AppDatabase.getInstance(this).movieDAO)
    }

    private val adapter by lazy {
        MovieAdapter(
            context = this
        ) {
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra(MOVIE_KEY_ID, it.movieId)
            startActivity(intent)
        }
    }

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var movies: MutableList<Movie>
    private var pageNumber = 1
    private var isScrolling = true
    private var pastVisiblesItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(activity_main_toolbar)
        configFab()
        configRecyclerView()
        getMovies()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_search -> {
                showMessage(getString(R.string.in_development))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configRecyclerView() {
        activity_main_recycler_view.adapter = adapter
        gridLayoutManager = GridLayoutManager(this, 2)
        activity_main_recycler_view.layoutManager = gridLayoutManager
    }

    private fun configFab() {
        activity_main_fab.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }
    }

    private fun getMovies() {
        showProgress(activity_main_progress_bar)
        repository.getMovies(
            page = pageNumber,
            onSuccess = {
                if (it != null) {
                    movies = it.listMovies
                    adapter.update(movies)
                    initScrollListener()
                }
            }, onFailure = {
                showMessage(getString(R.string.failure_to_load_movies))
            },
            onComplete = {
                hideProgressBar(activity_main_progress_bar)
            })
    }

    private fun loadMore() {
        showProgress(activity_main_progress_bar)
        repository.getMovies(
            page = pageNumber,
            onSuccess = {
                val listAux = it?.listMovies
                if (listAux != null) {
                    adapter.addListItem(listAux)
                    isScrolling = true
                }
            }, onFailure = {
                showMessage(getString(R.string.failure_to_load_movies))
            },
            onComplete = {
                hideProgressBar(activity_main_progress_bar)
            })
    }

    private fun initScrollListener() {
        activity_main_recycler_view.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = gridLayoutManager.childCount
                    totalItemCount = gridLayoutManager.itemCount
                    pastVisiblesItems =
                        gridLayoutManager.findFirstCompletelyVisibleItemPosition()

                    if (isScrolling) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            isScrolling = false
                            pageNumber++
                            loadMore()
                        }
                    }
                }
            }
        })
    }
}

