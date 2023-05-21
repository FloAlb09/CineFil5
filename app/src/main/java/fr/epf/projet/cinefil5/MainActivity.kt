package fr.epf.projet.cinefil5

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var tvResponse: TextView

    lateinit var toolbar: Toolbar
    lateinit var vectorAssetSearch: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResponse = findViewById(R.id.tvResponse)
        toolbar = findViewById(R.id.toolbar)
        vectorAssetSearch = toolbar.findViewById(R.id.search_vectorasset)

        setSupportActionBar(toolbar)

        val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(ScalarsConverterFactory.create()).build()

        val movieDetailsService = retrofit.create(MovieService::class.java)

        val result = movieDetailsService.getMovieDetailsString()

        result.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    tvResponse.text = response.body()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        })

        vectorAssetSearch.setOnClickListener {
            val intent = Intent(this, MovieDetailsActivity::class.java)
            this.startActivity(intent)
        }
    }
}
