package acedo.sergio.sqlitedemo

import acedo.sergio.facturapp.FiredbControl.ControlFbCliente
import acedo.sergio.facturapp.MenuPrincipal
import acedo.sergio.facturapp.ObjetoNegocio.ClienteDatos
import acedo.sergio.facturapp.ObjetoNegocio.Factura
import acedo.sergio.facturapp.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class FacturaAdapter(): RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {
    private var FacturaList:  ArrayList<Factura> = ArrayList()
    private var onClickItem:((Factura) -> Unit)? =  null
    private var onClickDeleteItem:((Factura) -> Unit)? =  null
    private var onClickStartItem:((Factura) -> Unit)? =  null

    fun addItems(items:ArrayList<Factura>){
        this.FacturaList =  items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (Factura)-> Unit){

        this.onClickItem = callback

    }
    fun setOnClickDeleteItem(callback: (Factura)-> Unit){

        this.onClickDeleteItem = callback

    }

    fun setOnClickStartItem(callback: (Factura)-> Unit){

        this.onClickStartItem = callback

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FacturaViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_factura,parent, false)



    )
    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val factura = FacturaList[position]
        holder.bindView(factura)
        holder.itemView.setOnClickListener{ onClickItem?.invoke(factura)}


    }


    override fun getItemCount(): Int {
        return FacturaList.size
    }



    class FacturaViewHolder(var view: View): RecyclerView.ViewHolder(view){
        var ClienteDatosGlobal: ClienteDatos = ClienteDatos()
        private var tv_ClienteItemFactura = view.findViewById<TextView>(R.id.tv_ClienteItemFactura)
        private var et_NombreFactura = view.findViewById<TextView>(R.id.et_NombreFactura)
        private var tv_Total_a_Pagar = view.findViewById<TextView>(R.id.tv_Total_a_Pagar)
        private var tv_FechaPagarse = view.findViewById<TextView>(R.id.tv_FechaPagarse)

        fun bindView(factura : Factura){
            if(!factura.nombreCliente.trimEnd().isEmpty()){
                tv_ClienteItemFactura.text = factura.nombreCliente
            }
            if (!factura.nombreFactura.trimEnd().isEmpty()){
            et_NombreFactura.text = factura.nombreFactura
            }
            tv_Total_a_Pagar.text  = factura.saldoAdeudado.toString()
            tv_FechaPagarse.text = factura.estado
        }
    }

}
