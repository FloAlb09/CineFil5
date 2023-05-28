package fr.epf.projet.cinefil5

import com.google.gson.annotations.SerializedName

class MovieDetailsResult (
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    val title: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("vote_count")
    val voteCount: Int,
    val genres: Array<Genre>,
    @SerializedName("release_date")
    val releaseDate: String,
    val status: String,
    val runtime: Int,
    val budget: Int,
    val revenue: Int,
    val overview: String
)

data class Genre(
    val id: Int,
    val name: String
)

