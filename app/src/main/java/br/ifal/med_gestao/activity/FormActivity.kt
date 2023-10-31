package br.ifal.med_gestao.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.ifal.med_gestao.database.DatabaseHelper
import br.ifal.med_gestao.databinding.ActivityFormBinding
import br.ifal.med_gestao.domain.Doctor

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityFormBinding.inflate(layoutInflater)

        val button = binding.button
        button.setOnClickListener{

            var urlImage = binding.editTextText.text.toString()
            var nomePacote = binding.editTextText2.text.toString()
            var desc =binding.editTextText3.text.toString()

            val dao = DatabaseHelper.getInstance(this).doctorDao()
            dao.insert(Doctor(0, urlImage, nomePacote, desc))

            finish()
        }



        setContentView(binding.root)
    }
}