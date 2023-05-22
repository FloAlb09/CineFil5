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
import fr.epf.projet.cinefil5.model.PopularResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var toolbar: Toolbar
    lateinit var vectorAssetSearch: ImageView
    lateinit var editTextSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("test","après onCreate")

        val movieService = RetrofitInstance.buildMovieService()

        val result = movieService.getMoviePopular()
        Log.i("result..............", result.toString())

        result.enqueue(object : Callback<PopularResult> {
            override fun onResponse(call: Call<PopularResult>, response: Response<PopularResult>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val items = result?.results
                    Log.i("items: ", items.toString())
                    items?.let {
                        binding.moviesRecyclerview.adapter = MoviesAdapter(items)
                        binding.moviesRecyclerview.apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            adapter = binding.moviesRecyclerview.adapter
                        }

                    }
                }
            }

            override fun onFailure(call: Call<PopularResult>, t: Throwable) {
                Log.i("throw---------------Error: ", t.toString())
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        })

        toolbar = findViewById(R.id.toolbar)
        vectorAssetSearch = toolbar.findViewById(R.id.search_vectorasset)
        editTextSearch = toolbar.findViewById(R.id.charsearch_edittext)

        setSupportActionBar(toolbar)

        vectorAssetSearch.setOnClickListener {
            val idMovie = editTextSearch.text.toString()
            if (idMovie.isEmpty()) {
                Toast.makeText(this, "idMovie can't be empty!!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, MovieDetailsActivity::class.java)
                intent.putExtra("id", idMovie.toInt())
                Log.i("idMovie MainActivity", idMovie)
                this.startActivity(intent)
            }
        }
    }
}
