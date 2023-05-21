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

        result.enqueue(object : Callback<MovieDetailsResult> {
            override fun onResponse(call: Call<MovieDetailsResult>, response: Response<MovieDetailsResult>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)

                    binding.textViewBudget.text = formatCurrency.format(result?.budget)

                    val genresArray = result?.genres
                    val sizeGenres: Int? = genresArray?.size
                    var genre: String? = ""
                    var genres: String? = ""
                    for (i in 0..(sizeGenres!!-1)){
                        genre = genresArray?.get(i)?.name.toString()
                        genres = genres + ", " + genre
                    }
                    binding.textViewGenre.text = genres

                    binding.textViewOriginalTitle.text = result?.originalTitle
                    binding.textViewOverview.text = result?.overview
                    binding.textViewReleasedDate.text = result?.releaseDate
                    binding.textViewRevenue.text = formatCurrency.format(result?.revenue)
                    binding.textViewRuntime.text = "${result?.runtime} minutes"
                    binding.textViewStatus.text = result?.status
                    binding.textViewTagLine.text = result?.tagline
                    binding.textViewTitle.text = result?.title
                    val vote_average = result?.voteAverage
                    if (vote_average != null) {
                        binding.ratingBarVoteAverage.rating = vote_average.toFloat()
                    }
                    binding.textViewVoteCount.text = result?.voteCount.toString()

                    val posterPath = result?.posterPath
                    val moviePosterURL = "https://image.tmdb.org/t/p/w500" + posterPath
                    val moviePoster = binding.imageViewPoster
                    Glide.with(moviePoster)
                        .load(moviePosterURL)
                        .into(moviePoster)
                }
            }

            override fun onFailure(call: Call<MovieDetailsResult>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        })
    }
}