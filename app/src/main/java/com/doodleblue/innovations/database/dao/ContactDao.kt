package com.doodleblue.innovations.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.doodleblue.innovations.model.ContactData

@Dao
interface ContactDao {

    @Insert
    fun insertContactData(contactData: ContactData)

    @Query("SELECT * from contact_data")
    fun getAllContactData(): LiveData<List<ContactData>>
}