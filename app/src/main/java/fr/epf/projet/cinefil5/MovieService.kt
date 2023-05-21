package fr.epf.projet.cinefil5

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

//https://api.themoviedb.org/3/movie/popular?api_key=7940aff05941e29c97a9c866fbad4ab6
//https://api.themoviedb.org/3/movie/2?api_key=7940aff05941e29c97a9c866fbad4ab6
//https://image.tmdb.org/t/p/w500/nLBRD7UPR6GjmWQp6ASAfCTaWKX.jpg

interface MovieService {
    @GET("movie/2?api_key=7940aff05941e29c97a9c866fbad4ab6")
    fun getMovieDetailsString(): Call<String>

    @GET("movie/2?api_key=7940aff05941e29c97a9c866fbad4ab6")
    fun getMovieDetails(): Call<MovieDetailsResult>
}