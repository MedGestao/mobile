package br.ifal.med_gestao.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

            if (patient != null) {
                if(checkPatient.password == patient.password){

                    var intent = Intent(this, ListDoctorsActivity::class.java)

                    val bundle = Bundle()
                    bundle.putParcelable("patient", patient)

                    intent.putExtras(bundle)
                    startActivity(intent)
                }

            } else {
                Toast.makeText(this, "O e-mail ou a senha estão inválidos!", Toast.LENGTH_SHORT).show()
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