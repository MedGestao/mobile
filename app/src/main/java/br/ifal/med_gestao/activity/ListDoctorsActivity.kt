package br.ifal.med_gestao.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import br.ifal.med_gestao.R
import br.ifal.med_gestao.adapters.DoctorAdapter
import br.ifal.med_gestao.clients.RetrofitHelper
import br.ifal.med_gestao.databinding.ListDoctorsActivityBinding
import br.ifal.med_gestao.domain.Doctor
import br.ifal.med_gestao.domain.Patient
import br.ifal.med_gestao.service.DoctorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListDoctorsActivity : AppCompatActivity() {

    private var patient: Patient? = null

    private val binding by lazy {
        ListDoctorsActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.custom_toolbar_title)

        patient = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("patient", Patient::class.java)
        } else {
            intent.getParcelableExtra("patient")
        }

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        val appointmentsButton = binding.appointmentsButton
        appointmentsButton.setOnClickListener {
            val intent = Intent(this, AppointmentsActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("patient", patient)

            intent.putExtras(bundle)
            startActivity(intent)
        }

//        val dao = DatabaseHelper.getInstance(this).doctorDao()
//        dao.deleteAll()
//        dao.insertAll(doctorsData())
//        var list = dao.findAll()

//        var list = ArrayList<Doctor>()

//        val scope = CoroutineScope(Dispatchers.IO)
//        scope.launch {
//            val doctor = DoctorService(RetrofitHelper().doctorClient()).findDoctorById(1)!!
//            list.add(doctor)
//        }

        var list = ArrayList<Doctor>();

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            var specialtyName = ""
            var doctorName = ""
            list.addAll(DoctorService(RetrofitHelper().doctorClient())
                .getAll(specialtyName, doctorName))
        }

        var listView = binding.doctorsListview
        var adapter = DoctorAdapter(this, patient, list)

        listView.adapter = adapter

        var searchView = binding.searchDoctorsListview
        searchView.clearFocus()
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                if (query != null) {
//                    var newList = dao.findByName(query.lowercase())
//                    adapter.update(newList)
//
//                }
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                val searchText = newText!!.lowercase()
//
//                if (searchText.isNotEmpty()) {
//                    var newList = dao.findByName(searchText)
//                    adapter.update(newList)
//                } else {
//                    adapter.update(list)
//                }
//                return false
//            }
//
//        })

    }
}