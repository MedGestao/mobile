package br.ifal.med_gestao.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import br.ifal.med_gestao.databinding.RadioButtonItemBinding
import br.ifal.med_gestao.domain.DoctorSchedule
import java.util.UUID
import kotlin.random.Random

class ScheduleRadioButtonAdapter(val context: Context, var item: DoctorSchedule) {

    fun getView(
        parent: ViewGroup,
    ) {
        // Inflate c/ viewBinding
        // Inflar o Card dentro de um item do ListView
        val inflater = LayoutInflater.from(context)
        val binding = RadioButtonItemBinding.inflate(inflater, parent, false)

        if (item.period1.isNotBlank()) {
            binding.radioButtonItem.text = item.period1
            binding.radioButtonItem.id = Random.nextInt(0, 20) // Set a unique ID for each RadioButton
            parent.addView(binding.root)
        }

        val binding2 = RadioButtonItemBinding.inflate(inflater, parent, false)
        if (item.period2.isNotBlank()) {
            binding2.radioButtonItem.text = item.period2
            binding.radioButtonItem.id = Random.nextInt(0, 20)
            parent.addView(binding2.root)
        }

    }
}