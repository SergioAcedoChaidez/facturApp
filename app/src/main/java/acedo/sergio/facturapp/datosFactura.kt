package acedo.sergio.facturapp

import acedo.sergio.facturapp.ObjetoNegocio.Articulo
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore

class   datosFactura : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var et_Descuentos: EditText
    private lateinit var et_Envios: EditText
    private lateinit var et_Impuesto: EditText
    private lateinit var et_Pagado: EditText
    private lateinit var btnGuardardatosFactura: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_factura)
        initView()
        actualizartextos()

        btnGuardardatosFactura.setOnClickListener {
            guardarDatos()

        }
    }

    private fun actualizartextos() {

            et_Descuentos.setText(crearFactura.FacturaTrabajandose.descuentos.toString())
            et_Envios.setText(crearFactura.FacturaTrabajandose.envios.toString())
            et_Impuesto.setText(crearFactura.FacturaTrabajandose.impuesto.toString())
            et_Pagado.setText(crearFactura.FacturaTrabajandose.pagado.toString())

    }

    private fun guardarDatos() {
        if(et_Pagado.text.toString().toDouble() > crearFactura.FacturaTrabajandose.totalaPagar){
            Toast.makeText(this,"No puede ingresar más del total", Toast.LENGTH_SHORT).show()
            return
        }else {


            crearFactura.FacturaTrabajandose.descuentos = et_Descuentos.text.toString().toDouble()
            crearFactura.FacturaTrabajandose.envios = et_Envios.text.toString().toDouble()
            crearFactura.FacturaTrabajandose.impuesto = et_Impuesto.text.toString().toDouble()
            crearFactura.FacturaTrabajandose.pagado = et_Pagado.text.toString().toDouble()

            Toast.makeText(this, "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show()
            if (!crearFactura.FacturaTrabajandose.facturaid.equals("")) {


                crearFactura.FacturaTrabajandose.saldoAdeudado = crearFactura.FacturaTrabajandose.totalaPagar -
                        crearFactura.FacturaTrabajandose.descuentos + crearFactura.FacturaTrabajandose.envios
                +crearFactura.FacturaTrabajandose.impuesto - crearFactura.FacturaTrabajandose.pagado



                val db: FirebaseFirestore = FirebaseFirestore.getInstance()
               db.collection("Factura")
                    .document(crearFactura.FacturaTrabajandose.facturaid).set(crearFactura.FacturaTrabajandose)

                crearFactura.Articulos = ArrayList()
            }
            var intent = Intent(this, crearFactura::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initView() {
        toolbar = findViewById(R.id.btn_CrearFacturaAppBar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        et_Descuentos = findViewById(R.id.et_Descuentos)
        et_Envios=findViewById(R.id.et_Envios)
        et_Impuesto= findViewById(R.id.et_Impuesto)
        et_Pagado= findViewById(R.id.et_Pagado)
        btnGuardardatosFactura= findViewById(R.id.btnGuardardatosFactura)
    }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {
        atras()


        return true
    }

    override fun onBackPressed() {
        atras()
    }

    private fun atras() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Seguro que desea salir sin guardar los cambios a los datos de factura?")
        builder.setCancelable(true)
        builder.setPositiveButton("Si") { dialog, _ ->
            if(!crearFactura.FacturaTrabajandose.facturaid.equals("")){
                crearFactura.Articulos = ArrayList()
            }
            val intent = Intent(this,crearFactura::class.java)
            startActivity(intent)
            finish()
            dialog.dismiss()

        }
        builder.setNegativeButton("No") { dialog, _ ->

            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }



}