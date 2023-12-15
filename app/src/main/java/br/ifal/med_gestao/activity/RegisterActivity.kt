package br.ifal.med_gestao.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import br.ifal.med_gestao.R
import br.ifal.med_gestao.clients.RetrofitHelper
import br.ifal.med_gestao.database.DatabaseHelper
import br.ifal.med_gestao.databinding.ActivityRegisterBinding
import br.ifal.med_gestao.domain.Patient
import br.ifal.med_gestao.service.PatientService
import br.ifal.med_gestao.util.CpfUtil
import br.ifal.med_gestao.util.MaskUtil
import br.ifal.med_gestao.util.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bindind = ActivityRegisterBinding.inflate(layoutInflater)
        var cpfView = bindind.registerCpfId
        cpfView.addTextChangedListener(MaskUtil.mask("###.###.###-##", cpfView))

        var birthDateView = bindind.registerBirthDateId
        birthDateView.addTextChangedListener(MaskUtil.mask("##/##/####", birthDateView))

        var telephoneView = bindind.registerTelephoneId
        telephoneView.addTextChangedListener(MaskUtil.mask("(##)#####-####", telephoneView))

        val registerButton = bindind.buttonRegisterId
        registerButton.setOnClickListener {
            var name = bindind.registerNameId.text.toString()
            var email = bindind.registerEmailId.text.toString()
            var cpf = bindind.registerCpfId.text.toString()
            var birthDate = bindind.registerBirthDateId.text.toString()
            var sex = bindind.registerSexId.text.toString()
            var telephone = bindind.registerTelephoneId.text.toString()
            var password = bindind.passwordId.text.toString()
            var confirmPassword = bindind.confirmPasswordId.text.toString()

            if(name != "" && email != "" && cpf != "" && birthDate != "" && sex != "" && telephone != "" && password != ""){

                var character = '@'
                var index = email.indexOf(character)
                if(!(email.contains(character) && index > 0)){
                    notification(this, "O e-mail não é válido!")
                }else if (!CpfUtil.myValidateCPF(cpf)){
                    notification(this, "O CPF não é válido!")
                }else if(password != confirmPassword){
                    notification(this, "As senhas não são iguais!")
                }else{
//                    val dao = DatabaseHelper.getInstance(this).patientDao()
//                    dao.insertPatient(Patient(name, email, cpf, birthDate, sex, telephone, password))
                    var patient = Patient(name, email, cpf, birthDate, sex, telephone, password)
                    val scope = CoroutineScope(Dispatchers.IO)
                    scope.launch {
                        connector(patient)
                    }
                }
            }else{
                notification(this, "Preencha todos os campos do cadastro!")
            }

        }

        val loginButton = bindind.buttonLoginId
        loginButton.setOnClickListener {
            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
        }

        setContentView(bindind.root)
    }

    suspend fun connector(patient : Patient) = withContext(Dispatchers.IO) {
        try {
            PatientService(RetrofitHelper().patientClient()).createPatient(patient)

            Handler(Looper.getMainLooper()).post {
                notification(this@RegisterActivity, "Cadastro realizado com sucesso!")
            }
            finish()
        } catch (exception: Exception) {
            println("erro" + exception.message)
            Handler(Looper.getMainLooper()).post {
                Notification.notification(
                    this@RegisterActivity,
                    "Erro ao enviar cadastro, tente novamente!"
                )
            }
        }
    }

    private fun notification(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}