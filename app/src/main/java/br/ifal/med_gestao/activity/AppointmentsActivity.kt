package br.ifal.med_gestao.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import br.ifal.med_gestao.R
import br.ifal.med_gestao.adapters.AppointmentAdapter
import br.ifal.med_gestao.clients.RetrofitHelper
import br.ifal.med_gestao.database.DatabaseHelper
import br.ifal.med_gestao.databinding.AppointmentsActivityBinding
import br.ifal.med_gestao.domain.Appointment
import br.ifal.med_gestao.domain.AppointmentWithDoctor
import br.ifal.med_gestao.domain.Patient
import br.ifal.med_gestao.service.AppointmentService
import br.ifal.med_gestao.service.DoctorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppointmentsActivity : AppCompatActivity() {

    private val binding by lazy {
        AppointmentsActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        supportActionBar?.setCustomView(R.layout.custom_toolbar_title);
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        val patient = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("patient", Patient::class.java)
        } else {
            intent.getParcelableExtra("patient")
        }

        var list = ArrayList<AppointmentWithDoctor>()

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            list.addAll(
                AppointmentService(RetrofitHelper().appointmentClient()).getAppointmentsByPatientID(patient!!.id))
        }

//        val dao = DatabaseHelper.getInstance(this).appointmentDao()
//        var list = dao.getAppointmentsByPatientId(patient!!.id)

        var listView = binding.appointmentsListview
        var adapter = AppointmentAdapter(this, list)

        listView.adapter = adapter

    }
}