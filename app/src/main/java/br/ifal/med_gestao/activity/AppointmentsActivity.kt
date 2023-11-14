package br.ifal.med_gestao.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import br.ifal.med_gestao.R
import br.ifal.med_gestao.adapters.AppointmentAdapter
import br.ifal.med_gestao.database.DatabaseHelper
import br.ifal.med_gestao.databinding.AppointmentsActivityBinding
import br.ifal.med_gestao.domain.Patient

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

        val dao = DatabaseHelper.getInstance(this).appointmentDao()

        var list = dao.getAppointmentsByPatientId(patient!!.id)
        Log.i("AppointmentsActivity", list.toString())

        var listView = binding.appointmentsListview
        var adapter = AppointmentAdapter(this, list)

        listView.adapter = adapter

    }
}