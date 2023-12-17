package br.ifal.med_gestao.activity

import android.content.Intent
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
                    connector(checkPatient)
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

    suspend fun connector(patient : Patient) = withContext(Dispatchers.IO) {
        try {
            var patientResponse = PatientService(RetrofitHelper().patientClient()).login(patient)

            if(patientResponse == null){

                Handler(Looper.getMainLooper()).post {
                    Notification.notification(this@ActivityLogin, "O e-mail ou a senha está incorreto!")
                }
            } else {
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