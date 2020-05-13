package br.com.hemersonsilva.hemersonsflix.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Movie(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val movieId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("overview")
    val descreption: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    var favorited: Boolean
)