package acedo.sergio.facturapp

import acedo.sergio.facturapp.ObjetoNegocio.Factura
import acedo.sergio.facturapp.ObjetoNegocio.OpcionesPago
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

class paymentOptions : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var et_DireccionPaypal: EditText
    private lateinit var et_ChequeNombreDe: EditText
    private lateinit var et_transferenciaBan: EditText
    private lateinit var et_otrosDetalles: EditText
    private lateinit var btn_GuardarOpcionesDePago: Button

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val CollectionReference = db.collection("Factura")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_options)
        initView()
        actualizarDatos()
        btn_GuardarOpcionesDePago.setOnClickListener {
            guardarOpcionesPago()
        }
        findViewById<Button>(R.id.btnMarcarComopagada).setOnClickListener{
            action()
        }
    }

    private fun action() {
        crearFactura.FacturaTrabajandose.estado = "Pagado"
        Toast.makeText(this,"Se han actualizado exitoasmente las opciones de pago",Toast.LENGTH_SHORT).show()
        if(!crearFactura.FacturaTrabajandose.facturaid.equals("")){
            CollectionReference.document(crearFactura.FacturaTrabajandose.facturaid).set(crearFactura.FacturaTrabajandose)
        }
    }

    private fun actualizarDatos() {
        if(crearFactura.FacturaTrabajandose.facturaid.equals("")){
            et_ChequeNombreDe  .setText(crearFactura.OpcionesPago.chequeNombrede)
            et_DireccionPaypal .setText(crearFactura.OpcionesPago.direccionPaypal)
            et_transferenciaBan.setText(crearFactura.OpcionesPago.transferenciaBancaria)
            et_otrosDetalles.   setText(crearFactura.OpcionesPago.detalles)
        }else{
            btn_GuardarOpcionesDePago.setText("Actualizar opciones de pago")
            //Todas las facturas las listamos

            CollectionReference.document(crearFactura.FacturaTrabajandose.facturaid)
                .collection("opcionesPago").get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        //aqui esta la lista de articulos pero en documento
                        val opcionesPago = it.result!!
                        opcionesPago.forEach {
                            //aqui esta el articulo ya separado
                                documento ->
                            val opcionesPago =documento.toObject(OpcionesPago::class.java)
                            opcionesPago.opcionesPagoid = documento.id
                            et_ChequeNombreDe.setText(opcionesPago.chequeNombrede)
                            et_DireccionPaypal.setText(opcionesPago.direccionPaypal)
                            et_transferenciaBan.setText(opcionesPago.transferenciaBancaria)
                            et_otrosDetalles.setText(opcionesPago.detalles)



                        }
                    }
                }
        }

    }

    private fun guardarOpcionesPago() {
        var OpcionesPago: OpcionesPago = OpcionesPago(
            et_DireccionPaypal.text.toString(),
            et_ChequeNombreDe.text.toString(),
            et_transferenciaBan.text.toString(),
            et_otrosDetalles.text.toString()
        )
        if(crearFactura.FacturaTrabajandose.facturaid.equals("")){

            crearFactura.OpcionesPago = OpcionesPago
        }else{
            crearFactura.Articulos = ArrayList()
            var id:String


            CollectionReference.document(crearFactura.FacturaTrabajandose.facturaid)
                .collection("opcionesPago").get().addOnCompleteListener{
                    if (it.isSuccessful) {
                        //aqui esta la lista de articulos pero en documento
                        val Opcpagos = it.result!!
                        Opcpagos.forEach {
                            //aqui esta el articulo ya separado
                                documentoOpsPagos ->
                            id = documentoOpsPagos.id

                            CollectionReference.document(crearFactura.FacturaTrabajandose.facturaid)
                                .collection("opcionesPago")
                                .document(id).set(OpcionesPago)
                            Toast.makeText(this,"Se han actualizado exitoasmente las opciones de pago",Toast.LENGTH_SHORT).show()

                        }
                    }
                }
        }


        // MenuPrincipal.controlFbOpcionesPago.addOpcionPago(OpcionesPago)

        Toast.makeText(this,"Se han actualizado exitosamente las opciones de pago", Toast.LENGTH_SHORT).show()
        var intent: Intent = Intent(this, crearFactura::class.java)
        startActivity(intent)
        finish()
    }

    private fun initView() {
        toolbar = findViewById(R.id.btn_CrearFacturaAppBar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        et_DireccionPaypal = findViewById(R.id.et_DireccionPaypal)
        et_ChequeNombreDe= findViewById(R.id.et_ChequeNombreDe)
        et_transferenciaBan= findViewById(R.id.et_Impuesto)
        et_otrosDetalles= findViewById(R.id.et_otrosDetalles)

        btn_GuardarOpcionesDePago= findViewById(R.id.btnGuardardatosFactura)


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
        builder.setMessage("¿Seguro que desea salir sin guardar los cambios las opciones de pago?")
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