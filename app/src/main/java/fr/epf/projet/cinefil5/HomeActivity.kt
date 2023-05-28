package fr.epf.projet.cinefil5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import fr.epf.projet.cinefil5.Adapter.MoviesAdapter
import fr.epf.projet.cinefil5.api.RetrofitInstance
import fr.epf.projet.cinefil5.databinding.ActivityHomeBinding
import fr.epf.projet.cinefil5.model.ServiceResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    lateinit var toolbar: Toolbar
    lateinit var vectorAssetSearch: ImageView
    lateinit var editTextSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieService = RetrofitInstance.buildMovieService()

        val resultPopular = movieService.getMoviePopular()
        resultPopular.enqueue(object : Callback<ServiceResult> {
            override fun onResponse(call: Call<ServiceResult>, response: Response<ServiceResult>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val items = result?.results
                    items?.let {
                        var moviesAdapter = MoviesAdapter(items)
                        binding.popularMoviesRecyclerview.adapter = moviesAdapter
                        moviesAdapter.setOnItemClickListener(object : MoviesAdapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
//                                Toast.makeText(this@MainActivity, "You clicked on item no. $position", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@HomeActivity, MovieDetailsActivity::class.java)
                                intent.putExtra("id", items[position].id)
                                startActivity(intent)
                            }

                        })
                        binding.popularMoviesRecyclerview.apply {
                            layoutManager = LinearLayoutManager(this@HomeActivity,HORIZONTAL,false)
                            adapter = binding.popularMoviesRecyclerview.adapter
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ServiceResult>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        })

        val resultTrendingWeek = movieService.getMovieTrending()
        resultTrendingWeek.enqueue(object : Callback<ServiceResult> {
            override fun onResponse(call: Call<ServiceResult>, response: Response<ServiceResult>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val items = result?.results
                    items?.let {
                        var moviesAdapter = MoviesAdapter(items)
                        binding.trendingWeekMoviesRecyclerview.adapter = moviesAdapter
                        moviesAdapter.setOnItemClickListener(object : MoviesAdapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
//                                Toast.makeText(this@MainActivity, "You clicked on item no. $position", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@HomeActivity, MovieDetailsActivity::class.java)
                                intent.putExtra("id", items[position].id)
                                startActivity(intent)
                            }

                        })
                        binding.trendingWeekMoviesRecyclerview.apply {
                            layoutManager = LinearLayoutManager(this@HomeActivity,HORIZONTAL,false)
                            adapter = binding.trendingWeekMoviesRecyclerview.adapter
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ServiceResult>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        })

        binding.activityHomeButtonSacnner.setOnClickListener{
            val intent = Intent(this, ScannerActivity::class.java)
            this.startActivity(intent)
        }

        toolbar = findViewById(R.id.toolbar)
        vectorAssetSearch = toolbar.findViewById(R.id.search_vectorasset)
        editTextSearch = toolbar.findViewById(R.id.charsearch_edittext)

        setSupportActionBar(toolbar)

        vectorAssetSearch.setOnClickListener {
            val keyword = editTextSearch.text
            if (keyword.isEmpty()) {
                Toast.makeText(this, "The search bar can't be empty!!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, KeywordActivity::class.java)
                intent.putExtra("keyword", keyword.toString())
                Log.i("keyword MainActivity", keyword.toString())
                this.startActivity(intent)
            }
        }
    }
}
