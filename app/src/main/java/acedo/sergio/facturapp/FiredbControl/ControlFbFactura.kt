package acedo.sergio.facturapp.FiredbControl

import acedo.sergio.facturapp.ObjetoNegocio.Articulo
import acedo.sergio.facturapp.ObjetoNegocio.ClienteDatos
import acedo.sergio.facturapp.ObjetoNegocio.Factura
import acedo.sergio.facturapp.ObjetoNegocio.OpcionesPago
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ControlFbFactura {
    private val db:FirebaseFirestore = FirebaseFirestore.getInstance()
    private val CollectionReference = db.collection("Factura")

    fun anadirFactura(factura: Factura, clienteDatos: ClienteDatos,opcionesPago: OpcionesPago,articulos:ArrayList<Articulo>){
        CollectionReference.add(factura).addOnSuccessListener {
                it ->
            factura.facturaid = it.id

            var FacRef:DocumentReference = CollectionReference
                .document(it.id)

            FacRef.update("facturaid",factura.facturaid
            )

            //updateFactura(factura)
                CollectionReference.document(it.id).collection("Cliente").add(clienteDatos)
                CollectionReference.document(it.id).collection("opcionesPago").add(opcionesPago)
            for (articulo in articulos){
                CollectionReference.document(it.id).collection("Articulo").add(articulo)
            }

        }
    }

    fun getFactura(){
        CollectionReference.addSnapshotListener{
            snapshot ,e ->
            if(e!=null){
                return@addSnapshotListener
            }

            if(snapshot!=null){
                val allFacturas = ArrayList<Factura>()
                val documents = snapshot.documents
                documents.forEach{
                    val factura = it.toObject(Factura::class.java)

                    if(factura!=null){
                        factura.facturaid = it.id
                        Log.e("Cliente","$factura")
                        CollectionReference.document(factura.facturaid).collection("Cliente").addSnapshotListener{
                                snapshot ,e ->
                            if(e!=null){
                                return@addSnapshotListener
                            }

                            if(snapshot!=null){
                                val Cliente = ArrayList<Factura>()
                                val documents = snapshot.documents
                                documents.forEach{
                                    val ClienteDatos = it.toObject(ClienteDatos::class.java)
                                    if (ClienteDatos!= null){
                                        Log.e("Cliente Factura fb","$ClienteDatos")
                                    }
                                }
                                }
                        }
                        allFacturas.add(factura!!)
                    }
                }

            }
        }
    }





    fun updateFactura(factura:Factura){
    var FacRef:DocumentReference = CollectionReference
        .document(factura.facturaid)

     FacRef.update("facturaid",factura.facturaid,
       "nombreFactura",factura.nombreFactura,
                           "usuarioDuenio",factura.usuarioDuenio,
                            "notasAdicionales",factura.notasAdicionales
          )
      }



    }


