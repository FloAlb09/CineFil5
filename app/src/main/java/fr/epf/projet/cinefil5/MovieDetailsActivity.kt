package fr.epf.projet.cinefil5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import fr.epf.projet.cinefil5.api.RetrofitInstance
import fr.epf.projet.cinefil5.databinding.ActivityMovieDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding

    lateinit var recommendedButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieService = RetrofitInstance.buildMovieService()

        val idMovie: Int = intent.getIntExtra("id", 1)

        val result = movieService.getMovieDetails(idMovie)

        result.enqueue(object : Callback<MovieDetailsResult> {
            override fun onResponse(
                call: Call<MovieDetailsResult>, response: Response<MovieDetailsResult>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)

                    binding.textViewBudget.text = formatCurrency.format(result?.budget)

                    val genresArray = result?.genres
                    val sizeGenres: Int? = genresArray?.size
                    var genre: String? = ""
                    var genres: String? = ""
                    for (i in 0..(sizeGenres!! - 1)) {
                        genre = genresArray.get(i).name.toString()
                        genres = genres + ", " + genre
                    }
                    binding.textViewGenre.text = genres

                    binding.textViewOriginalTitle.text = result.originalTitle
                    binding.textViewOverview.text = result.overview
                    binding.textViewReleasedDate.text = result.releaseDate
                    binding.textViewRevenue.text = formatCurrency.format(result.revenue)
                    binding.textViewRuntime.text = "${result.runtime} minutes"
                    binding.textViewStatus.text = result.status
                    binding.textViewTagLine.text = result.tagline
                    binding.textViewTitle.text = result.title
                    val vote_average = result.voteAverage
                    if (vote_average != null) {
                        binding.ratingBarVoteAverage.rating = vote_average.toFloat()
                    }
                    binding.textViewVoteCount.text = result.voteCount.toString()

                    val posterPath = result.posterPath
                    val moviePosterURL = "https://image.tmdb.org/t/p/w500" + posterPath
                    val moviePoster = binding.imageViewPoster
                    Glide.with(moviePoster).load(moviePosterURL).into(moviePoster)
                }
            }

            override fun onFailure(call: Call<MovieDetailsResult>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        })

        recommendedButton = findViewById(R.id.button_recommended)

        recommendedButton.setOnClickListener {
            val intent = Intent(this, MovieRecommendationsActivity::class.java)
            intent.putExtra("id", idMovie)
            this.startActivity(intent)
        }
    }
}
