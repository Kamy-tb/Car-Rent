package com.example.tp4_1

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.widget.Toast

class SmsSentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                // SMS envoyé avec succès
                Toast.makeText(context, "SMS envoyé", Toast.LENGTH_SHORT).show()
            }
            SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                // échec générique
                Toast.makeText(context, "Echec de l'envoi du SMS", Toast.LENGTH_SHORT).show()
            }
            SmsManager.RESULT_ERROR_NO_SERVICE -> {
                // Pas de service
                Toast.makeText(context, "Pas de service", Toast.LENGTH_SHORT).show()
            }
            SmsManager.RESULT_ERROR_NULL_PDU -> {
                // PDU null
                Toast.makeText(context, "PDU null", Toast.LENGTH_SHORT).show()
            }
            SmsManager.RESULT_ERROR_RADIO_OFF -> {
                // Radio éteinte
                Toast.makeText(context, "Radio éteinte", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
