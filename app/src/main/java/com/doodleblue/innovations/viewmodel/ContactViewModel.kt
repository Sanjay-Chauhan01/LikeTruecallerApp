package com.doodleblue.innovations.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doodleblue.innovations.database.AppDatabase
import com.doodleblue.innovations.database.repository.ContactRepository
import com.doodleblue.innovations.model.ContactData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    val strToastMessage = MutableLiveData<String>()
    val notifyActivity = MutableLiveData<String>()
    val isShowProgressDialog = MutableLiveData<Boolean>()
    val isPermissionGranted = MutableLiveData<Boolean>(false)
    val contactDataList = MutableLiveData<List<ContactData>>()

    private val contactRepository: ContactRepository

    val allContactData: LiveData<List<ContactData>>

    init {
        val contactDAO = AppDatabase.getDatabase(application).contactDataDao
        contactRepository = ContactRepository(contactDAO)
        allContactData = contactRepository.allContactData
    }

    fun insertNewsSource(contactData: ContactData) = viewModelScope.launch(Dispatchers.IO) {
        contactRepository.insertContactData(contactData)
    }
}