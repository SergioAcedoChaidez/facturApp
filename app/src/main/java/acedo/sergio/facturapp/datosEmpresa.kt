package acedo.sergio.facturapp

import acedo.sergio.facturapp.ObjetoNegocio.Usuario
import acedo.sergio.facturapp.ObjetoNegocio.datosEmpresa
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class datosEmpresa : AppCompatActivity() {
    private  lateinit var Imagen: ImageView
    private  var filePath:Uri? = null
    private var  PICK_IMAGE_REQUEST = 1234
    private lateinit var toolbar: Toolbar
    private lateinit var btnguardar           : Button
    private lateinit var et_Nombreempresa      : EditText
    private lateinit var et_CorreoElectronico   : EditText
    private lateinit var et_Telefono          : EditText
    private lateinit var et_numeronegocio      : EditText
    private lateinit var et_sitioweb:          EditText
    private lateinit var et_nombreduenio        : EditText
    private lateinit var et_Direccionlinea      : EditText

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val CollectionReference = db.collection("Usuario")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_empresa)
        initView()
        llenarDatos()
        Imagen.setOnClickListener{
            slectImage()

        }
        btnguardar.setOnClickListener{

            guardarEnbd()
           uploadImage()
        }

    }

    private fun uploadImage() {
        val progressBar = ProgressDialog(this)


        if(filePath!=null){
            filePath.toString()
            progressBar.setTitle("Subiendo archivo...")
            progressBar.setCancelable(false)
            progressBar.show()
            db.collection("Imagen").add(filePath!!)


        }
    }

    private fun slectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action =Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"),PICK_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!= null){
                filePath = data.data


                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filePath)
                    Imagen.setImageBitmap(bitmap)
                }catch (e:IOException){
                    e.printStackTrace()
                }
            }
    }

    private fun llenarDatos() {
        var id:String
        CollectionReference.document(MenuPrincipal.UsuarioStatico.userid)
            .collection("detallesEmpresa").document(MenuPrincipal.UsuarioStatico.userid).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    //aqui esta la lista de articulos pero en documento
                    val datoEmpresa = it.result!!

                        //aqui esta el articulo ya separado
                        val Empresa =datoEmpresa.toObject(datosEmpresa::class.java)
                        et_Nombreempresa.setText(Empresa?.nombreNegocio)
                        et_nombreduenio.setText(Empresa?.nombreDuenioEmpresa)
                        et_numeronegocio.setText(Empresa?.numeroNegocio)
                        et_Direccionlinea.setText(Empresa?.direccion)
                        et_CorreoElectronico.setText(Empresa?.correoelectronico)
                        et_Telefono.setText(Empresa?.telefono)
                        et_sitioweb.setText(Empresa?.sitioweb)

                    }
                }

            }


    private fun guardarEnbd() {
        var DatosdelaEmpresa = datosEmpresa(
            et_Nombreempresa.text.toString(),
            et_nombreduenio.text.toString(),
            et_numeronegocio.text.toString(),
            et_Direccionlinea.text.toString(),
            et_CorreoElectronico.text.toString(),
            et_Telefono.text.toString(),
            et_sitioweb.text.toString())
        //Verificar que no este en base se datos



        CollectionReference.document(MenuPrincipal.UsuarioStatico.userid)
            .collection("detallesEmpresa").document(MenuPrincipal.UsuarioStatico.userid)
            .set(DatosdelaEmpresa)

     Toast.makeText(this,"Se actualizaron los datos de la empresa correctamente",Toast.LENGTH_SHORT).show()

  // }
    }

    private fun initView() {
        toolbar = findViewById(R.id.btn_CrearFacturaAppBar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        btnguardar = findViewById(R.id.btn_GuardardatosdeEmpresa)
        et_Nombreempresa= findViewById(R.id.et_Nombre_Negocio)
        et_CorreoElectronico= findViewById(R.id.CorreoElectronicoEmpresa)
        et_Telefono= findViewById(R.id.et_telefono)
        et_numeronegocio = findViewById(R.id.et_numeroNegocio)
        et_sitioweb= findViewById(R.id.et_sitio_web)
        et_nombreduenio =  findViewById(R.id.NombreDuenio)
        et_Direccionlinea =  findViewById(R.id.et_DireccionEmpresa)
        Imagen = findViewById(R.id.Imagen)
    }


    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        atras()


        return true
    }

    override fun onBackPressed() {
        atras()
    }

    private fun atras() {

            if(!crearFactura.FacturaTrabajandose.facturaid.equals("")){
                crearFactura.Articulos = ArrayList()
            }
            val intent = Intent(this,MenuPrincipal::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()




    }
}