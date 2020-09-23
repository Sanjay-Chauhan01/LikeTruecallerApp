package com.doodleblue.innovations.callstate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import timber.log.Timber


open class CallStateBroadcastReceiver : BroadcastReceiver() {
    var context: Context? = null
    var intent: Intent? = null

    override fun onReceive(context: Context, intent: Intent) {
        Timber.e("CallStateBroadcastReceiver : onReceive")


        val listener = InComingPhoneStateListener(context)

        this.context = context
        this.intent = intent
        val telephonyManager = context
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }
}