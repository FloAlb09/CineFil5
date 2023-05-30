package fr.epf.projet.cinefil5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.epf.projet.cinefil5.api.RetrofitInstance
import fr.epf.projet.cinefil5.databinding.ActivityMovieDetailsBinding
import fr.epf.projet.cinefil5.db.FavorisDatabase
import fr.epf.projet.cinefil5.model.Favoris
import fr.epf.projet.cinefil5.model.ServiceDetailsResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding

    lateinit var db: FavorisDatabase
    lateinit var posterPathDb: String
    lateinit var titleDb: String
    lateinit var originalTitleDb: String
    lateinit var releaseDateDb: String
    var voteAverageDb: Float = 0.0f
    lateinit var overviewDb: String
    var likedDb: Int = 0

    private var requestCamera: ActivityResultLauncher<String>? = null

    lateinit var navigationBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FavorisDatabase(this)

        val movieService = RetrofitInstance.buildMovieService()

        val idMovie: Int = intent.getIntExtra("id", 1)

        Log.i("--movie_id-------------------------------",idMovie.toString())

        val result = movieService.getMovieDetails(idMovie)

        result.enqueue(object : Callback<MovieDetailsResult> {
            override fun onResponse(
                call: Call<MovieDetailsResult>, response: Response<MovieDetailsResult>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)

                    val vote_average = result?.voteAverage
                    val genresArray = result?.genres
                    val sizeGenres: Int? = genresArray?.size
                    var genre: String? = ""
                    var genres: String? = ""
                    for (i in 0..(sizeGenres!! - 1)) {
                        genre = genresArray.get(i).name
                        genres = genres + ", " + genre
                    }


                    val posterPath = result.posterPath
                    val moviePosterURL = "https://image.tmdb.org/t/p/w500" + posterPath
                    val moviePoster = binding.movieDetailsPoster
                    Glide.with(moviePoster).load(moviePosterURL).into(moviePoster)
                    binding.movieDetailsTitle.text = result.title
                    binding.movieDetailsOriginalTitle.text = result.originalTitle
                    if (vote_average != null) {
                        binding.movieDetailsVoteAverage.rating = vote_average.toFloat()
                    }
                    binding.movieDetailsVoteCount.text = result.voteCount.toString()
                    binding.movieDetailsGenres.text = genres
                    binding.movieDetailsReleaseDate.text = result.releaseDate
                    binding.movieDetailsStatus.text = result.status
                    binding.movieDetailsRuntime.text = "${result.runtime} minutes"
                    binding.movieDetailsBudget.text = formatCurrency.format(result.budget)
                    binding.movieDetailsRevenue.text = formatCurrency.format(result.revenue)
                    binding.movieDetailsOverview.text = result.overview

                    var favorisDB = db.findFavoris(idMovie)

                    if (favorisDB?.id == null) {
                        binding.movieDetailsFavoris.setImageResource(R.drawable.ic_unstar)
                    } else {
                        binding.movieDetailsFavoris.setImageResource(R.drawable.ic_star)
                    }


                    posterPathDb = posterPath
                    titleDb = binding.movieDetailsTitle.text.toString()
                    originalTitleDb = binding.movieDetailsOriginalTitle.text.toString()
                    releaseDateDb = binding.movieDetailsReleaseDate.text.toString()
                    overviewDb = binding.movieDetailsOverview.text.toString()
                    if (vote_average != null) {
                        voteAverageDb = vote_average.toFloat()
                    }
                }
            }

            override fun onFailure(call: Call<MovieDetailsResult>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        })

        binding.movieDetailsRecommendations.setOnClickListener {
            val intent = Intent(this, MovieRecommendationsActivity::class.java)
            intent.putExtra("id", idMovie)
            intent.putExtra("movieTitle", titleDb)
            this.startActivity(intent)
        }

        binding.movieDetailsFavoris.setOnClickListener {
            var favorisDB = db.findFavoris(idMovie)

            if (favorisDB?.id != null) {
                val favorisDelete = db.deleteFavoris(idMovie)
                if (favorisDelete){
                    Toast.makeText(this,getString(R.string.delete_favoris_success), Toast.LENGTH_SHORT).show()
                    binding.movieDetailsFavoris.setImageResource(R.drawable.ic_unstar)
                } else{
                    Toast.makeText(this,getString(R.string.delete_favoris_erreur), Toast.LENGTH_SHORT).show()
                }
            } else {
                val favoris = Favoris(
                    idMovie,
                    posterPathDb,
                    titleDb,
                    originalTitleDb,
                    releaseDateDb,
                    voteAverageDb,
                    overviewDb,
                    likedDb
                )
                val isInserted = db.addFavoris(favoris)

                if (isInserted) {
                    Toast.makeText(
                        this, getString(R.string.success_favoris), Toast.LENGTH_SHORT
                    ).show()
                    binding.movieDetailsFavoris.setImageResource(R.drawable.ic_star)
                }
            }

        }


        requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission(),){
            if (it){
                this.startActivity(Intent(this, ScanActivity::class.java))
            }else {
                Toast.makeText(this, getString(R.string.no_barcode_detected), Toast.LENGTH_SHORT).show()
            }
        }

        navigationBar = findViewById(R.id.navigation_bar)
        navigationBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_page -> {
                    this.startActivity(Intent(this, HomeActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.collection_page -> {
                    this.startActivity(Intent(this, FavorisActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.scan -> {
                    requestCamera?.launch((android.Manifest.permission.CAMERA))
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
            true
        }
    }
}
