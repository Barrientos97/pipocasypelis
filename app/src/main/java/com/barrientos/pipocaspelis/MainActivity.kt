package com.barrientos.pipocaspelis

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barrientos.pipocaspelis.BaseDeDatos.AppDatabase
import com.barrientos.pipocaspelis.DetalleShowActivity2.Companion.SHOW
import com.barrientos.pipocaspelis.Modelos.ContenedorShow
import com.barrientos.pipocaspelis.Modelos.show
import com.barrientos.pipocaspelis.ServicioRest.servicios
import com.barrientos.pipocaspelis.ServicioRest.tvmaze
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.show_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private var peliculas: ArrayList<show> = arrayListOf()
    lateinit var servicio:tvmaze
    lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      /*
        ///
        //AQUI UTILIZAMOS DIRECTAMENTE EL SERVICIO JSON SIN UTILIZAR RETROFIT
        //
        val tipContenedor = object  : TypeToken<List<ContenedorShow>>(){}.type
        val  contenedorShows = Gson().fromJson<List<ContenedorShow>>(loadJSONFromAsset(this),tipContenedor)
        for (contenedor in contenedorShows){
            peliculas.add((contenedor.show))
        }*/

        db = AppDatabase.getDatabase(this)
        servicio = servicios().getTVMaze()

        descargaPeliculas("police")

        text_buscador.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, R.string.buscando,Toast.LENGTH_LONG).show()
                (lista_Shows.adapter as RecyclerShowAdapter).filter.filter(query)

                query?.let {
                    descargaPeliculas(query)
                }

                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
              //  (lista_Shows.adapter as RecyclerShowAdapter).filter.filter(newText)
                return false            }
        })

    }

   inner class RecyclerShowAdapter(private val peliculas: ArrayList<show>):
        RecyclerView.Adapter<RecyclerShowAdapter.ShowHolder>(),
        Filterable{

       var peliculasFiltradas : MutableList<show> = arrayListOf()
       init{
           peliculasFiltradas = peliculas
       }

       inner class ShowHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bind(Show: show) = with(itemView){
                titulo_Show.text = Show.name
                if (Show.summary!=null)
                    descripcion_Show.text = HtmlCompat.fromHtml(Show.summary, HtmlCompat.FROM_HTML_MODE_LEGACY);
                if (Show.image != null){
                val url = Show.image.medium
                Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(portada_Show);
                }
                itemView.setOnClickListener {
                    val intent = Intent(this@MainActivity, DetalleShowActivity2:: class.java)
                   intent.putExtra(SHOW ,Show )
                    startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.show_item, parent, false)
            return ShowHolder(view)
        }

        override fun onBindViewHolder(holder: ShowHolder, position: Int) {
        holder.bind(peliculasFiltradas.get(position))
        }

        override fun getItemCount(): Int {
            return peliculasFiltradas.size
        }

       override fun getFilter(): Filter {
            return object : Filter(){
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val palabraBuscar = constraint.toString()
                    val resultadoFiltro = ArrayList<show>()
                    for(show in peliculas){

                        if (show.name.toLowerCase().contains(palabraBuscar.toLowerCase())){
                            resultadoFiltro.add(show)
                        }
                    }
                    val filterResults = FilterResults()
                    filterResults.values = resultadoFiltro
                    return filterResults
                }

                override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
                    peliculasFiltradas = filterResults?.values as MutableList<show>
                    notifyDataSetChanged()

                    if(peliculasFiltradas.isEmpty()){
                        text_no_encontrado.visibility = View.VISIBLE
                    }else{
                        text_no_encontrado.visibility = View.GONE
                    }

                }

            }
       }

   }
/*
    fun loadJSONFromAsset(context: Context): String? {
        var json: String? = null
        json = try {
            val `is`: InputStream = context.getAssets().open("shows.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
*/
    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setMessage(R.string.mensaje_desea_salir)

        builder.setPositiveButton("YES") { dialog, which ->
            super.onBackPressed()
        }
            builder.setNegativeButton("NO") { dialog, which ->
           dialog.dismiss()
           }

            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

    fun descargaPeliculas(filtro:String)
    {
        cargando.visibility = View.VISIBLE
        lista_Shows.visibility = View.GONE
        servicio.listarPelicula(filtro).enqueue(object : Callback<List<ContenedorShow>>{

            override fun onResponse(
                call: Call<List<ContenedorShow>>,
                response: Response<List<ContenedorShow>>
            ) {
                cargando.visibility = View.GONE
                lista_Shows.visibility = View.VISIBLE
                if(response.isSuccessful){
                    peliculas.clear()
                    val contenedorShow = response.body()
                    for (contenedor in contenedorShow!!)
                    {
                        peliculas.add(contenedor.show)
                    }

                    GlobalScope.launch {
                        guardarEnBD(peliculas)
                    }

                }else{
                    GlobalScope.launch{
                    peliculas = LeerPeliculas() as ArrayList<show>
                    }
                }
                lista_Shows.layoutManager = GridLayoutManager(this@MainActivity, 3)
                lista_Shows.adapter = RecyclerShowAdapter(peliculas)
            }
            override fun onFailure(call: Call<List<ContenedorShow>>, t: Throwable) {
                cargando.visibility = View.GONE
                lista_Shows.visibility = View.VISIBLE
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()

                GlobalScope.launch{
                    peliculas = LeerPeliculas() as ArrayList<show>

                    withContext(Dispatchers.Main){
                    lista_Shows.layoutManager = GridLayoutManager(this@MainActivity, 3)
                    lista_Shows.adapter = RecyclerShowAdapter(peliculas)
                }
              }
            }

        })
    }
    fun guardarEnBD(peliculas: List<show>){
            db.ShowDAO().guardarList(peliculas)
    }

    fun LeerPeliculas(): List<show>{
      return  db.ShowDAO().listPeliculas()
    }
}