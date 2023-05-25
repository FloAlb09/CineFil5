package fr.epf.projet.cinefil5.api

import fr.epf.projet.cinefil5.MovieDetailsResult
import fr.epf.projet.cinefil5.model.ServiceResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Popular: https://api.themoviedb.org/3/movie/popular?api_key=7940aff05941e29c97a9c866fbad4ab6
//MovieDetailsById: https://api.themoviedb.org/3/movie/2?api_key=7940aff05941e29c97a9c866fbad4ab6
//Poster: https://image.tmdb.org/t/p/w500/nLBRD7UPR6GjmWQp6ASAfCTaWKX.jpg
//Recommendations: https://api.themoviedb.org/3/movie/{movie_id}/recommendations?api_key=7940aff05941e29c97a9c866fbad4ab6
//Trending-week: https://api.themoviedb.org/3/trending/movie/week?api_key=7940aff05941e29c97a9c866fbad4ab6
//Keyword: https://api.themoviedb.org/3/search/movie?api_key=7940aff05941e29c97a9c866fbad4ab6&query={keyword}

interface MovieService {
    companion object {
        const val API_KEY = "7940aff05941e29c97a9c866fbad4ab6"
    }

    @GET("movie/popular?api_key=$API_KEY")
    fun getMoviePopular(): Call<ServiceResult>

    @GET("trending/movie/week?api_key=$API_KEY")
    fun getMovieTrending(): Call<ServiceResult>

    @GET("movie/{idMovie}?api_key=$API_KEY")
    fun getMovieDetails(@Path("idMovie") id: Int): Call<MovieDetailsResult>

    @GET("movie/{idMovie}/recommendations?api_key=$API_KEY")
    fun getMovieRecommendations(@Path("idMovie") id: Int): Call<ServiceResult>

    @GET("search/movie?api_key=$API_KEY&")
    fun getKeyword(@Query("query") keyword: String): Call<ServiceResult>


}