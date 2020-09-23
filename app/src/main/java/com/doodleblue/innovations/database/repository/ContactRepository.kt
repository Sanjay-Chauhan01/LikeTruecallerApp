package com.doodleblue.innovations.database.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.doodleblue.innovations.model.ContactData
import com.doodleblue.innovations.database.dao.ContactDao

class ContactRepository(private val contactDAO: ContactDao) {

    val allContactData: LiveData<List<ContactData>> = contactDAO.getAllContactData()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertContactData(contactData: ContactData) {
        contactDAO.insertContactData(contactData)
    }

}