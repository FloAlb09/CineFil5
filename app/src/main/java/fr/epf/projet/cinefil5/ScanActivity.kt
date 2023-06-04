package fr.epf.projet.cinefil5

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.SurfaceHolder
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.epf.projet.cinefil5.databinding.ActivityScanBinding
import java.io.IOException

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    var intentData = ""

    lateinit var toolbar: Toolbar
    lateinit var vectorAssetSearch: ImageView
    lateinit var editTextSearch: EditText

    private var requestCamera: ActivityResultLauncher<String>? = null

    lateinit var navigationBar: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        editTextSearch.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val keyword = editTextSearch.text
                if (keyword.isEmpty()) {
                    Toast.makeText(this, "The search bar can't be empty!!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val intent = Intent(this, KeywordActivity::class.java)
                    intent.putExtra("keyword", keyword.toString())
                    Log.i("keyword MainActivity", keyword.toString())
                    this.startActivity(intent)
                }
                return@OnKeyListener true
            }
            false
        })

        requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                this.startActivity(Intent(this, ScanActivity::class.java))
            } else {
                Toast.makeText(this, getString(R.string.no_barcode_detected), Toast.LENGTH_SHORT)
                    .show()
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

    private fun initBarcode() {
        barcodeDetector =
            BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()
        cameraSource =
            CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true).build()

        binding.scanSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(p0: SurfaceHolder) {
                try {
                    cameraSource.start(binding.scanSurfaceView.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                cameraSource.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.barcode_stopped),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {
                    binding.scanTextBarcode.post {
                        binding.scanActionButton.text = getString(R.string.btn_search_item)
                        intentData = barcodes.valueAt(0).displayValue
                        binding.scanTextBarcode.text = intentData
                        val intent = Intent(this@ScanActivity, MovieDetailsActivity::class.java)
                        intent.putExtra("id", intentData.toInt())
                        startActivity(intent)
                    }
                }
            }

        })
    }

    override fun onPause() {
        super.onPause()
        cameraSource.release()
    }

    override fun onResume() {
        super.onResume()
        initBarcode()
    }
}