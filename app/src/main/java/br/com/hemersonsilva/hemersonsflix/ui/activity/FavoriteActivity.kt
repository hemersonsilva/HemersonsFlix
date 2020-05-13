package br.com.hemersonsilva.hemersonsflix.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.hemersonsilva.hemersonsflix.R
import br.com.hemersonsilva.hemersonsflix.constants.MOVIE_KEY_ID
import br.com.hemersonsilva.hemersonsflix.extensions.showMessage
import br.com.hemersonsilva.hemersonsflix.model.Movie
import br.com.hemersonsilva.hemersonsflix.ui.recyclerview.adapter.FavoriteAdapter
import br.com.hemersonsilva.movies.database.AppDatabase
import br.com.hemersonsilva.hemersonsflix.repository.MovieRepository
import kotlinx.android.synthetic.main.activity_favorite.*
import java.util.*

class FavoriteActivity : AppCompatActivity() {

    private val repository by lazy {
        MovieRepository(AppDatabase.getInstance(this).movieDAO)
    }

    private val adapter by lazy {
        FavoriteAdapter(
            context = this
        ) {
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra(MOVIE_KEY_ID, it.movieId)
            startActivity(intent)
            finish()
        }
    }

    private var movies: MutableList<Movie> = ArrayList()
    private var displayList: MutableList<Movie> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        configToolBar()
        configRecyclerView()
        getInternalMovies()
        configOnSwipedRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        configSearchView(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun configToolBar() {
        setSupportActionBar(activity_favorite_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        supportActionBar?.title = getString(R.string.my_list)
    }

    private fun configRecyclerView() {
        activity_favorite_recycler_view.adapter = adapter
        activity_favorite_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun configSearchView(menu: Menu?) {
        val manage = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.favorite_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(manage.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                displayList.clear()
                if (newText!!.isNotEmpty()) {
                    val search = newText.toLowerCase(Locale.getDefault())
                    adapter.movies.forEach {
                        if (it.title.toLowerCase(Locale.getDefault()).contains(search)) {
                            displayList.add(it)
                        }
                    }

                } else {
                    displayList.addAll(movies)
                }
                adapter.update(displayList)
                return true
            }
        })
    }

    private fun configOnSwipedRecyclerView() {
        val simpleItemTouchCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    removeMovie(viewHolder.adapterPosition)
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(activity_favorite_recycler_view)
    }

    private fun removeMovie(position: Int) {
        val movie = adapter.movies[position]
        repository.internalRemove(movie) {
            adapter.remove(position)
            refreshListMovies()
            showMessage(getString(R.string.movie_removed_from_list))
            isEmpty(adapter.itemCount)
        }
    }

    private fun refreshListMovies() {
        repository.internalSearch {
            movies = it
        }
    }

    private fun getInternalMovies() {
        repository.internalSearch {
            movies = it
            updateRecyclerView()
            isEmpty(adapter.itemCount)
        }
    }

    private fun updateRecyclerView() {
        displayList.addAll(movies)
        adapter.update(displayList)
    }

    fun isEmpty(count: Int) {
        if (count == 0)
            activity_favorite_frame_layout.visibility = View.VISIBLE
        else
            activity_favorite_frame_layout.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
