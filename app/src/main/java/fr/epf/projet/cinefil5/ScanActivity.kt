package fr.epf.projet.cinefil5

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import fr.epf.projet.cinefil5.databinding.ActivityScanBinding
import java.io.IOException

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    var intentData = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        barcodeDetector.setProcessor(object: Detector.Processor<Barcode>{
            override fun release() {
                Toast.makeText(applicationContext, getString(R.string.barcode_stopped), Toast.LENGTH_SHORT).show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes =detections.detectedItems
                if (barcodes.size()!=0){
                    binding.scanTextBarcode!!.post{
                        binding.scanActionButton!!.text = getString(R.string.btn_search_item)
                        intentData = barcodes.valueAt(0).displayValue
                        binding.scanTextBarcode.setText(intentData)
                        val intent = Intent(this@ScanActivity, MovieDetailsActivity::class.java)
                        intent.putExtra("id", intentData.toInt())
                        startActivity(intent)
                        //finish()
                    }
                }
            }

        })
    }
    override fun onPause(){
        super.onPause()
        cameraSource!!.release()
    }

    override fun onResume() {
        super.onResume()
        initBarcode()
    }
}