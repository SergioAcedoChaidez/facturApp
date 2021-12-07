package acedo.sergio.facturapp.FiredbControl

import acedo.sergio.facturapp.ObjetoNegocio.OpcionesPago
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ControlFBOpcionesPago {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val CollectionReference = db.collection("OpcionesPago")

    fun addOpcionPago(opcionesPago: OpcionesPago) {
        CollectionReference.add(opcionesPago).addOnSuccessListener { documentReference ->

       //     opcionesPago.OpcionesPagoid = documentReference.id
            Log.e("id", "${documentReference.id}")
            //Actualizamos la factura para que se pueda tener el id en la base de datos mas accesible
            //jaja no me jusquen

           updateOpcionesPago(opcionesPago)

        }
    }

   private fun updateOpcionesPago(opcionesPago: OpcionesPago) {
  //  var opcRef: DocumentReference = CollectionReference
  //      .document(opcionesPago.OpcionesPagoid)
  //  opcRef.update(
  //      "chequeNombrede", opcionesPago.ChequeNombrede,
  //      "detalles", opcionesPago.Detalles,
  //      "direccionPaypal", opcionesPago.DireccionPaypal,
  //      "opcionesPagoid", opcionesPago.OpcionesPagoid,
  //      "transferenciaBancaria", opcionesPago.TransferenciaBancaria

  //  ).addOnCompleteListener { task ->
  //      if (task.isSuccessful) {
  //      } else {
  //      }
  //  }
   }
}