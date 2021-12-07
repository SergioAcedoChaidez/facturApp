package acedo.sergio.facturapp

import acedo.sergio.facturapp.ObjetoNegocio.Usuario
import acedo.sergio.facturapp.ObjetoNegocio.datosEmpresa
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore

class Ajustes : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
   private lateinit var et_nombrePefil           :  EditText
   private lateinit var CorreoPerfil           : EditText
   private lateinit var et_numeromovil         : EditText
   private lateinit var et_telefonofijoPerfil          : EditText
   private lateinit var direccionPerfil        : EditText
  private lateinit var  btnGuardardatosPeril        : Button

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val CollectionReference = db.collection("Usuario")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)
        initView()
        llenarDatos()
        btnGuardardatosPeril.setOnClickListener{

          guardarEnbd()
        }

    }

    private fun llenarDatos() {
        var id:String
        CollectionReference.document(MenuPrincipal.UsuarioStatico.userid)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    //aqui esta la lista de articulos pero en documento
                    val datoUsuario = it.result!!
                   var usuario= datoUsuario.toObject(Usuario::class.java)
                    et_nombrePefil.setText(usuario?.nombre)
                    CorreoPerfil.setText(usuario?.correo)
                    et_numeromovil.setText(usuario?.telefonoM)
                    et_telefonofijoPerfil.setText(usuario?.telefonoC)
                    direccionPerfil.setText(usuario?.direccion)
                }

            }
    }

    private fun guardarEnbd() {
      var Usuario = Usuario(MenuPrincipal.UsuarioStatico.userid,
          et_nombrePefil.text.toString(),
          CorreoPerfil.text.toString(),
          et_numeromovil.text.toString(),
          et_telefonofijoPerfil.text.toString(),
          arrayListOf(),
          direccionPerfil.text.toString())

    CollectionReference.document(MenuPrincipal.UsuarioStatico.userid).set(Usuario)

     Toast.makeText(this,"Se actualizaron los datos del perfil correctamente",Toast.LENGTH_SHORT).show()

    }

    private fun initView() {
        toolbar = findViewById(R.id.btn_CrearFacturaAppBar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        et_nombrePefil         = findViewById(R.id.et_nombrePefil)
        CorreoPerfil           = findViewById(R.id.CorreoPerfil)
        et_numeromovil         = findViewById(R.id.et_numeromovil)
        et_telefonofijoPerfil  = findViewById(R.id.et_telefonofijoPerfil)
        direccionPerfil        = findViewById(R.id.direccionPerfil)
        btnGuardardatosPeril   = findViewById(R.id.btnGuardardatosPeril)

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