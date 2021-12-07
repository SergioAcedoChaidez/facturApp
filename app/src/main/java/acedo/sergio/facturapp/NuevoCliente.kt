package acedo.sergio.facturapp

import acedo.sergio.facturapp.ObjetoNegocio.Articulo
import acedo.sergio.facturapp.ObjetoNegocio.ClienteDatos
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class NuevoCliente : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var btn_GuardarCliente:Button
    private lateinit var et_Nombre:EditText
    private lateinit var et_CorreoElectronico:EditText
    private lateinit var et_TelefonoFijo:EditText
    private lateinit var et_fax:EditText
    private lateinit var et_Direccion:EditText

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val CollectionReference = db.collection("Factura")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_cliente)


        initView()
        llenarCamposInciar()
        btn_GuardarCliente.setOnClickListener{
            guardarCliente()
        }
    }



    private fun guardarCliente() {
        val Nombre = et_Nombre.text.toString()
        val Correo = et_CorreoElectronico.text.toString()
        val TelMovil = findViewById<TextInputEditText>(R.id.et_telefonoMovil).text.toString()
        val TelFijo = et_TelefonoFijo.text.toString()
        val Fax = et_fax.text.toString()
        val Direccion = et_Direccion.text.toString()
        val ClienteDatos= ClienteDatos(Nombre,Correo,TelMovil,TelFijo,Fax,Direccion)

        if(crearFactura.FacturaTrabajandose.facturaid.equals("")){

            crearFactura.ClienteDatos = ClienteDatos
            crearFactura.FacturaTrabajandose.nombreCliente = Nombre
            Toast.makeText(this,"Se ha creado exitoasmente el nuevo informacion",Toast.LENGTH_SHORT).show()

        }else{
              crearFactura.Articulos = ArrayList()
              crearFactura.FacturaTrabajandose.nombreCliente = Nombre
        var id:String
        CollectionReference.document(crearFactura.FacturaTrabajandose.facturaid).update("nombreCliente",et_Nombre.text.toString())
            CollectionReference.document(crearFactura.FacturaTrabajandose.facturaid)
                .collection("Cliente").get().addOnCompleteListener{
                    if (it.isSuccessful) {
                        //aqui esta la lista de articulos pero en documento
                        val Cliente = it.result!!
                        Cliente.forEach {
                            //aqui esta el articulo ya separado
                                documentoCliente ->
                            id = documentoCliente.id
                            CollectionReference.document(crearFactura.FacturaTrabajandose.facturaid)
                                .collection("Cliente")
                                .document(id).set(ClienteDatos)
                            Toast.makeText(this,"Se ha actualizado exitoasmente el nuevo informacion",Toast.LENGTH_SHORT).show()

                        }
                    }
                }
        }


        vaciarCampos()
       var intent = Intent(this, crearFactura::class.java)
        startActivity(intent)
        finish()
    }

    private fun vaciarCampos() {
        et_Nombre.setText("")
        et_CorreoElectronico.setText("")
        findViewById<TextInputEditText>(R.id.et_telefonoMovil).setText("")
        et_TelefonoFijo.setText("")
        et_fax.setText("")
        et_Direccion.setText("")
    }
    private fun llenarCamposInciar() {
        if(crearFactura.FacturaTrabajandose.facturaid.equals("")){

            et_Nombre.setText(crearFactura.ClienteDatos.nombre)
            et_CorreoElectronico.setText(crearFactura.ClienteDatos.correo)
            findViewById<TextInputEditText>(R.id.et_telefonoMovil).setText(crearFactura.ClienteDatos.telefonoMovil)
            et_TelefonoFijo.setText(crearFactura.ClienteDatos.telefonoFijo)
            et_fax.setText(crearFactura.ClienteDatos.fax)
            et_Direccion.setText(crearFactura.ClienteDatos.direccion)
        }else{

            btn_GuardarCliente.setText("Actualizar informacion")
            //Todas las facturas las listamos

            CollectionReference.document(crearFactura.FacturaTrabajandose.facturaid)
                .collection("Cliente").get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        //aqui esta la lista de articulos pero en documento
                        val Cliente = it.result!!
                        Cliente.forEach {
                            //aqui esta el articulo ya separado
                                documentoCliente ->
                            val cliente =documentoCliente.toObject(ClienteDatos::class.java)
                            cliente.clienteid = documentoCliente.id

                            et_Nombre.setText(cliente.nombre)
                            et_CorreoElectronico.setText(cliente.correo)
                            findViewById<TextInputEditText>(R.id.et_telefonoMovil).setText(cliente.telefonoMovil)
                            et_TelefonoFijo.setText(cliente.telefonoFijo)
                            et_fax.setText(cliente.fax)
                            et_Direccion.setText(cliente.direccion)

                        }
                    }
                }
        }
    }


    private fun initView() {

        toolbar = findViewById(R.id.btn_CrearFacturaAppBar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        btn_GuardarCliente = findViewById(R.id.btn_GuardarClienteNuevo)
        et_Nombre  = findViewById(R.id.et_Nombre)
        et_CorreoElectronico = findViewById(R.id.et_CorreoElectronico)
        et_TelefonoFijo = findViewById(R.id.et_TelefonoFijo)
        et_fax = findViewById(R.id.et_fax)
        et_Direccion = findViewById(R.id.et_Direccion)

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
        builder.setMessage("¿Seguro que desea salir sin guardar los cambios al informacion?")
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