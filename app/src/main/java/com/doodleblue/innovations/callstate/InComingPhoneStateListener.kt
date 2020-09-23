package com.doodleblue.innovations.callstate

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.telephony.PhoneStateListener
import com.doodleblue.innovations.TAG_MOBILE_NUMBER
import com.doodleblue.innovations.ui.activity.InComingDialogActivity
import timber.log.Timber

class InComingPhoneStateListener(var context: Context) :
    PhoneStateListener() {

    override fun onCallStateChanged(state: Int, phoneNumber: String) {
        super.onCallStateChanged(state, phoneNumber)

        Timber.e("MyPhoneListener $state   incoming no : $phoneNumber")

        if (state == 1) {
            Timber.e(" New Phone Call Event. InComing Number : $phoneNumber")
            showDialog(phoneNumber)
        }
    }

    private fun showDialog(phoneNumber: String) {
        Timber.e("showDialog")
        Handler().postDelayed(Runnable {
            val intent = Intent(context, InComingDialogActivity::class.java)
            intent.putExtra(TAG_MOBILE_NUMBER, phoneNumber)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }, 2000)
    }
}