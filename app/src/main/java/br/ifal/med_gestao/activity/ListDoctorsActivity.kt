package br.ifal.med_gestao.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
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
import br.ifal.med_gestao.service.PatientService
import br.ifal.med_gestao.util.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val editProfileButton = binding.editProfileButton
        editProfileButton.setOnClickListener {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                connectorSelectById(patient!!.id)
            }
        }

//        val dao = DatabaseHelper.getInstance(this).doctorDao()
//        dao.deleteAll()
//        dao.insertAll(doctorsData())
//        var list = dao.findAll()

//        var list = ArrayList<Doctor>()

    private fun getDoctors(searchText: String, adapter: DoctorAdapter) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            var newList = doctorService.getAll(searchText)

            launch(Dispatchers.Main) {
                adapter.update(newList)
            }
        }
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

    suspend fun connectorSelectById(id : Long) = withContext(Dispatchers.IO) {
        try {
            var patientEdit = PatientService(RetrofitHelper().patientClient()).findPatientById(id)

//            Handler(Looper.getMainLooper()).post {
//                notification(this@RegisterActivity, "Cadastro realizado com sucesso!")
//            }
            val intent = Intent(this@ListDoctorsActivity, RegisterActivity ::class.java)
            val bundle = Bundle()
            bundle.putParcelable("patient", patientEdit)

            intent.putExtras(bundle)
            startActivity(intent)
        } catch (exception: Exception) {
            println("erro" + exception.message)
            Handler(Looper.getMainLooper()).post {
                Notification.notification(
                    this@ListDoctorsActivity,
                    "Erro ao retornar dados, tente novamente!"
                )
            }
        }
    }

    private fun notification(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}