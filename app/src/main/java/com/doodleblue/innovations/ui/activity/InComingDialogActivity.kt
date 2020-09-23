package com.doodleblue.innovations.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.doodleblue.innovations.R
import com.doodleblue.innovations.TAG_MOBILE_NUMBER
import com.doodleblue.innovations.databinding.ActivityDialogIncomingCallBinding
import com.doodleblue.innovations.viewmodel.ContactViewModel
import timber.log.Timber

class InComingDialogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDialogIncomingCallBinding
    private lateinit var contactViewModel: ContactViewModel
    private var phoneNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("InComingDialogActivity onCreate")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dialog_incoming_call)
        binding.lifecycleOwner = this

        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        initObserver()
        manageIntent()

        binding.ivClose.setOnClickListener {
            finish()
        }
    }

    private fun manageIntent() {
        val intent = intent
        if (intent != null) {
            phoneNumber = intent.getStringExtra(TAG_MOBILE_NUMBER)
            binding.tvNumber.text = phoneNumber
            contactViewModel.allContactData
        }
    }

    /***
     * Initialize the observers.
     */
    private fun initObserver() {

        contactViewModel.allContactData.observe(this, Observer { data ->
            data.forEach {
                val contactNumber = it.contactNumber.replace(" ", "")
                    .replace("(", "")
                    .replace(")", "")
                Timber.e("Data : %s = %s", it.contactName, contactNumber)
                if (contactNumber == phoneNumber) {
                    updateCallerName(it.contactName)
                }
            }
        })
    }

    private fun updateCallerName(contactName: String) {
        binding.tvName.text = contactName
    }
}