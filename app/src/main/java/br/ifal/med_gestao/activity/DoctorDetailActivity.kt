package br.ifal.med_gestao.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.ifal.med_gestao.databinding.DoctorDetailActivityBinding
import br.ifal.med_gestao.domain.Doctor
import com.bumptech.glide.Glide

class DoctorDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val doctor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("doctor", Doctor::class.java)
        } else {
            intent.getParcelableExtra("doctor")
        }

        val binding = DoctorDetailActivityBinding.inflate(layoutInflater)
        binding.titleDetalheActivity.text = doctor?.name
        binding.descDetalheActivity.text = doctor?.specialty

        // Set imagem
        Glide.with(this)
            .load(doctor?.image)
            .into(binding.imageDetalheActivity)


        setContentView(binding.root)
    }
}