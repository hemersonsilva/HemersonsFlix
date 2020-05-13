package br.com.hemersonsilva.hemersonsflix.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import br.com.hemersonsilva.hemersonsflix.model.Movie

@Dao
interface MovieDAO {

    @Query("SELECT * FROM Movie")
    fun searchAll(): MutableList<Movie>

    @Insert(onConflict = REPLACE)
    fun save(movie: Movie)

    @Query("SELECT * FROM Movie WHERE movieId = :idMovie")
    fun searchById(idMovie: Int): Movie?

    @Delete
    fun remove(movie: Movie)

    @Query("SELECT * FROM Movie WHERE movieId = :idMovie")
    fun searchByPosition(idMovie: Int): Movie?
}
