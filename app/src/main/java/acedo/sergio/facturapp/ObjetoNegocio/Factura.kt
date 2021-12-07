package acedo.sergio.facturapp.ObjetoNegocio

import java.io.Serializable
import java.util.*


data class Factura(
    var facturaid:String = "",
    var nombreFactura: String = "",
    var usuarioDuenio: String = "",
    var notasAdicionales: String = "",
    var totalParcial:Double= 0.0,
    var totalaPagar: Double = 0.00,
    var saldoAdeudado: Double = 0.00,
    var descuentos: Double = 0.00,
    var envios: Double = 0.00,
    var impuesto: Double = 0.00,
    var pagado: Double = 0.00,
    var nombreCliente: String="",
    var fechaVence: String = "",
    var estado:String = "pendiente"
    ):Serializable
