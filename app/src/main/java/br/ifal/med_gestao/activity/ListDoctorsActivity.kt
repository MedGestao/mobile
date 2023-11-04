package br.ifal.med_gestao.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import br.ifal.med_gestao.R
import br.ifal.med_gestao.adapters.DoctorAdapter
import br.ifal.med_gestao.database.DatabaseHelper
import br.ifal.med_gestao.databinding.ListDoctorsActivityBinding
import br.ifal.med_gestao.domain.Doctor

class ListDoctorsActivity : AppCompatActivity() {

    private val binding by lazy {
        ListDoctorsActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        supportActionBar?.setCustomView(R.layout.custom_toolbar_title);

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

//        RoomDatabase.clearAllTables();

        val dao = DatabaseHelper.getInstance(this).doctorDao()
        dao.deleteAll()
        dao.insertAll(staticDoctors())
        var list = dao.findAll()

        //        val fab = binding.floatingActionButton
//        fab.setOnClickListener {
//            val intent = Intent(this, FormActivity::class.java)
//            startActivity(intent)
//        }

        var searchList = arrayListOf<Doctor>()
        var searchView = binding.searchDoctorsListview
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.lowercase()

                if (searchText.isNotEmpty()) {
                    list = dao.findByName(searchText)

//                    recyclerView.adapter!!.notifyDatasetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(list)
//                    recyclerView.adapter!!.notifyDatasetChanged()
                }
                return false
            }

        })

        var listView = binding.doctorsListview
        var adapter = DoctorAdapter(this, list)

        listView.adapter = adapter
    }

    private fun staticDoctors(): List<Doctor> {
        return listOf(Doctor(1,
            "https://292aa00292a014763d1b-96a84504aed2b25fc1239be8d2b61736.ssl.cf1.rackcdn.com/GaleriaImagem/132060/fotos-profissionais-para-medicos-e-ambientes_dr-rodrigo-4.jpg",
            "Gabriel Melo",
            "Cardiologista"),
            Doctor(2,
            "https://storage.alboom.ninja/sites/716/albuns/1117452/carine-103.jpg",
            "Cecília Almeida",
                "Endocrinologista"),
            Doctor(3,
                "https://i.pinimg.com/originals/84/fe/29/84fe291a1b11bf1276183347ad62c96d.jpg",
                "Heloísa Bastos",
                "Neurologista"),
            Doctor(4,
                "https://portal.unit.br/wp-content/uploads/2022/03/07.03-MED-Daniel-Silva-1024x798.jpg",
                "Gabriel Telles",
                "Neurologista"),
            Doctor(5,
                "https://eduardomafra.files.wordpress.com/2021/04/ensaio-dr-fabio-medico-perfil-profissional-salvador-lauro-de-freitas-bahia-eduardo-mafra-fotografo-estudio-profissional-2-1.jpg",
                "Luciano Santos",
                "Hematologista"),
            Doctor(6,
                "https://christianeribeiro.com.br/wp-content/uploads/2020/08/fotopaginainicial.jpg",
                "Ludmila Santos",
                "Psiquiatra"),
            Doctor(7,
                "https://rsaude.com.br/img/pessoas/g/-19082019112629.png",
                "Sandra Gomes",
                "Psiquiatra"),
            Doctor(8,
                "https://blogdasaude.com.br/wp-content/uploads/Dra-Danielle-Negri.jpg",
                "Aline Silva",
                "Ginecologista"),
            Doctor(9,
                "https://www.catalogo.med.br/fmfiles/index.asp/::app_Catalogo::/jeffrey-frederico-lui-filho-1.jpg",
                "Humberto Silva",
                "Cardiologista"),
            Doctor(10,
                "https://www.catalogo.med.br/fmfiles/index.asp/::catalogo::/fotos/crop_nVIdawj2WhatsApp_Image_2023-07-19_at_22.49.49.jpg",
                "Larissa Fernandes",
                "Ginecologista"),
            )
    }
}