package fr.epf.projet.cinefil5.model

data class Favoris(
    var id: Int,
    val posterPath: String,
    val title: String,
    val originalTitle: String,
    val releaseDate: String,
    val voteAverage: Float,
    val overview: String,
    val liked: Int
)
