package br.com.hemersonsilva.hemersonsflix.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.hemersonsilva.hemersonsflix.R
import br.com.hemersonsilva.hemersonsflix.constants.BASE_URL_POSTER_PATH
import br.com.hemersonsilva.hemersonsflix.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item.view.*

class SearchAdapter(
    private val context: Context,
    private val movies: MutableList<Movie> = mutableListOf(),
    var clickItem: (movie: Movie) -> Unit = {}
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val newView = LayoutInflater.from(context).inflate(
                R.layout.movie_item,
                parent, false
            )
        return ViewHolder(newView)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.binds(movie)
    }

    fun update(movies: MutableList<Movie>) {
        notifyItemRangeRemoved(0, this.movies.size)
        this.movies.clear()
        this.movies.addAll(movies)
        notifyItemRangeInserted(0, this.movies.size)
    }

    fun addListItem(listMovies: List<Movie?>) {
        for (movie in listMovies) {
            movies.add(movie!!)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private lateinit var movie: Movie

        init {
            itemView.setOnClickListener {
                if (::movie.isInitialized) {
                    clickItem(movie)
                }
            }
        }

        fun binds(movie: Movie) {
            this.movie = movie
            Picasso.get().load(BASE_URL_POSTER_PATH + movie.posterPath)
                .into(itemView.movie_item_poster_path)
        }
    }
}
