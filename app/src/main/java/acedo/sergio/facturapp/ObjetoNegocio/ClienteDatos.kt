package acedo.sergio.facturapp.ObjetoNegocio

import java.io.Serializable

data class ClienteDatos(
    var nombre: String = "",
    var correo:String= "",
    var telefonoMovil: String = "",
    var telefonoFijo: String = "",
    var fax: String= "",
    var direccion:String= "",
    var clienteid:String=""):Serializable