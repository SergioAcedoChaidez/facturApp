package acedo.sergio.facturapp

import acedo.sergio.facturapp.FiredbControl.ControlFbFactura
import acedo.sergio.facturapp.ObjetoNegocio.*
import acedo.sergio.sqlitedemo.ArticuloAdapter
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class crearFactura : AppCompatActivity() {

companion object{
    var FacturaTrabajandose:Factura = Factura()
    var ClienteDatos: ClienteDatos = ClienteDatos()
    var OpcionesPago:OpcionesPago = OpcionesPago()
    var Articulos:ArrayList<Articulo> = ArrayList()
}
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val CollectionReference = db.collection("Factura")
    private lateinit var toolbar: Toolbar
    private lateinit var et_NumFactura:EditText
    private lateinit var et_NotasAdicionales:EditText
    private lateinit var btn_Guardar:Button
    private lateinit var btnAnadirArticulo:Button
    private lateinit var btnAnadirCliente:Button
    private lateinit var LLDatosFactura:LinearLayout
    private lateinit var tv_Descuento   :TextView
    private lateinit var tv_Envio       : TextView
    private lateinit var tv_impuestos    :TextView
    private lateinit var tv_Pagado      :TextView

    private lateinit var tv_Total                           :TextView
    private lateinit var btnsaldoAdeudado                    :  Button


    private var adapter :  ArticuloAdapter? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_factura)
        initView()
        InitRecyclerView()
        actualizarCampos()
        anadirArticulos()
        llenarCamposAdicionales()

        et_NumFactura.doAfterTextChanged {
            FacturaTrabajandose.nombreFactura = et_NumFactura.text.toString()
        }
        et_NotasAdicionales.doAfterTextChanged {
            FacturaTrabajandose.notasAdicionales = et_NotasAdicionales.text.toString()
        }
        findViewById<Button>(R.id.btnOpcionesdePago).setOnClickListener {
            val intent = Intent(this,paymentOptions::class.java)
            startActivity(intent)

        }
        btnAnadirCliente.setOnClickListener {
            val intent = Intent(this,NuevoCliente::class.java)
            startActivity(intent)
        }
        btn_Guardar.setOnClickListener {
            GuardarFactura()
            Toast.makeText(this,"¡Se guardo esta factura exitósamente!",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MenuPrincipal::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        btnAnadirArticulo.setOnClickListener {
            val intent = Intent(this,AnadirArticulo::class.java)
            startActivity(intent)
        }
        TV()
         adapter?.setOnClickItem {
             var articulo = it
             var intent= Intent(this,AnadirArticulo::class.java)
             intent.putExtra("articulo", articulo as Serializable)
             startActivity(intent)
         }
        findViewById<TextView>(R.id.showFecha).setOnClickListener{
            fecha()
        }
        findViewById<TextView>(R.id.tv_fecha).setOnClickListener{
            fecha()
        }
    }

    private fun llenarCamposAdicionales() {
       // Toast.makeText(this,"Factura $FacturaTrabajandose",Toast.LENGTH_SHORT).show()

    }

    private fun anadirArticulos() {
        if(!FacturaTrabajandose.facturaid.equals("")) {

            //Todas las facturas las listamos
            CollectionReference.get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val facturas = it.result!!
                        facturas.forEach { documentoFactura ->
                            var Factura: Factura = Factura()
                            Factura.facturaid = documentoFactura.id
                            if (documentoFactura.id.equals(FacturaTrabajandose.facturaid)) {

                                CollectionReference.document(Factura.facturaid)
                                    .collection("Articulo")
                                    .get().addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            //aqui esta la lista de articulos pero en documento
                                            val articulos = it.result!!
                                            articulos.forEach {
                                                //aqui esta el articulo ya separado
                                                    documentoArticulo ->
                                                val articulo =documentoArticulo.toObject(Articulo::class.java)
                                                articulo.articuloid = documentoArticulo.id

                                                Articulos.add(articulo)
                                                adapter?.addItems(Articulos)
                                                actualizarCampos()

                                                var totalParcial:Double = 0.0
                                                var totalaPagar:Double = 0.00
                                                var SaldoAdeudado:Double = 0.00

                                                for (articulo in Articulos){
                                                    var total = articulo.Cantidad * articulo.CostoUnitario
                                                    totalParcial = totalParcial + total
                                                }

                                                totalaPagar = totalParcial -
                                                        FacturaTrabajandose.descuentos +
                                                        FacturaTrabajandose.envios +
                                                        FacturaTrabajandose.impuesto

                                                FacturaTrabajandose.totalaPagar = totalaPagar

                                                SaldoAdeudado = FacturaTrabajandose.totalaPagar - FacturaTrabajandose.pagado
                                                FacturaTrabajandose.saldoAdeudado = SaldoAdeudado

                                                findViewById<Button>(R.id.btn_totalParcial).setText("Total Parcial:                        $${totalParcial}")
                                                //Aqui guardamos en la base de datos
                                                CollectionReference.document(FacturaTrabajandose.facturaid).set(FacturaTrabajandose)
                                            }
                                        }
                                    }
                            }

                        }
                    }

                }
        }else {
            adapter?.addItems(Articulos)
        }
    }

    private fun fecha() {

        val cal  = Calendar.getInstance()
        val timeSetListener = DatePickerDialog.OnDateSetListener{
                datePicker, anio, mes, dia ->
            cal.set(Calendar.YEAR,anio)
            cal.set(Calendar.MONTH,mes)
            cal.set(Calendar.DAY_OF_MONTH,dia)
            findViewById<TextView>(R.id.showFecha).text = SimpleDateFormat("dd/MM/yyyy").format(cal.time)
            FacturaTrabajandose.fechaVence= SimpleDateFormat("dd/MM/yyyy").format(cal.time)

            findViewById<TextView>(R.id.showFecha).setTextColor(Color.parseColor("#5DE0DD"))
        }
        DatePickerDialog(this,timeSetListener,cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()

    }

    private fun TV() {
        tv_Descuento.setOnClickListener{abrirDatos()}
        tv_Envio.setOnClickListener{abrirDatos() }
        tv_impuestos.setOnClickListener{abrirDatos() }
        tv_Pagado.setOnClickListener{abrirDatos() }
    }

    private fun abrirDatos() {
        val intent = Intent(this,datosFactura::class.java)
        startActivity(intent)
    }




    private fun actualizarCampos() {
        findViewById<TextView>(R.id.showFecha).text =  FacturaTrabajandose.fechaVence
        et_NotasAdicionales.setText(FacturaTrabajandose.notasAdicionales)


        var totalParcial:Double = 0.0
        var totalaPagar:Double = 0.00
        var SaldoAdeudado:Double = 0.00
        //Total parcial
        for (articulo in Articulos){
            var MontoporArticulo = articulo.Cantidad * articulo.CostoUnitario
            totalParcial = totalParcial + MontoporArticulo
        }
        FacturaTrabajandose.totalParcial = totalParcial

        //Total
        totalaPagar = FacturaTrabajandose.totalParcial -
                FacturaTrabajandose.descuentos +
                FacturaTrabajandose.envios +
                FacturaTrabajandose.impuesto
        FacturaTrabajandose.totalaPagar = totalaPagar

        SaldoAdeudado = FacturaTrabajandose.totalaPagar - FacturaTrabajandose.pagado

        FacturaTrabajandose.saldoAdeudado = SaldoAdeudado

        findViewById<Button>(R.id.btn_totalParcial).setText("Total Parcial:                        $${totalParcial}")
        findViewById<Button>(R.id.btn_SaldoAdeudado).setText("Saldo Adeudado:                        $${SaldoAdeudado}")


        if(FacturaTrabajandose.descuentos != 0.0){
            tv_Descuento.setText( "$${FacturaTrabajandose.descuentos}"  )
        }
        if(FacturaTrabajandose.envios != 0.0){
            tv_Envio.setText("$${FacturaTrabajandose.envios}")
        }
        if(FacturaTrabajandose.impuesto != 0.0){
            tv_impuestos.setText("$${FacturaTrabajandose.impuesto}")
        }
        if(FacturaTrabajandose.pagado != 0.0){
            tv_Pagado.setText("$${FacturaTrabajandose.pagado}")
        }

        if(FacturaTrabajandose.totalaPagar != 0.0){
            tv_Total.text = "$${FacturaTrabajandose.totalaPagar}"
        }

        if(FacturaTrabajandose.saldoAdeudado != 0.0){
            btnsaldoAdeudado.text = "Saldo adeudado:                        $${FacturaTrabajandose.saldoAdeudado}"
        }

    }

    private fun GuardarFactura() {
        FacturaTrabajandose.notasAdicionales = et_NotasAdicionales.text.toString()
        FacturaTrabajandose.nombreFactura = et_NumFactura.text.toString()
        FacturaTrabajandose.usuarioDuenio = MenuPrincipal.UsuarioStatico.userid
        //Ahora lo que queremos hacer porque por alguna razon no se asigno la facturaid en ningun obketo
        // asi que crearemos estos metodos para arreglar este problema
        var ControlFbFactura =ControlFbFactura()
        if (FacturaTrabajandose.saldoAdeudado == 0.0){
            FacturaTrabajandose.estado = "Pagado"
        }
        if(FacturaTrabajandose.facturaid.equals("")){
            FacturaTrabajandose.nombreCliente = ClienteDatos.nombre

            ControlFbFactura.anadirFactura(FacturaTrabajandose, ClienteDatos, OpcionesPago, Articulos)
        }else{
            if (FacturaTrabajandose.saldoAdeudado == 0.0){
                FacturaTrabajandose.estado = "Pagado"
            }
            CollectionReference.document(FacturaTrabajandose.facturaid).set(FacturaTrabajandose)
        }

        MenuPrincipal.facturasEstaticas = ArrayList()
        Articulos = ArrayList()
    }

    private fun InitRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ArticuloAdapter()
        recyclerView.adapter = adapter


        // val itemTouchHelper = ItemTouchHelper()
        // itemTouchHelper.attachToRecyclerView(recyclerView )
    }

    private fun initView() {
        recyclerView = findViewById<RecyclerView>(R.id.rvArticulos)
        toolbar = findViewById(R.id.btn_CrearFacturaAppBar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        et_NumFactura = findViewById(R.id.et_NumFactura)
        et_NotasAdicionales = findViewById(R.id.et_NotasaAdicionales)
        btn_Guardar = findViewById(R.id.btn_GuardarFactura)
        btnAnadirCliente = findViewById(R.id.btn_anadirCliente)
        et_NumFactura.setText(FacturaTrabajandose.nombreFactura)
        LLDatosFactura = findViewById(R.id.LLDatosFactura)
        btnAnadirArticulo= findViewById(R.id.btnAnadirArticulo)
        tv_Descuento = findViewById(R.id.tv_Descuento)
        tv_Envio= findViewById(R.id.tv_Envio)
        tv_impuestos= findViewById(R.id.tv_impuestos)
        tv_Pagado= findViewById(R.id.tv_Pagado)
        tv_Total=    findViewById(R.id.tv_total_antes_pago)
        btnsaldoAdeudado= findViewById(R.id.btn_SaldoAdeudado)

        findViewById<TextView>(R.id.tv_NombreCliente).setText("Cliente: ${FacturaTrabajandose.nombreCliente}")

}




    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        atras()


        return true
    }

    override fun onBackPressed() {
        atras()
    }

    private fun atras() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Seguro que desea salir sin guardar esta factura?")
        builder.setCancelable(true)
        builder.setPositiveButton("Si") { dialog, _ ->
            Articulos = ArrayList()
            MenuPrincipal.facturasEstaticas = ArrayList()
            val intent = Intent(this,MenuPrincipal::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            dialog.dismiss()

        }
        builder.setNegativeButton("No") { dialog, _ ->

            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }
}