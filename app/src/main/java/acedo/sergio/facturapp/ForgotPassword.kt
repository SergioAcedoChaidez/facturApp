package acedo.sergio.facturapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    private lateinit var txtUser: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initView()
        findViewById<AppCompatButton>(R.id.btn_EnviarRecovery).setOnClickListener{
            send()
        }
    }

    private fun send() {
       val txtCorreo = txtUser.text.toString()
        if(!TextUtils.isEmpty(txtCorreo)){
            auth.sendPasswordResetEmail(txtCorreo)
                .addOnCompleteListener(this){
                    task ->
                    if(task.isSuccessful){
                        progressBar.visibility = View.VISIBLE
                    startActivity(Intent(this,MainActivity::class.java))
                    }else{
                        Toast.makeText(this,"Error al enviar el email", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initView() {

        progressBar = findViewById(R.id.BarraProgreso3)
        txtUser = findViewById(R.id.et_emailRecovery)
        auth = FirebaseAuth.getInstance()
    }
}