package br.ifal.med_gestao.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import br.ifal.med_gestao.activity.AppointmentActivity
import br.ifal.med_gestao.databinding.ItemListAppointmentActivityBinding
import br.ifal.med_gestao.domain.AppointmentWithDoctor
import com.bumptech.glide.Glide
import java.time.format.DateTimeFormatter

class AppointmentAdapter(val context: Context, var appointments: List<AppointmentWithDoctor>) : BaseAdapter() {
    override fun getCount(): Int {
        return appointments.size
    }

    override fun getItem(position: Int): Any {
        return appointments[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun update(newData: List<AppointmentWithDoctor>) {
        appointments = newData
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
        val binding = ItemListAppointmentActivityBinding.inflate(inflater, parent, false)

        // Settando novos valores p/ views do Card
        binding.itemListviewDoctorName.text = appointments[position].doctor.name
        binding.itemListviewDoctorSpecialty.text = appointments[position].doctor.specialty

        binding.itemListviewDate.text = appointments[position].appointment.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        binding.itemListviewTime.text = appointments[position].appointment.time

        Glide.with(context)
            .load(appointments[position].doctor.image)
            .into(binding.itemListviewDoctorImg)

        val cardView = binding.itemCardviewAppointment
        cardView.setOnClickListener {
//            val intent = Intent(context, AppointmentActivity::class.java)
//
//            val bundle = Bundle()
//            bundle.putParcelable("appointment", appointments[position])
//
//            intent.putExtras(bundle)
//            context.startActivity(intent)
        }

        return binding.root
    }
}