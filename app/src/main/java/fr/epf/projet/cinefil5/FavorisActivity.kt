package fr.epf.projet.cinefil5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.epf.projet.cinefil5.Adapter.FavorisAdapter
import fr.epf.projet.cinefil5.Adapter.MoviesAdapter
import fr.epf.projet.cinefil5.databinding.ActivityFavorisBinding
import fr.epf.projet.cinefil5.db.FavorisDatabase

class FavorisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavorisBinding

    lateinit var db: FavorisDatabase

    lateinit var navigationBar : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavorisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FavorisDatabase(this)

        val favoris = db.findAll()

        if (favoris != null) {
            val items = favoris
            items.let {
                var favorisAdapter = FavorisAdapter(items)
                binding.favorisMoviesRecyclerview.adapter = favorisAdapter

                favorisAdapter.setOnItemClickListener(object : FavorisAdapter.onItemClickListener {
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
                    this.startActivity(Intent(this, ScannerActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
            true
        }

    }
}