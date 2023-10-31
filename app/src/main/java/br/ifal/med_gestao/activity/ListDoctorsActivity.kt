package br.ifal.med_gestao.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
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

        val dao = DatabaseHelper.getInstance(this).doctorDao()
//        var list = dao.findAll()

        var list = listOf(Doctor(1,
            "https://292aa00292a014763d1b-96a84504aed2b25fc1239be8d2b61736.ssl.cf1.rackcdn.com/GaleriaImagem/132060/fotos-profissionais-para-medicos-e-ambientes_dr-rodrigo-4.jpg",
            "Dr gabriel melo", "Cardiologista"));

//        val fab = binding.floatingActionButton
//        fab.setOnClickListener {
//            val intent = Intent(this, FormActivity::class.java)
//            startActivity(intent)
//        }

        var listView = binding.doctorsListview
        var adapter = DoctorAdapter(this, list)

        listView.adapter = adapter
    }
}