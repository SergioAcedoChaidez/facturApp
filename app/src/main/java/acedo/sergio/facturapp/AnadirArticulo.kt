package acedo.sergio.facturapp

import acedo.sergio.facturapp.ObjetoNegocio.Articulo
import acedo.sergio.facturapp.ObjetoNegocio.Factura
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class AnadirArticulo : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var et_Descripcion: EditText
    private lateinit var et_CostoUnitario: EditText
    private lateinit var et_cantidad: EditText
    private lateinit var tv_Monto: TextView
    private lateinit var et_DetallesAdicionales: TextView
    private lateinit var btn_GuardarArticulo: TextView
    private var confirmacion:Int = -1
    var Articuloactualizar :Articulo = Articulo()

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val CollectionReference = db.collection("Factura")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_articulo)
        initView()
        try {
            Articuloactualizar = intent.extras?.get("articulo") as Articulo
            confirmacion = 1
            btn_GuardarArticulo.setText("Actualizar artículo")
                //significa que no es de la base de datos
                actualizarArticulo(Articuloactualizar)
                cambiarMonto()

        }catch (e:Exception){
            confirmacion = 0
            Log.e("message", "${e.message}")
        }

        cambiarMonto()
        btn_GuardarArticulo.setOnClickListener {
            //la confirmacion de que es un objeto del recyclerview
            if (confirmacion == 1){
                if(Articuloactualizar.articuloid.equals("")){
                    UpdateArticuloInArray()
                }else{

                    actualizarArticuloDebd()
                }
              }else if (confirmacion == 0){

                 guardarArticulo()
              }
        }
        et_CostoUnitario.doAfterTextChanged {
            cambiarMonto()

        }
        et_cantidad.doAfterTextChanged {
            cambiarMonto()
        }

    }

    private fun actualizarArticuloDebd() {
        var et_CostoUnitario:String =  et_CostoUnitario.text.toString()
        var et_cantidad:String =       et_cantidad.text.toString()
        var CostoUnitario :Double = 0.0
        var Cantidad :Double= 0.0
        if(et_CostoUnitario != ""){
            CostoUnitario = et_CostoUnitario.toDouble()
        }
        if(et_cantidad != ""){
            Cantidad = et_cantidad.toDouble()
        }

        var Descripcion =  et_Descripcion.text.toString()
        var et_DetallesAdicionales=et_DetallesAdicionales.text.toString()

        var id = Articuloactualizar.articuloid
        Articuloactualizar = Articulo(Descripcion,
            CostoUnitario,
            Cantidad,
            Cantidad*CostoUnitario,
            et_DetallesAdicionales,
            id)

      //  Toast.makeText(this,"$Articuloactualizar",Toast.LENGTH_SHORT).show()

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val CollectionReference = db.collection("Factura")

        CollectionReference.document(crearFactura.FacturaTrabajandose.facturaid)
            .collection("Articulo").document(Articuloactualizar.articuloid)
            .set(Articuloactualizar)
        crearFactura.Articulos=ArrayList()
        var intent= Intent(this, crearFactura::class.java)
        startActivity(intent)
        finish()

    }

    private fun UpdateArticuloInArray() {
        var indexof = crearFactura.Articulos.indexOf(Articuloactualizar)
        val articulo = crearFactura.Articulos.get(indexof)

        var et_CostoUnitario:String =  et_CostoUnitario.text.toString()
        var et_cantidad:String =et_cantidad.text.toString()
        var CostoUnitario :Double = et_CostoUnitario.toDouble()
        var Cantidad :Double = et_cantidad.toDouble()
        var Descripcion =  et_Descripcion.text.toString()

        var et_DetallesAdicionales=et_DetallesAdicionales.text.toString()
        articulo.Monto = CostoUnitario * Cantidad
        articulo.DetallesAdicionales = et_DetallesAdicionales
        articulo.CostoUnitario       = CostoUnitario
        articulo.Cantidad            = Cantidad
        articulo.Descripcion         = Descripcion
        Toast.makeText(this,"Se ha Actualizado correctamente este articulo", Toast.LENGTH_SHORT).show()
        var intent= Intent(this, crearFactura::class.java)
        startActivity(intent)
        finish()


    }
    private fun actualizarArticulo(articulo: Articulo) {
        et_CostoUnitario.setText( articulo.CostoUnitario.toString())
        et_cantidad.setText( articulo.Cantidad.toString())
        et_DetallesAdicionales.setText(articulo.DetallesAdicionales)
        et_Descripcion.setText( articulo.Descripcion)
        cambiarMonto()
    }


    private fun cambiarMonto() {

        var et_CostoUnitario:String =  et_CostoUnitario.text.toString()
        var et_cantidad:String =       et_cantidad.text.toString()
        var CostoUnitario :Double = 0.0
        var Cantidad :Double= 0.0
        if(et_CostoUnitario != "" && et_CostoUnitario != "."){
            CostoUnitario = et_CostoUnitario.toDouble()
        }
        if(et_cantidad != "" && et_cantidad != "."){
            Cantidad = et_cantidad.toDouble()
        }
     //  Log.e("CostoUnitario","$CostoUnitario")
     //  Log.e("Cantidad","$Cantidad")

        var Monto = CostoUnitario * Cantidad
        tv_Monto.setText("$$Monto")
    }

    private fun guardarArticulo() {
        var et_CostoUnitario:String =  et_CostoUnitario.text.toString()
        var et_cantidad:String =       et_cantidad.text.toString()
        var CostoUnitario :Double = 0.0
        var Cantidad :Double= 0.0
        var Descripcion =  et_Descripcion.text.toString()
        var et_DetallesAdicionales=et_DetallesAdicionales.text.toString()

        if(et_CostoUnitario != ""){
            CostoUnitario = et_CostoUnitario.toDouble()
        }
        if(et_cantidad != ""){
            Cantidad = et_cantidad.toDouble()
        }



        if(et_CostoUnitario.equals("")||
            et_cantidad.equals("")||
            Descripcion.equals("")){
            Toast.makeText(this,"Ingrese los datos necesario para el artículo", Toast.LENGTH_SHORT).show()
            return
        }

            var articulo =
                Articulo(Descripcion,CostoUnitario,Cantidad,CostoUnitario*Cantidad,et_DetallesAdicionales)
            if(crearFactura.FacturaTrabajandose.facturaid.equals("")){

                crearFactura.Articulos.add(articulo)
            }else {
            CollectionReference.document(crearFactura.FacturaTrabajandose.facturaid).collection("Articulo").document().set(articulo)
                crearFactura.Articulos = ArrayList()
        }

            Toast.makeText(this,"Se ha añadido correctamente este articulo", Toast.LENGTH_SHORT).show()
            var intent= Intent(this, crearFactura::class.java)
            startActivity(intent)
            finish()
    }

    private fun initView() {
        toolbar = findViewById(R.id.btn_CrearFacturaAppBar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        et_Descripcion = findViewById(R.id.et_Descripcion)
        et_CostoUnitario=findViewById(R.id.et_Costo_Unitario)
        et_cantidad= findViewById(R.id.et_Cantidad)
        tv_Monto= findViewById(R.id.tv_Monto)
        et_DetallesAdicionales= findViewById(R.id.et_otrosDetalles)
        btn_GuardarArticulo= findViewById(R.id.btn_GuardarArticulo)

    }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        atras()


        return true
    }

    override fun onBackPressed() {
        atras()
    }

    private fun atras() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Seguro que desea salir sin guardar este articulo?")
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

