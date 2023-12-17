package br.ifal.med_gestao.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import br.ifal.med_gestao.activity.ScheduleAppointmentFormActivity
import br.ifal.med_gestao.databinding.ItemListDoctorsActivityBinding
import br.ifal.med_gestao.domain.Doctor
import br.ifal.med_gestao.domain.Patient
import com.bumptech.glide.Glide

class DoctorAdapter(val context: Context, val patient: Patient?, var doctors: List<Doctor>) : BaseAdapter() {
    override fun getCount(): Int {
        return doctors.size
    }

    override fun getItem(position: Int): Any {
        return doctors[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun update(newData: List<Doctor>) {
        doctors = newData
        notifyDataSetChanged()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?,
    ): View {
        // Inflate c/ viewBinding
        // Inflar o Card dentro de um item do ListView
        val inflater = LayoutInflater.from(context)
        val binding = ItemListDoctorsActivityBinding.inflate(inflater, parent, false)

        // Settando novos valores p/ views do Card
        binding.itemListviewDoctorName.text = doctors[position].name
        binding.itemListviewDoctorSpecialty.text = doctors[position].specialty

        // Set imagem
        Glide.with(context)
            .load(doctors[position].image)
            .into(binding.itemListviewDoctorImg)

        // Recuperar CardView
        val cardView = binding.itemCardviewDoctor
        cardView.setOnClickListener {
            val intent = Intent(context, ScheduleAppointmentFormActivity::class.java)

            val bundle = Bundle()
            bundle.putParcelable("doctor", doctors[position])
            bundle.putParcelable("patient", patient)

            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        return binding.root
    }
}