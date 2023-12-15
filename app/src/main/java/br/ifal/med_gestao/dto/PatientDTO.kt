package br.ifal.med_gestao.dto

import br.ifal.med_gestao.domain.Patient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class PatientDTO (
    var user : UserDTO
) {
    val convertToPatient : Patient
        get() = Patient(
            id = user.id,
            name = user.name,
            birthDate = user.birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            cpf = user.cpf,
            sex = user.sex,
            email = user.email,
            telephone = user.cellphoneUser.number,
            password = user.password)

    companion object {
        fun of(patient: Patient, date: LocalDateTime) = PatientDTO(
            user = UserDTO.of(patient, date)
        )
    }
}

class PatientAuthDTO(
    var email : String,
    var password: String
)


class PatientAuthDTOResponse(
    var id : String
)