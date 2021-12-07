package acedo.sergio.facturapp.FiredbControl

import acedo.sergio.facturapp.ObjetoNegocio.ClienteDatos
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class ControlFbCliente {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val CollectionReference = db.collection("Cliente")

    fun addCliente(cliente: ClienteDatos) {
        CollectionReference.add(cliente).addOnSuccessListener { documentReference ->

           // informacion.Clienteid = documentReference.id
            Log.e("id", "${documentReference.id}")
            //Actualizamos la factura para que se pueda tener el id en la base de datos mas accesible
            //jaja no me jusquen

            updateCliente(cliente)

        }
    }

    fun getClientebyfacturaid(facturaId :String){


        CollectionReference.whereEqualTo("facturaid",facturaId).get().addOnSuccessListener {

            var ClienteDatosGlobal: ClienteDatos = ClienteDatos()
                for (document in it) {
                    Log.e("Documento " , "${document.data}")
               //    ClienteDatosGlobal.Clienteid = document.data["clienteid"].toString()
                   ClienteDatosGlobal.nombre = document.data["nombre"].toString()
                   ClienteDatosGlobal.correo = document.data["correo"].toString()
                    ClienteDatosGlobal.telefonoMovil = document.data["telefonoMovil"].toString()
                  ClienteDatosGlobal.telefonoFijo = document.data["telefonoFijo"].toString()
                  ClienteDatosGlobal.fax = document.data["fax"].toString()
                  ClienteDatosGlobal.direccion = document.data["direccion"].toString()
                  //ClienteDatosGlobal.Facturaid = facturaId

                    Log.e("ClienteDatosGlobalAdap" , "${ClienteDatosGlobal}")

                }

        }
    }


    fun updateCliente(cliente: ClienteDatos) {
        //    var CltRef: DocumentReference = CollectionReference
        //        .document(informacion.Clienteid)
        //    CltRef.update(
        //        "clienteid", informacion.Clienteid,
        //        "nombre", informacion.Nombre,
        //        "correo", informacion.Correo,
        //        "telefonoMovil", informacion.TelefonoMovil,
        //        "telefonoFijo", informacion.TelefonoFijo,
        //        "fax", informacion.Fax,
        //        "direccion", informacion.Direccion

        //    ).addOnCompleteListener { task ->
        //        if (task.isSuccessful) {
        //        } else {

        //        }
        //    }
        //}
    }
}


