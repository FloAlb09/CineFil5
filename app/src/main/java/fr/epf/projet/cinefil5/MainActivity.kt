package fr.epf.projet.cinefil5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import fr.epf.projet.cinefil5.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var toolbar: Toolbar
    lateinit var vectorAssetSearch: ImageView
    lateinit var editTextSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        vectorAssetSearch = toolbar.findViewById(R.id.search_vectorasset)
        editTextSearch = toolbar.findViewById(R.id.charsearch_edittext)

        setSupportActionBar(toolbar)

        val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(ScalarsConverterFactory.create()).build()

        val movieDetailsService = retrofit.create(MovieService::class.java)

        val result = movieDetailsService.getMoviePopular()

        result.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    binding.tvResponse.text = response.body()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        })

        vectorAssetSearch.setOnClickListener {
            val idMovie = editTextSearch.text.toString()
            if (idMovie.isEmpty()){
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
