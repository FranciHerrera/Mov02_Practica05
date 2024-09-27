package com.example.mov2_clase25sep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lector = findViewById<Button>(R.id.btnLector)
        val camara = findViewById<Button>(R.id.btnFoto)
        lector.setOnClickListener { lector() }
        camara.setOnClickListener { camara() }
    }

    private fun lector(){
        val lectorAct = Intent(applicationContext, LectorActivity::class.java)
        startActivity(lectorAct)
    }

    private fun camara(){
        val fotoAct = Intent(applicationContext, FotoActivity::class.java)
        startActivity(fotoAct)
    }
}