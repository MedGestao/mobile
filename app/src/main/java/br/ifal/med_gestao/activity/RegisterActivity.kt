package br.ifal.med_gestao.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.view.isVisible
import br.ifal.med_gestao.clients.RetrofitHelper
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
import java.text.ParseException
import java.text.SimpleDateFormat

class RegisterActivity : AppCompatActivity() {

    private var patient: Patient? = null
    private var patientId : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        patient = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("patient", Patient::class.java)
        } else {
            intent.getParcelableExtra("patient")
        }

        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        val registerButton = binding.buttonRegisterId
        val loginButton = binding.buttonLoginId
        if (patient?.id != null) {
            patientId = patient!!.id
            val titleEdit = binding.registerTitleId
            titleEdit.text = "Editar Perfil"

            registerButton.text = "ALTERAR"

            loginButton.text = "VOLTAR"

            var birthdate = formatDate(patient!!.birthDate)
            binding.registerNameId.setText(patient?.name)
            binding.registerEmailId.setText(patient?.email)
            binding.registerCpfId.setText(patient?.cpf)
            binding.registerBirthDateId.setText(birthdate)
            if(patient?.sex == "M"){
                binding.registerSexId.setText("Masculino")
            }else if(patient?.sex == "F"){
                binding.registerSexId.setText("Feminino")
            }
            binding.registerTelephoneId.setText(patient?.telephone)
            println("Data de nascimento: " + patient?.birthDate)
        }


        var cpfView = binding.registerCpfId
        cpfView.addTextChangedListener(MaskUtil.mask("###.###.###-##", cpfView))

        var birthDateView = binding.registerBirthDateId
        birthDateView.addTextChangedListener(MaskUtil.mask("##/##/####", birthDateView))

        var telephoneView = binding.registerTelephoneId
        telephoneView.addTextChangedListener(MaskUtil.mask("(##)#####-####", telephoneView))

        registerButton.setOnClickListener {
            var name = binding.registerNameId.text.toString()
            var email = binding.registerEmailId.text.toString()
            var cpf = binding.registerCpfId.text.toString()
            var birthDate = binding.registerBirthDateId.text.toString()
            var sex = binding.registerSexId.text.toString()
            var telephone = binding.registerTelephoneId.text.toString()
            var password = binding.passwordId.text.toString()
            var confirmPassword = binding.confirmPasswordId.text.toString()

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
                        if (patient.id != null) {
                            connectorEditPatient(patientId, patient)
                            patientId = 0
                        }else {
                            connector(patient)
                        }
                    }
                }
            }else{
                notification(this, "Preencha todos os campos do cadastro!")
            }

        }

        loginButton.setOnClickListener {
            val intent : Intent = if(patientId != 0L){
                Intent(this, ListDoctorsActivity::class.java)
            }else{
                Intent(this, ActivityLogin::class.java)
            }
            startActivity(intent)
        }

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

//        println("Id do paciente: " + patient?.id)
//        println("Nome do paciente: " + patient?.name)

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

    suspend fun connectorSelectById(id : Long) = withContext(Dispatchers.IO) {
        try {
            var patientEdit = PatientService(RetrofitHelper().patientClient()).findPatientById(id)

//            Handler(Looper.getMainLooper()).post {
//                notification(this@RegisterActivity, "Cadastro realizado com sucesso!")
//            }
            finish()
        } catch (exception: Exception) {
            println("erro" + exception.message)
            Handler(Looper.getMainLooper()).post {
                Notification.notification(
                    this@RegisterActivity,
                    "Erro ao retornar dados, tente novamente!"
                )
            }
        }
    }

    suspend fun connectorEditPatient(patientId : Long, patient : Patient) = withContext(Dispatchers.IO) {
        try {
            PatientService(RetrofitHelper().patientClient()).editPatient(patientId, patient)

            Handler(Looper.getMainLooper()).post {
                notification(this@RegisterActivity, "Cadastro alterado com sucesso!")
            }
            finish()
        } catch (exception: Exception) {
            println("erro" + exception.message)
            Handler(Looper.getMainLooper()).post {
                Notification.notification(
                    this@RegisterActivity,
                    "Erro ao alterar dados do cadastro, tente novamente!"
                )
            }
        }
    }

    private fun notification(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun formatDate(date : String): String {
        try {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val newFormat = SimpleDateFormat("dd/MM/yyyy")

            val data = originalFormat.parse(date)
            return newFormat.format(data)
        } catch (e: ParseException) {
            e.printStackTrace()
            // Tratar exceção, se necessário
        }
        return ""
    }
}