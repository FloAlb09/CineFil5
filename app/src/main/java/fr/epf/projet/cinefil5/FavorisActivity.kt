package fr.epf.projet.cinefil5

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.projet.cinefil5.Adapter.MoviesAdapter
import fr.epf.projet.cinefil5.databinding.ActivityFavorisBinding
import fr.epf.projet.cinefil5.db.FavorisDatabase

class FavorisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavorisBinding

    lateinit var db: FavorisDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavorisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FavorisDatabase(this)

        val favoris = db.findAll()

        if (favoris != null) {

            val items = favoris
            items.let {
                var moviesAdapter = MoviesAdapter(items)
                binding.favorisMoviesRecyclerview.adapter = moviesAdapter
                moviesAdapter.setOnItemClickListener(object : MoviesAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(this@FavorisActivity, MovieDetailsActivity::class.java)
                        intent.putExtra("id", items[position].id)
                        startActivity(intent)
                    }

                })
                binding.favorisMoviesRecyclerview.apply {
                    layoutManager = LinearLayoutManager(this@FavorisActivity)
                    adapter = binding.favorisMoviesRecyclerview.adapter
                }

            }
        }

    }
}