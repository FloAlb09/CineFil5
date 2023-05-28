package fr.epf.projet.cinefil5.model

import com.google.gson.annotations.SerializedName

data class ServiceResult(
    val page: Int,
    val results: List<ServiceDetailsResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

data class ServiceDetailsResult(
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    val title: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val voteAverage: Float,
    val overview: String
)