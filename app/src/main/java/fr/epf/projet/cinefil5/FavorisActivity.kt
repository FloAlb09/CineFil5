package fr.epf.projet.cinefil5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.epf.projet.cinefil5.Adapter.FavorisAdapter
import fr.epf.projet.cinefil5.Adapter.MoviesAdapter
import fr.epf.projet.cinefil5.databinding.ActivityFavorisBinding
import fr.epf.projet.cinefil5.db.FavorisDatabase

class FavorisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavorisBinding

    lateinit var db: FavorisDatabase

    lateinit var toolbar: Toolbar
    lateinit var vectorAssetSearch: ImageView
    lateinit var editTextSearch: EditText

    private var requestCamera: ActivityResultLauncher<String>? = null

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