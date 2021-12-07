package acedo.sergio.facturapp.ObjetoNegocio

import java.io.Serializable

data class Articulo (
    var Descripcion:String=""
    , var CostoUnitario:Double=0.0
    , var Cantidad:Double=0.0,
    var Monto:Double=0.0,
    var DetallesAdicionales:String="",
            var articuloid:String= ""):Serializable