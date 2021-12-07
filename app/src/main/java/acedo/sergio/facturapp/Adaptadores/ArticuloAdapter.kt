package acedo.sergio.sqlitedemo

import acedo.sergio.facturapp.ObjetoNegocio.Articulo
import acedo.sergio.facturapp.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArticuloAdapter: RecyclerView.Adapter<ArticuloAdapter.ArticuloViewHolder>() {
    private var articuloList:  ArrayList<Articulo> = ArrayList()
    private var onClickItem:((Articulo) -> Unit)? =  null
    private var onClickDeleteItem:((Articulo) -> Unit)? =  null
    private var onClickStartItem:((Articulo) -> Unit)? =  null

    fun addItems(items:ArrayList<Articulo>){
        this.articuloList =  items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (Articulo)-> Unit){

        this.onClickItem = callback

    }
    fun setOnClickDeleteItem(callback: (Articulo)-> Unit){

        this.onClickDeleteItem = callback

    }

    fun setOnClickStartItem(callback: (Articulo)-> Unit){
        this.onClickStartItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ArticuloViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_articulo, parent, false)


    )
    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        val articulo = articuloList[position]
        holder.bindView(articulo)
        holder.itemView.setOnClickListener{ onClickItem?.invoke(articulo)}


    }


    override fun getItemCount(): Int {
        return articuloList.size
    }


    class ArticuloViewHolder(var view: View): RecyclerView.ViewHolder(view){
        private var tv_nombreArticulo = view.findViewById<TextView>(R.id.tv_nombreArticulo)
        private var tv_Total = view.findViewById<TextView>(R.id.tv_Total)
        private var tvCantidad_Precio = view.findViewById<TextView>(R.id.tvCantidad_Precio)

        fun bindView(articulo : Articulo){

            tv_nombreArticulo.text= "${articulo.Descripcion}"
            tvCantidad_Precio.text = "Cantidad * C/U: ${articulo.Cantidad} * ${articulo.CostoUnitario}"
            var Cantidad = articulo.Cantidad
            var CostoUnitario = articulo.CostoUnitario
            var Total = Cantidad * CostoUnitario
            tv_Total.text = "Total: $Total"
        }
    }

}