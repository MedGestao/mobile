package br.ifal.med_gestao.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import br.ifal.med_gestao.R
import br.ifal.med_gestao.database.DatabaseHelper
import br.ifal.med_gestao.databinding.ActivityLoginBinding
import br.ifal.med_gestao.domain.Patient
import br.ifal.med_gestao.util.Notification

class ActivityLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)


        val loginButton = binding.loginId
        loginButton.setOnClickListener {
            var email = binding.emailId.text.toString()
            var password = binding.passwordId.text.toString()

            if(email != "" && password != ""){
                var checkPatient = Patient(email, password)
                val dao = DatabaseHelper.getInstance(this).patientDao()
                var patient: Patient? = dao.validatePatient(checkPatient.email)

                if(patient == null){
                    Notification.notification(this, "O e-mail ou a senha está incorreto!")
                }else if(checkPatient.password == patient.password){
                    var intent = Intent(this, ListDoctorsActivity::class.java)
                    startActivity(intent)
                }else{
                    Notification.notification(this, "O e-mail ou a senha está incorreto!")
                }
            }else{
                Notification.notification(this,  "Preencha todos os campos!")
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