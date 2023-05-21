package fr.epf.projet.cinefil5

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

//Popular: https://api.themoviedb.org/3/movie/popular?api_key=7940aff05941e29c97a9c866fbad4ab6
//MovieDetailsById: https://api.themoviedb.org/3/movie/2?api_key=7940aff05941e29c97a9c866fbad4ab6
//Poster: https://image.tmdb.org/t/p/w500/nLBRD7UPR6GjmWQp6ASAfCTaWKX.jpg

interface MovieService {
    companion object {
        const val API_KEY = "7940aff05941e29c97a9c866fbad4ab6"
    }

    @GET("movie/popular?api_key=$API_KEY")
    fun getMoviePopular(): Call<String>



    @GET("movie/{idMovie}?api_key=$API_KEY")
    fun getMovieDetails(@Path("idMovie") id: Int): Call<MovieDetailsResult>
}