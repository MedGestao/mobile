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
import java.math.BigDecimal

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

        val dao = DatabaseHelper.getInstance(this).doctorDao()
        dao.deleteAll()
        dao.insertAll(staticDoctors())
        var list = dao.findAll()

        var listView = binding.doctorsListview
        var adapter = DoctorAdapter(this, list)

        listView.adapter = adapter

        var searchView = binding.searchDoctorsListview
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                if (query != null) {
                    var newList = dao.findByName(query.lowercase())
                    adapter.update(newList)

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText!!.lowercase()

                if (searchText.isNotEmpty()) {
                    var newList = dao.findByName(searchText)
                    adapter.update(newList)
                } else {
                    adapter.update(list)
                }
                return false
            }

        })

    }

    private fun staticDoctors(): List<Doctor> {
        return listOf(Doctor(1,
            "https://292aa00292a014763d1b-96a84504aed2b25fc1239be8d2b61736.ssl.cf1.rackcdn.com/GaleriaImagem/132060/fotos-profissionais-para-medicos-e-ambientes_dr-rodrigo-4.jpg",
            "Gabriel Melo",
            "Cardiologista",
            BigDecimal(200)),
            Doctor(2,
            "https://storage.alboom.ninja/sites/716/albuns/1117452/carine-103.jpg",
            "Cecília Almeida",
                "Endocrinologista",
                BigDecimal(300)),
            Doctor(3,
                "https://i.pinimg.com/originals/84/fe/29/84fe291a1b11bf1276183347ad62c96d.jpg",
                "Heloísa Bastos",
                "Neurologista",
                BigDecimal(350)),

            Doctor(4,
                "https://portal.unit.br/wp-content/uploads/2022/03/07.03-MED-Daniel-Silva-1024x798.jpg",
                "Gabriel Telles",
                "Neurologista",
                BigDecimal(250)),
            Doctor(5,
                "https://eduardomafra.files.wordpress.com/2021/04/ensaio-dr-fabio-medico-perfil-profissional-salvador-lauro-de-freitas-bahia-eduardo-mafra-fotografo-estudio-profissional-2-1.jpg",
                "Luciano Santos",
                "Hematologista",
                BigDecimal(200)),
            Doctor(6,
                "https://christianeribeiro.com.br/wp-content/uploads/2020/08/fotopaginainicial.jpg",
                "Ludmila Santos",
                "Psiquiatra",
                BigDecimal(280)),
            Doctor(7,
                "https://rsaude.com.br/img/pessoas/g/-19082019112629.png",
                "Sandra Gomes",
                "Psiquiatra",
                BigDecimal(300)),
            Doctor(8,
                "https://blogdasaude.com.br/wp-content/uploads/Dra-Danielle-Negri.jpg",
                "Aline Silva",
                "Pediatra",
                BigDecimal(250)),
            Doctor(9,
                "https://www.catalogo.med.br/fmfiles/index.asp/::app_Catalogo::/jeffrey-frederico-lui-filho-1.jpg",
                "Humberto Silva",
                "Cardiologista",
                BigDecimal(300)),
            Doctor(10,
                "https://www.catalogo.med.br/fmfiles/index.asp/::catalogo::/fotos/crop_nVIdawj2WhatsApp_Image_2023-07-19_at_22.49.49.jpg",
                "Larissa Fernandes",
                "Angiologista",
                BigDecimal(200)),

            )
    }
}