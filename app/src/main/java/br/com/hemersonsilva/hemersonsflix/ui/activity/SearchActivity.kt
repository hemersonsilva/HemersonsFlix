package br.com.hemersonsilva.hemersonsflix.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import br.com.hemersonsilva.hemersonsflix.R
import br.com.hemersonsilva.hemersonsflix.constants.MOVIE_KEY_ID
import br.com.hemersonsilva.hemersonsflix.extensions.hideProgressBar
import br.com.hemersonsilva.hemersonsflix.extensions.showMessage
import br.com.hemersonsilva.hemersonsflix.extensions.showProgress
import br.com.hemersonsilva.hemersonsflix.model.Movie
import br.com.hemersonsilva.hemersonsflix.model.MovieResult
import br.com.hemersonsilva.hemersonsflix.repository.MovieRepository
import br.com.hemersonsilva.hemersonsflix.ui.recyclerview.adapter.MovieAdapter
import br.com.hemersonsilva.movies.database.AppDatabase
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity() {

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        configToolBar()
        configRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        configSearchView(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun configSearchView(menu: Menu?) {
        val manage = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.favorite_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(manage.getSearchableInfo(componentName))

        searchView.queryHint = "Buscar"
        searchView.onActionViewExpanded()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                hideKeybaord(searchView)
                searchMovies(query)
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false

            }
        })
    }

    private fun searchMovies(query: String?) {
        if (query != null) {
            showProgress(activity_search_progress_bar)
            activity_search_linear_layout_search.visibility = View.GONE
            repository.searchMovies(
                query = query,
                onSuccess = {
                    if (it != null) {
                        if (noResultSearch(it)) {
                            setViewsNoResultSearch()
                        } else {
                            updateRecyclerView(it)
                        }
                    }

                }, onFailure = {
                    showMessage(getString(R.string.failure_to_load_movies))
                },
                onComplete = {
                    hideProgressBar(activity_search_progress_bar)
                })
        }
    }

    private fun noResultSearch(it: MovieResult) =
        it.listMovies.size == 0

    private fun setViewsNoResultSearch() {
        activity_search_image_view.setImageResource(R.drawable.ic_vader_search_not_found)
        activity_search_text_view.text = getString(R.string.no_results_found)
        activity_search_recycler_view.visibility = View.GONE
        activity_search_linear_layout_search.visibility = View.VISIBLE
    }

    private fun updateRecyclerView(it: MovieResult) {
        movies = it.listMovies
        adapter.update(movies)
        activity_search_recycler_view.visibility = View.VISIBLE
    }


    private fun configToolBar() {
        setSupportActionBar(activity_search_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        supportActionBar?.title = null
    }

    private fun configRecyclerView() {
        activity_search_recycler_view.adapter = adapter
        gridLayoutManager = GridLayoutManager(this, 2)
        activity_search_recycler_view.layoutManager = gridLayoutManager
    }

    private fun hideKeybaord(v: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
