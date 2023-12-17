package br.ifal.med_gestao.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.ifal.med_gestao.databinding.ActivityLoginBinding

class AppointmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}