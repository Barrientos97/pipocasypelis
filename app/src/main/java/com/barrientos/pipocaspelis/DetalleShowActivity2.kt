package com.barrientos.pipocaspelis

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.barrientos.pipocaspelis.BaseDeDatos.AppDatabase
import com.barrientos.pipocaspelis.Modelos.ShowPrefered
import com.barrientos.pipocaspelis.Modelos.show
import kotlinx.android.synthetic.main.activity_detalle_show2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalleShowActivity2 : AppCompatActivity() {
    companion object {
        public val SHOW = "SHOW"
    }

    lateinit var showseleccionado : show
    lateinit var Shared: SharedPreferences
    lateinit var db:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_show2)


        showseleccionado = intent.getSerializableExtra(SHOW) as show
        db= AppDatabase.getDatabase(this)
        setTitle(showseleccionado.name)

        Toast.makeText(this, showseleccionado.name, Toast.LENGTH_LONG).show()
        Shared = getSharedPreferences(getString(R.string.favoritos),Context.MODE_PRIVATE)


        val demoCollectionAdapter = DemoPageAdapter(supportFragmentManager)
        paginador.adapter = demoCollectionAdapter
        tabs_layout.setupWithViewPager(paginador)

        favorito.setOnClickListener{
            GlobalScope.launch {
            guardarFavoritos(showseleccionado.id.toLong())
            cambiarIcono()
            }
        }

        cambiarIcono()
    }

    inner class DemoPageAdapter(fm:FragmentManager):FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Fragment {
                when(position){
                    0 -> return  DetalleShowFragment.newInstance(showseleccionado)
                    1->  return  InforacionTecnicaFragment.newInstance(showseleccionado)
                    else ->  return  DetalleShowFragment.newInstance(showseleccionado)
                }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position)
            {
                0-> "Resumen"
                1-> "Detalle"
                else -> "Resumen"
            }
        }
    }


    fun guardarFavoritos(id:Long){

        /* val esfavorito = !(Shared.getBoolean(nombre, false))
         with(Shared.edit()){
             putBoolean(nombre, esfavorito)
             apply()
             cambiarIcono()*/

        var preferido = db.ShowPreferidoDAO().get(id)
        if (preferido != null){
            preferido.esFAVORITO = !preferido.esFAVORITO
            db.ShowPreferidoDAO().actualizar(preferido)
       }else{
           preferido = ShowPrefered(0 , id , true)
            db.ShowPreferidoDAO().guardar(preferido)
        }

        }

    fun leerpreferido(id: Long):Boolean{

        val prefered = db.ShowPreferidoDAO().get(id)
        if(prefered != null)
        {
            return prefered.esFAVORITO

        }else{
            return false
        }
    }

    fun cambiarIcono(){

        GlobalScope.launch {
        val esPrefered = withContext(Dispatchers.IO){leerpreferido(showseleccionado.id.toLong())}

        if(esPrefered){
            favorito.setImageResource(R.drawable.ic_baseline_wite_24)
        }else {
            favorito.setImageResource(R.drawable.ic_baseline_favorite_wite_24)
        }
        }
    }
}