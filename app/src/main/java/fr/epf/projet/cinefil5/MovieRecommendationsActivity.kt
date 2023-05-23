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
import fr.epf.projet.cinefil5.api.RetrofitInstance
import fr.epf.projet.cinefil5.databinding.ActivityMainBinding
import fr.epf.projet.cinefil5.model.ServiceResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRecommendationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var toolbar: Toolbar
    lateinit var vectorAssetSearch: ImageView
    lateinit var editTextSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieService = RetrofitInstance.buildMovieService()

        val idMovie: Int = intent.getIntExtra("id", 1)

        val result = movieService.getMovieRecommendations(idMovie)

        result.enqueue(object : Callback<ServiceResult> {
            override fun onResponse(call: Call<ServiceResult>, response: Response<ServiceResult>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val items = result?.results
                    items?.let {
                        var moviesAdapter = MoviesAdapter(items)
                        binding.moviesRecyclerview.adapter = moviesAdapter
                        moviesAdapter.setOnItemClickListener(object : MoviesAdapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
//                                Toast.makeText(this@MainActivity, "You clicked on item no. $position", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@MovieRecommendationsActivity, MovieDetailsActivity::class.java)
                                intent.putExtra("id", items[position].id)
                                startActivity(intent)
                            }

                        })
                        binding.moviesRecyclerview.apply {
                            layoutManager = LinearLayoutManager(this@MovieRecommendationsActivity)
                            adapter = binding.moviesRecyclerview.adapter
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ServiceResult>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        })

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