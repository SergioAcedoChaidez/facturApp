package acedo.sergio.facturapp.ObjetoNegocio

data class OpcionesPago (
    var direccionPaypal: String = "",
    var chequeNombrede:String="",
    var transferenciaBancaria:String="",
    var detalles:String="",
    var opcionesPagoid:String= "")