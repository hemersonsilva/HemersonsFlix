package br.com.hemersonsilva.movies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.hemersonsilva.hemersonsflix.database.dao.MovieDAO
import br.com.hemersonsilva.hemersonsflix.model.Movie

private const val NAME_DATABASE = "movies.db"

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val movieDAO: MovieDAO

    companion object {

        private lateinit var db: AppDatabase

        fun getInstance(context: Context): AppDatabase {

            if (Companion::db.isInitialized) return db

            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                NAME_DATABASE
            ).build()

            return db
        }
    }
}