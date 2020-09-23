package com.doodleblue.innovations.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.doodleblue.innovations.R
import com.doodleblue.innovations.callstate.CallStateService
import com.doodleblue.innovations.databinding.ActivityMainBinding
import com.doodleblue.innovations.viewmodel.ContactViewModel
import com.newsapp.constants.TAG_ASK_PERMISSION
import permissions.dispatcher.*
import timber.log.Timber


@RuntimePermissions
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactViewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainActivity = this
        binding.lifecycleOwner = this

        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        initObserver()

        onContactPermissionGrantedWithPermissionCheck()
    }

    /***
     * This method is use to initialize observer
     */
    private fun initObserver() {
        contactViewModel.strToastMessage.observe(this, Observer { message -> showToast(message) })
        contactViewModel.isShowProgressDialog.observe(this, Observer { isShow ->
            if (isShow) {
                showProgressDialog(getString(R.string.lbl_please_wait))
            } else {
                dismissProgressDialog()
            }
        })
        contactViewModel.notifyActivity.observe(this, Observer {
            if (it == TAG_ASK_PERMISSION) {
                onContactPermissionGrantedWithPermissionCheck()
            }
        })
        contactViewModel.isShowProgressDialog.observe(this, Observer {
            val service = Intent(this, CallStateService::class.java)
            startService(service)
        })
    }

    @NeedsPermission(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG
    )
    fun onContactPermissionGranted() {
        contactViewModel.isPermissionGranted.postValue(true)
        Timber.e("onContactPermissionGranted")
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 1234)
        }
    }

    @OnPermissionDenied(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG
    )
    fun onContactPermissionDenied() {
        showToast("Contact Permission Denied")
    }

    @OnShowRationale(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG
    )
    fun showRationaleForPermission(request: PermissionRequest) {
        request.proceed()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}