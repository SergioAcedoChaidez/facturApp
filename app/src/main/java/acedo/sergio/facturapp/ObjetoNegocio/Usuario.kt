package acedo.sergio.facturapp.ObjetoNegocio

import androidx.core.text.isDigitsOnly
import com.google.firebase.auth.FirebaseUser
import java.io.Serializable
data class Usuario(var userid: String = "",
                   var nombre:String="",
                   var correo:String="",
                   var telefonoC:String="",
                   var telefonoM:String="",
                   var listaFactura:List<Factura> = listOf(),
                   var direccion: String=""):Serializable