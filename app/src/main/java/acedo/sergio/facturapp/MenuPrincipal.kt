package acedo.sergio.facturapp

import acedo.sergio.facturapp.FiredbControl.ControlFBOpcionesPago
import acedo.sergio.facturapp.FiredbControl.ControlFbArticulo
import acedo.sergio.facturapp.FiredbControl.ControlFbCliente
import acedo.sergio.facturapp.FiredbControl.ControlFbFactura
import acedo.sergio.facturapp.ObjetoNegocio.ClienteDatos
import acedo.sergio.facturapp.ObjetoNegocio.Factura
import acedo.sergio.facturapp.ObjetoNegocio.OpcionesPago
import acedo.sergio.facturapp.ObjetoNegocio.Usuario
import acedo.sergio.sqlitedemo.FacturaAdapter
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable


class MenuPrincipal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var user: FirebaseUser
    private lateinit var db: FirebaseFirestore
    private lateinit var toolbar: Toolbar
    private lateinit var drawerlayout: DrawerLayout
    private lateinit var tablayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var navigationView: NavigationView
    private lateinit var btnFlotante: FloatingActionButton
    private var adapter: FacturaAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnTodo: Button
    private lateinit var btnPendiente: Button
    private lateinit var btnPagado: Button


    companion object {
        var facturasEstaticas: ArrayList<Factura> = arrayListOf()
        var UsuarioStatico: Usuario = Usuario()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)
        initView()
        InitRecyclerView()
        getFacturasdeUsuario()


        btnFlotante.setOnClickListener {
            abrirMenuopciones()
        }
        adapter?.setOnClickItem { Factura ->
            var factura = Factura
            var intent = Intent(this, crearFactura::class.java)
            crearFactura.FacturaTrabajandose = factura
            intent.putExtra("factura", factura as Serializable)
            startActivity(intent)
        }
        btnTodo.setOnClickListener { facturasEstaticas = ArrayList()
            allFacturas() }
        btnPendiente.setOnClickListener {
            facturasEstaticas = ArrayList()
            pendientesFacturas() }
        btnPagado.setOnClickListener { facturasEstaticas = ArrayList()
            pagadasFacturas() }

    }

    private fun pagadasFacturas() {
        val FacturaList = ArrayList<Factura>()
        db.collection("Factura").whereEqualTo("estado", "Pagado")
            .whereEqualTo("usuarioDuenio", UsuarioStatico.userid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val facturas = it.result!!
                    //Log.e("data","${document.data}"

                    facturas.forEach { documentoFactura ->
                        val factura = documentoFactura.toObject(Factura::class.java)
                        factura.facturaid = documentoFactura.id
                        FacturaList.add(factura)
                        facturasEstaticas.add(factura)
                        adapter?.addItems(facturasEstaticas)
                    }


                }
            }
    }

    private fun pendientesFacturas() {
        val FacturaList = ArrayList<Factura>()
        db.collection("Factura").whereEqualTo("estado", "pendiente")
            .whereEqualTo("usuarioDuenio", UsuarioStatico.userid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val facturas = it.result!!
                    //Log.e("data","${document.data}"

                    facturas.forEach { documentoFactura ->
                        val factura = documentoFactura.toObject(Factura::class.java)
                        factura.facturaid = documentoFactura.id
                        FacturaList.add(factura)
                        facturasEstaticas.add(factura)
                        adapter?.addItems(facturasEstaticas)
                    }


                }
            }
    }

    private fun allFacturas() {
        db.collection("Factura").whereEqualTo("usuarioDuenio", UsuarioStatico.userid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val facturas = it.result!!
                    //Log.e("data","${document.data}"

                    facturas.forEach { documentoFactura ->
                        val factura = documentoFactura.toObject(Factura::class.java)
                        factura.facturaid = documentoFactura.id

                        facturasEstaticas.add(factura)
                        adapter?.addItems(facturasEstaticas)
                    }

                }
            }
    }


    private fun abrirMenuopciones() {
        val window = PopupWindow(this)
        val view = layoutInflater.inflate(R.layout.menupopup, null)
        window.contentView = view
        window.showAsDropDown(findViewById<RecyclerView>(R.id.rv_Facturas))
        val btnCrearFactura = view.findViewById<Button>(R.id.btn_CrearFactura)
        //val btnCrearPresupuesto = view.findViewById<Button>(R.id.btn_CrearPresupuesto)

        btnCrearFactura.setOnClickListener {
            facturasEstaticas = ArrayList()
            window.dismiss()
            CrearObjetoFactura()

        }
       // btnCrearPresupuesto.setOnClickListener {
       //     window.dismiss()
       //     var intent: Intent = Intent(this, CrearPresupuesto::class.java)
       //     startActivity(intent)
//
       // }
    }

    private fun CrearObjetoFactura() {
        //Agregamos factura nueva para a;adirles los nuevos datos
        var factura = Factura()
        factura.usuarioDuenio = UsuarioStatico.userid
        var intent: Intent = Intent(this, crearFactura::class.java)

        crearFactura.FacturaTrabajandose = factura
        startActivity(intent)

    }

    private fun getFacturasdeUsuario() {
        val FacturaList = ArrayList<Factura>()
        var ControlFbFactura = ControlFbFactura()
        ControlFbFactura.getFactura()

        db.collection("Factura").whereEqualTo("usuarioDuenio", UsuarioStatico.userid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val facturas = it.result!!
                    //Log.e("data","${document.data}"

                    facturas.forEach { documentoFactura ->
                        val factura = documentoFactura.toObject(Factura::class.java)
                        factura.facturaid = documentoFactura.id
                        FacturaList.add(factura)
                        facturasEstaticas.add(factura)
                        adapter?.addItems(facturasEstaticas)
                    }

                }
            }


    }

    private fun InitRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FacturaAdapter()
        recyclerView.adapter = adapter


        // val itemTouchHelper = ItemTouchHelper()
        // itemTouchHelper.attachToRecyclerView(recyclerView )
    }


    private fun reiniciarObjetosGlobales() {
        crearFactura.FacturaTrabajandose = Factura()
        crearFactura.ClienteDatos = ClienteDatos()
        crearFactura.OpcionesPago = OpcionesPago()
        crearFactura.Articulos = ArrayList()

    }

    private fun initView() {


        reiniciarObjetosGlobales()
        recyclerView = findViewById<RecyclerView>(R.id.rv_Facturas)

        btnFlotante = findViewById(R.id.fab)
        try {
            UsuarioStatico = intent.extras?.get("user") as Usuario


        } catch (e: Exception) {
        }
        db = FirebaseFirestore.getInstance()

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        drawerlayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        var ActionBarDrawerToggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerlayout,
            toolbar,
            R.string.openNavDrawer,
            R.string.closeNavDrawer
        )
        drawerlayout.addDrawerListener(ActionBarDrawerToggle)
        ActionBarDrawerToggle.syncState()
        //  navigationView.findViewById<TextView>(R.id.TvCorreo).text = user.email.toString()
        navigationView.setNavigationItemSelectedListener(this)
        val header: View = navigationView.getHeaderView(0)
        header.findViewById<TextView>(R.id.TvCorreo).text = UsuarioStatico.correo.toString()


       btnPendiente = findViewById(R.id.btnPendiente)

         btnTodo = findViewById(R.id.btnTodo)

       btnPagado = findViewById(R.id.btn_Pagado)
    }


    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Seguro que desea cerrar sesion?")
        builder.setCancelable(true)
        builder.setPositiveButton("Si") { dialog, _ ->
           facturasEstaticas = ArrayList()
            val intent = Intent(this, MainActivity::class.java)
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

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemDetallesEmpresa -> {
                facturasEstaticas = ArrayList()
                val intent = Intent(this, datosEmpresa::class.java)
                startActivity(intent)
                return true
            }
      //   R.id.itemClientes -> {
      //       facturasEstaticas = ArrayList()
      //       val intent = Intent(this, informacion::class.java)
      //       startActivity(intent)
      //       return true
      //   }
      //   R.id.itemPagos -> {
      //       facturasEstaticas = ArrayList()
      //       val intent = Intent(this, instruccionesPago::class.java)
      //       startActivity(intent)
      //       return true
      //   }
            R.id.itemConfig -> {
                facturasEstaticas = ArrayList()
                val intent = Intent(this, Ajustes::class.java)
                startActivity(intent)
                return true
            }
            R.id.itemInfo -> {
                facturasEstaticas = ArrayList()
                val intent = Intent(this, informacion::class.java)
                startActivity(intent)
                return true
            }
            R.id.item_Logout -> {
                facturasEstaticas = ArrayList()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Seguro que desea cerrar sesion?")
                builder.setCancelable(true)
                builder.setPositiveButton("Si") { dialog, _ ->
                    val intent = Intent(this, MainActivity::class.java)
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
                return true
            }
        }


        findViewById<TextView>(R.id.TvCorreo).text = user.email.toString()
        return true
    }
}

