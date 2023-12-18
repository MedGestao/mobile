package br.ifal.med_gestao.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
    private var doctorService = DoctorService(RetrofitHelper().doctorClient());

    private val binding by lazy {
        ListDoctorsActivityBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        DoctorAdapter(this, patient, ArrayList<Doctor>())
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

        var listView = binding.doctorsListview
        listView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        getDoctors("", adapter)

        var searchView = binding.searchDoctorsListview
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                if (query != null) {
                    getDoctors(query.lowercase(), adapter)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText!!.lowercase()

                if (searchText.isNotEmpty()) {
                    getDoctors(searchText, adapter)
                } else {
                    getDoctors(searchText, adapter)
                }
                return true
            }

        })

        val appointmentsButton = binding.appointmentsButton
        appointmentsButton.setOnClickListener {
            val intent = Intent(this, AppointmentsActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("patient", patient)

            intent.putExtras(bundle)
            startActivity(intent)
        }

    }

    private fun getDoctors(searchText: String, adapter: DoctorAdapter) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            var newList = doctorService.getAll(searchText)

            launch(Dispatchers.Main) {
                adapter.update(newList)
            }
        }
    }
}