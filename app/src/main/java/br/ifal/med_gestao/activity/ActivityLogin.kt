package br.ifal.med_gestao.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import br.ifal.med_gestao.clients.RetrofitHelper
import br.ifal.med_gestao.databinding.ActivityLoginBinding
import br.ifal.med_gestao.domain.Patient
import br.ifal.med_gestao.service.PatientService
import br.ifal.med_gestao.util.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ActivityLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)

        var shared = getSharedPreferences("SHARED_LOGIN", Context.MODE_PRIVATE)

        val loginButton = binding.loginId
        loginButton.setOnClickListener {
            var email = binding.emailId.text.toString()
            var password = binding.passwordId.text.toString()

            if(email != "" && password != ""){
                var checkPatient = Patient(email, password)
//                val dao = DatabaseHelper.getInstance(this).patientDao()
//                var patient: Patient? = dao.validatePatient(checkPatient.email)
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    connector(checkPatient, shared)
                }

            } else {
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

    suspend fun connector(patient : Patient, shared : SharedPreferences) = withContext(Dispatchers.IO) {
        try {
            var patientResponse = PatientService(RetrofitHelper().patientClient()).login(patient)

            if(patientResponse == null){

                Handler(Looper.getMainLooper()).post {
                    Notification.notification(this@ActivityLogin, "O e-mail ou a senha está incorreto!")
                }
            } else {
                var editor = shared.edit()
                editor.putString("PATIENT_ID", patientResponse.id.toString())
                editor.apply()

                var intent = Intent(this@ActivityLogin, ListDoctorsActivity::class.java)

                val bundle = Bundle()
                bundle.putParcelable("patient", patientResponse)

                intent.putExtras(bundle)
                startActivity(intent)
            }
        } catch (exception: Exception) {
            println("erro" + exception.message)
            Handler(Looper.getMainLooper()).post {
                Notification.notification(this@ActivityLogin, "Erro ao fazer login, tente novamente!")
            }
        }
    }
}