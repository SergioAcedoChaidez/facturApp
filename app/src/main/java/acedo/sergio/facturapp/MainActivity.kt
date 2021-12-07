package acedo.sergio.facturapp

import acedo.sergio.facturapp.ObjetoNegocio.ClienteDatos
import acedo.sergio.facturapp.ObjetoNegocio.Factura
import acedo.sergio.facturapp.ObjetoNegocio.Usuario
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var txtUser: EditText
    private lateinit var txtPassword: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initView()

        val btnInicio = findViewById<Button>(R.id.signInAppCompatButton)
        btnInicio.setOnClickListener {
          login()
        }
        val tv_registrate = findViewById<TextView>(R.id.signUpTextView)
        tv_registrate.setOnClickListener {
            var intent: Intent = Intent(this,RegistroUsuario::class.java)
            // getStudents()
            startActivity(intent)
        }
        findViewById<TextView>(R.id.recoveryAccountTextView).setOnClickListener{
            forgotPassword()
        }

    }

    private fun login() {
        val user :String = txtUser.text.toString()
        val password :String = txtPassword.text.toString()

        if(!TextUtils.isEmpty(user)&&!TextUtils.isEmpty(password)){

            val email:String =  user.trim{it <= ' '}
            val contra:String =  password.trim{it <= ' '}
            progressBar.visibility = View.VISIBLE
            //Login with firebase
            auth.signInWithEmailAndPassword(email,contra)
                .addOnCompleteListener{
                    task ->
                    if(task.isSuccessful){
                        action(auth.currentUser!!)

                    }else{
                        progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }



    private fun action(currentUser: FirebaseUser) {

        getUser(currentUser.uid)


    }



    private fun getUser(uid: String) {
        var listUsuario:ArrayList<Usuario> = ArrayList()
        var userid:String
        var nombre:String
        var correo:String
        var telefonoC:String
        var telefonoM:String
        var direccion: String
        db.collection("Usuario")
            .whereEqualTo("userid", uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        //Log.e("data","${document.data}")
                        userid = document.data["userid"].toString()
                        nombre = document.data["nombre"].toString()
                        correo = document.data["correo"].toString()
                        telefonoC = document.data["telefonoC"].toString()
                        telefonoM = document.data["telefonoM"].toString()
                        direccion = document.data["direccion"].toString()

                        var usuario = Usuario(
                            userid,nombre,correo,telefonoC,telefonoM, listOf(),direccion
                        )


                        val intent = Intent(this,MenuPrincipal::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("user",usuario as Serializable)
                        Log.e("Enviado","${usuario}")
                        startActivity(intent)
                        finish()
                    }
                    Log.e("lista usuarios", "${listUsuario}")


                }
            }

    }





    private fun forgotPassword() {
        val intent = Intent(this,ForgotPassword::class.java)
        startActivity(intent)


    }

    private fun initView() {
        db = FirebaseFirestore.getInstance()
        txtUser = findViewById(R.id.emailEditText)
        txtPassword = findViewById(R.id.passwordEditText)
        progressBar = findViewById(R.id.BarraProgreso2)
        auth = FirebaseAuth.getInstance()
    }

    override fun onBackPressed() {


        val builder = AlertDialog.Builder(this)
        builder.setMessage("Seguro que desea salir de la aplicacion:")
        builder.setCancelable(true)
        builder.setPositiveButton("Si"){dialog, _ ->
            finish()
            dialog.dismiss()

        }
        builder.setNegativeButton("No"){dialog, _ ->

            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }




}

