package br.ifal.med_gestao.dto

import br.ifal.med_gestao.domain.Patient
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserSimpleDTO(
    var id : Long,
    var name : String,
    var imageUrl: String
)

data class UserDTO(
    var id : Long,
    var name : String,
    var imageUrl: String,
    var birthDate: String,
    var cpf: String,
    var sex: String,
    var email: String,
    var password: String,
    var cellphoneUser: CellphoneDTO
) {
    companion object {
        fun of(patient: Patient, birthDate: LocalDateTime) = UserDTO(
            id = patient.id,
            name = patient.name,
            birthDate = birthDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")),
            cpf = patient.cpf.replace(Regex("[^0-9]"), ""),
            sex = patient.sex.first().toString(),
            email = patient.email,
            password = patient.password,
            imageUrl = patient.imageUrl,
            cellphoneUser = CellphoneDTO(patient.telephone))
    }
}
// time.ParseError {Layout: "\"2006-01-02T15:04:05Z07:00\""
class CellphoneDTO(
    var number : String
)