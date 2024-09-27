package com.example.mov2_clase25sep

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class FotoActivity : AppCompatActivity() {

    private lateinit var foto: ImageView
    private lateinit var btnTomar: Button
    private lateinit var btnGuardar: Button
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foto)

        setSupportActionBar(findViewById(R.id.barraFoto))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        foto = findViewById(R.id.imgFoto)
        btnTomar = findViewById(R.id.btnTomar)
        btnGuardar = findViewById(R.id.btnGuardar)

        btnTomar.setOnClickListener {
            if (checkPermissions()) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                responseLauncher.launch(intent)
            } else {
                requestPermissions()
            }
        }

        btnGuardar.setOnClickListener {
            if (imageBitmap != null) {
                guardarFoto(imageBitmap!!)
            } else {
                Toast.makeText(this, "No hay foto para guardar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            Toast.makeText(this, "Foto tomada", Toast.LENGTH_SHORT).show()
            val extras = activityResult.data!!.extras
            imageBitmap = extras!!["data"] as Bitmap?
            foto.setImageBitmap(imageBitmap)
        } else {
            Toast.makeText(this, "Foto no tomada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarFoto(bitmap: Bitmap) {
        val resolver = contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "JPEG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp") // Carpeta personalizada
        }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        uri?.let {
            try {
                val outputStream = resolver.openOutputStream(uri)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                outputStream?.close()
                Toast.makeText(this, "Foto guardada en la carpeta MyApp", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error al guardar la foto", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "Error al crear el archivo de imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        return cameraPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                responseLauncher.launch(intent)
            } else {
                Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}
