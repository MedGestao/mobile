package br.ifal.med_gestao.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.ifal.med_gestao.R
import br.ifal.med_gestao.database.DatabaseHelper
import br.ifal.med_gestao.databinding.ActivityLoginBinding
import br.ifal.med_gestao.domain.Patient

class ActivityLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        val loginButton = binding.loginId
        loginButton.setOnClickListener {
            var email = binding.emailId.text.toString()
            var password = binding.passwordId.text.toString()

            var checkPatient = Patient(email, password)
            val dao = DatabaseHelper.getInstance(this).patientDao()
            var patient = dao.validatePatient(checkPatient.email)
            println("Email do paciente: " + patient.email)
            println("Senha do paciente: " + patient.password)

            if(checkPatient.password == patient.password){
                println("Entrou na condição!")
                var intent = Intent(this, ListDoctorsActivity::class.java)
                startActivity(intent)
            }
        }

        val registerButton = binding.registerId
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }
}