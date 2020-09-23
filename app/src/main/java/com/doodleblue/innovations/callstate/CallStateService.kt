package com.doodleblue.innovations.callstate

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import timber.log.Timber

class CallStateService : Service() {
    private var screenOffReceiver: CallStateBroadcastReceiver? = null

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        registerScreenOffReceiver()
    }

    override fun onDestroy() {
        unregisterReceiver(screenOffReceiver)
        screenOffReceiver = null
    }

    private fun registerScreenOffReceiver() {
        screenOffReceiver = object : CallStateBroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent
            ) {
                Timber.e("ACTION_SCREEN_OFF")

                val listener = InComingPhoneStateListener(context)

                this.context = context
                this.intent = intent
                val telephonyManager = context
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
            }
        }
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        registerReceiver(screenOffReceiver, filter)
    }
}