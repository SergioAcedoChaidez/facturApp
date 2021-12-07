package acedo.sergio.facturapp.FiredbControl

import acedo.sergio.facturapp.ObjetoNegocio.Articulo
import acedo.sergio.facturapp.ObjetoNegocio.ClienteDatos
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ControlFbArticulo {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val CollectionReference = db.collection("Articulo")

    fun addArticulo(Articulo: Articulo) {
        CollectionReference.add(Articulo).addOnSuccessListener { documentReference ->


            Log.e("id", "${documentReference.id}")
            //Actualizamos la factura para que se pueda tener el id en la base de datos mas accesible
            //jaja no me jusquen

         //  updateArticulo(Articulo)

        }
    }
    private fun updateArticulo(articulo: Articulo) {
    // var CltRef: DocumentReference = CollectionReference
    //  .document(articulo.Articuloid)
     // CltRef.update(
     //     "facturaid", articulo.Facturaid,
     //     "articuloid", articulo.Articuloid,
     //     "descripcion", articulo.Descripcion,
     //     "CostoUnitario", articulo.CostoUnitario,
     //     "Cantidad", articulo.Cantidad,
     //     "monto", articulo.Monto,
     //     "detallesAdicionales", articulo.DetallesAdicionales

     // ).addOnCompleteListener { task ->
     //     if (task.isSuccessful) {
     //     } else {

     //     }
     // }
    }
}