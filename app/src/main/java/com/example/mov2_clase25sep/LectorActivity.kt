package com.example.mov2_clase25sep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class LectorActivity : AppCompatActivity() {

    private lateinit var objProducto: Producto

    private lateinit var codigo: EditText
    private lateinit var descripcion: EditText
    private lateinit var nombre: EditText
    private lateinit var precio: EditText

    private lateinit var btnEscanear: Button
    private lateinit var btnCapturar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var btnBuscar: Button

    companion object {
        val productos = Array<Producto?>(10) { null }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lector)

        setSupportActionBar(findViewById(R.id.barraLector))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        objProducto = Producto()

        codigo = findViewById(R.id.edtCodigo)
        descripcion = findViewById(R.id.edtDescripcion)
        nombre = findViewById(R.id.edtNombre)
        precio = findViewById(R.id.edtPrecio)

        btnEscanear = findViewById(R.id.btnEscanear)
        btnCapturar = findViewById(R.id.btnCapturar)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        btnBuscar = findViewById(R.id.btnBuscar)

        btnEscanear.setOnClickListener { escanearCodigo() }
        btnCapturar.setOnClickListener { capturarDatos() }
        btnLimpiar.setOnClickListener { limpiarCampos() }
        btnBuscar.setOnClickListener { buscarDatos() }
    }

    private fun capturarDatos() {
        val code = codigo.text.toString()
        val name = nombre.text.toString()
        val description = descripcion.text.toString()
        val price = precio.text.toString().toDoubleOrNull()


        if(code.isNotEmpty() && name.isNotEmpty()
            && description.isNotEmpty() && price != null){
            var posicion = productos.indexOfFirst { it == null }

            if(posicion != -1){
                productos[posicion] = Producto(code,name,description,price)
                Toast.makeText(this,"Producto agregado",Toast.LENGTH_SHORT).show()
                limpiarCampos()
            }
            else{
                Toast.makeText(this, "No hay espacio disponible", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun escanearCodigo(){
        val intentIntegrator = IntentIntegrator(this@LectorActivity)
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        intentIntegrator.setPrompt("Lector de Codigos")
        intentIntegrator.setCameraId(0)
        intentIntegrator.setBeepEnabled(true)
        intentIntegrator.setBarcodeImageEnabled(true)
        intentIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(intentResult != null){
            if(intentResult.contents == null){
                Toast.makeText(this,"Lectura cancelada", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Codigo leído", Toast.LENGTH_SHORT).show()
                codigo.setText(intentResult.contents)
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun limpiarCampos(){
        codigo.setText("")
        descripcion.setText("")
        nombre.setText("")
        precio.setText("")
    }

    private fun buscarDatos(){
        val code = codigo.text.toString()

        if (code.isNotEmpty()) {
            val productoEncontrado = productos.find { it?.codigo == code }

            if (productoEncontrado != null) {
                nombre.setText(productoEncontrado.nombre)
                descripcion.setText(productoEncontrado.descripcion)
                precio.setText(productoEncontrado.precio.toString())
                Toast.makeText(this, "Producto encontrado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Escanea o Ingresa un código para buscar", Toast.LENGTH_SHORT).show()
        }
    }
    //Regresar al menu
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
}