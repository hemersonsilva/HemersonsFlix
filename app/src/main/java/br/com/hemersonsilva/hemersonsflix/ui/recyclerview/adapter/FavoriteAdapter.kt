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
import kotlinx.android.synthetic.main.favorite_item.view.*

class FavoriteAdapter(
    private val context: Context,
    val movies: MutableList<Movie> = mutableListOf(),
    var onClickListener: (movie: Movie) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val newView = LayoutInflater.from(context).inflate(
            R.layout.favorite_item,
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

    fun remove(positions: Int) {
        movies.removeAt(positions)
        notifyItemRemoved(positions)
        notifyItemRangeRemoved(positions, movies.size);
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView){

        private lateinit var movie: Movie

        init {
            itemView.setOnClickListener {
                if (::movie.isInitialized) {
                    onClickListener(movie)
                }
            }
        }

        fun binds(movie: Movie) {
            this.movie = movie
            itemView.my_list_text_view_movie_title.text = movie.title
            itemView.my_list_text_view_movie_nota.text = movie.voteAverage.toString()
            itemView.my_list_text_view_desc.text = movie.descreption
            Picasso.get().load(BASE_URL_POSTER_PATH + movie.posterPath)
                .into(itemView.my_list_image_view_cover);
        }
    }
}
