package fr.epf.projet.cinefil5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator

class ScannerActivity : AppCompatActivity() {

    lateinit var navigationBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val intentIntegrator = IntentIntegrator(this@ScannerActivity)
        intentIntegrator.setPrompt("Scan a Qr code")
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        intentIntegrator.initiateScan()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            val contents = intentResult.contents
            if (contents != null) {
                val intent = Intent(this@ScannerActivity, MovieDetailsActivity::class.java)
                intent.putExtra("id", intentResult.contents.toInt())
                startActivity(intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
