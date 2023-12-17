package br.ifal.med_gestao.dto

import br.ifal.med_gestao.domain.Doctor

class DoctorDTO(
    var crm : String,
    var user : UserSimpleDTO,
    var specialty : Specialty,
) {
    val convertToDoctor : Doctor get() = Doctor(
        id = user.id,
        crm = crm,
        name = user.name,
        image = user.imageUrl,
        specialty = specialty.description)
}

class Specialty(
    var description : String
)
