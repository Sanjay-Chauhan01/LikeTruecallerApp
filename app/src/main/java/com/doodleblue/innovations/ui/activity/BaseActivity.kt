package com.doodleblue.innovations.ui.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.doodleblue.innovations.BuildConfig
import timber.log.Timber

open class BaseActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    /***
     * This method to use Create or Show progress dialog
     * @param msg set message to progress dialog
     */
    fun showProgressDialog(msg: String?) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
            progressDialog!!.setMessage(msg)
        }
        progressDialog!!.setCancelable(false)
        if (!progressDialog!!.isShowing) progressDialog!!.show()
    }

    /***
     * This method terminate the progress dialog
     */
    fun dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog!!.isShowing) progressDialog!!.dismiss()
        } catch (e: Exception) {
            Timber.e("Error : %s", e.localizedMessage)
        } finally {
            progressDialog = null
        }
    }

    /***
     * This method use for displaying toast message
     * @param message set Message to show a toast
     */
    fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}