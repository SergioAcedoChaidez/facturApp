package acedo.sergio.facturapp

import acedo.sergio.facturapp.ObjetoNegocio.ClienteDatos
import acedo.sergio.facturapp.ObjetoNegocio.Factura
import acedo.sergio.facturapp.ObjetoNegocio.Usuario
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable


class RegistroUsuario : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var  db: FirebaseFirestore
    private lateinit var txtNombreNegocio: EditText
    private lateinit var txtCorreo: EditText
    private lateinit var txtConstrasenia: EditText
    private lateinit var txtTelefonoM: EditText
    private lateinit var Usuario:Usuario
    private lateinit var txtTelefonoC: EditText
    private lateinit var txtDireccion: EditText
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)

        initView()
        findViewById<Button>(R.id.btn_registrar).setOnClickListener {
            crearCuentaNueva()
        }

    }



    private fun crearCuentaNueva() {
        val Nombre = txtNombreNegocio.text.toString()
        val Correo = txtCorreo.text.toString()
        val password = txtConstrasenia.text.toString()
        val telefonoC = txtTelefonoC.text.toString()
        val telefonoM = txtTelefonoM.text.toString()
        val direccion = txtDireccion.text.toString()
        val progressBar = progressBar

        if(!TextUtils.isEmpty(Nombre) &&
            !TextUtils.isEmpty(Correo) &&
            !TextUtils.isEmpty(password)){
            val email:String =  Correo.trim{it <= ' '}
            val contra:String =  password.trim{it <= ' '}
            progressBar.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email,contra)
                .addOnCompleteListener(
                    {  task ->

                        if (task.isSuccessful){

                            val user: FirebaseUser = task.result!!.user!!
                            verifyEmail(user)
                            Toast.makeText(this,"Registrado correctamente",Toast.LENGTH_SHORT).show()


                            Usuario = Usuario(user.uid, Nombre,Correo,telefonoC,telefonoM,
                                listOf(),direccion)

                            db.collection("Usuario").document(user.uid).set(Usuario
                            )
                            action(user)
                        }else{
                            progressBar.visibility = View.INVISIBLE
                            Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                )
        }

    }

    private fun action(user: FirebaseUser) {
        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        intent.putExtra("user_id",user.uid)
        intent.putExtra("email_id",Usuario as Serializable)
        startActivity(intent)
        finish()
    }


    private fun verifyEmail(user: FirebaseUser?){
        if (user != null) {
            user.sendEmailVerification()
                .addOnCompleteListener(this){ task ->
                    if(task.isComplete){
                        Toast.makeText(this,"Correo Enviado",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"Error al enviar correo",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    private fun initView() {
        toolbar = findViewById(R.id.btn_CrearFacturaAppBar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        db = FirebaseFirestore.getInstance()
        txtNombreNegocio = findViewById(R.id.pt_nombreNegocio)
        txtCorreo = findViewById(R.id.et_email)
        txtConstrasenia = findViewById(R.id.edt_contraseña)
        txtTelefonoM = findViewById(R.id.etTelefonoM)
        txtTelefonoC = findViewById(R.id.etTelefonoC)
        txtDireccion = findViewById(R.id.editTextTextPostalAddress)

        progressBar = findViewById(R.id.BarraProgreso)
        auth = FirebaseAuth.getInstance()
    }


}