package br.ifal.med_gestao.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import br.ifal.med_gestao.R
import br.ifal.med_gestao.adapters.DoctorAdapter
import br.ifal.med_gestao.databinding.DoctorDetailActivityBinding
import br.ifal.med_gestao.databinding.ScheduleAppointmentFormActivityBinding
import br.ifal.med_gestao.domain.Doctor
import br.ifal.med_gestao.domain.Patient
import com.bumptech.glide.Glide

class DoctorDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        supportActionBar?.setCustomView(R.layout.custom_toolbar_title);

        val doctor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("doctor", Doctor::class.java)
        } else {
            intent.getParcelableExtra("doctor")
        }

        val patient = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("patient", Patient::class.java)
        } else {
            intent.getParcelableExtra("patient")
        }

        val binding = DoctorDetailActivityBinding.inflate(layoutInflater)

        val cardView = binding.buttonIdDoctorDetails
        cardView.setOnClickListener {
            val intent = Intent(this, ScheduleAppointmentFormActivity::class.java)

            val bundle = Bundle()
            bundle.putParcelable("doctor", doctor)
            bundle.putParcelable("patient", patient)

            intent.putExtras(bundle)
            this.startActivity(intent)
        }


        binding.titleDetalheActivity.text = doctor?.name
        binding.descDetalheActivity.text = doctor?.specialty
        val price =doctor?.price.toString()
        binding.valueConsult.text = "${price}" +",00"
        // Set imagem
        Glide.with(this)
            .load(doctor?.image)
            .into(binding.itemListviewDoctorImg)


        setContentView(binding.root)
    }
}