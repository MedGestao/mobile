package br.ifal.med_gestao.util

import android.content.Context
import android.widget.Toast

class Notification {
    companion object{
        fun notification(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}