package br.ifal.med_gestao.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.ifal.med_gestao.R
import br.ifal.med_gestao.database.DatabaseHelper
import br.ifal.med_gestao.databinding.ActivityRegisterBinding
import br.ifal.med_gestao.domain.Patient

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bindind = ActivityRegisterBinding.inflate(layoutInflater)
        val registerButton = bindind.buttonRegisterId
        registerButton.setOnClickListener {
            var name = bindind.registerNameId.text.toString()
            var email = bindind.registerEmailId.text.toString()
            var cpf = bindind.registerCpfId.text.toString()
            var birthDate = bindind.registerBirthDateId.text.toString()
            var sex = bindind.registerSexId.text.toString()
            var telephone = bindind.registerTelephoneId.text.toString()
            var password = bindind.passwordId.text.toString()

            val dao = DatabaseHelper.getInstance(this).patientDao()
            dao.insertPatient(Patient(name, email, cpf, birthDate, sex, telephone, password))



            successfulRegistration(this, "Cadastro realizado com sucesso!")

        }

        val loginButton = bindind.buttonLoginId
        loginButton.setOnClickListener {
            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
        }

        setContentView(bindind.root)
    }

    fun successfulRegistration(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}