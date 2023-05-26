package fr.epf.projet.cinefil5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import fr.epf.projet.cinefil5.databinding.ActivityScannerBinding

class ScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.activityScannerButton.setOnClickListener(View.OnClickListener {
            val intentIntegrator = IntentIntegrator(this@ScannerActivity)
            intentIntegrator.setOrientationLocked(true)
            intentIntegrator.setPrompt("Scan a Qr code")
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            intentIntegrator.initiateScan()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            val contents = intentResult.contents
            if (contents != null) {
                binding.activityScannerText!!.text = intentResult.contents
                val intent = Intent(this@ScannerActivity, MovieDetailsActivity::class.java)
                intent.putExtra("id",intentResult.contents.toInt() )
                val result = intentResult.contents
                Log.i("id", result)
                startActivity(intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
