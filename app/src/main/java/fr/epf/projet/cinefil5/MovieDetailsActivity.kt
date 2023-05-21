package fr.epf.projet.cinefil5

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import fr.epf.projet.cinefil5.databinding.ActivityMovieDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val movieDetailsService = retrofit.create(MovieService::class.java)

        val result = movieDetailsService.getMovieDetails()

        result.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)

                    binding.textViewBudget.text = formatCurrency.format(result?.get("budget")?.asInt)

                    val genresArray = result?.get("genres")?.asJsonArray
                    val sizeGenres: Int? = genresArray?.size()
                    var genre: String? = ""
                    var genres: String? = ""
                    for (i in 0..(sizeGenres!!-1)){
                        genre = genresArray?.get(i)?.asJsonObject?.get("name")?.asString.toString()
                        genres = genres + ", " + genre
                    }
                    binding.textViewGenre.text = genres

                    binding.textViewOriginalTitle.text = result?.get("original_title")?.asString
                    binding.textViewOverview.text = result?.get("overview")?.asString
                    binding.textViewReleasedDate.text = result?.get("release_date")?.asString
                    binding.textViewRevenue.text = formatCurrency.format(result?.get("revenue")?.asInt)
                    binding.textViewRuntime.text = result?.get("runtime")?.asString + " minutes"
                    binding.textViewStatus.text = result?.get("status")?.asString
                    binding.textViewTagLine.text = result?.get("tagline")?.asString
                    binding.textViewTitle.text = result?.get("title")?.asString
//                    val vote_average = result?.get("vote-average")?.asFloat
//                    if (vote_average != null) {
//                        binding.ratingBarVoteAverage.rating = vote_average
//                    }
//                    binding.textViewVoteCount.text = result?.get("vote_count")?.asString

                    val posterPath = result?.get("poster_path")?.asString
                    val moviePosterURL = "https://image.tmdb.org/t/p/w500" + posterPath
                    val moviePoster = binding.imageViewPoster
                    Glide.with(moviePoster)
                        .load(moviePosterURL)
                        .into(moviePoster)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        })
    }
}