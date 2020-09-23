package com.doodleblue.innovations.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_data")
data class ContactData(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "contactId")
    var sourceId: Int = 0,
    @NonNull
    @ColumnInfo(name = "contact_name")
    var contactName: String = "",
    @NonNull
    @ColumnInfo(name = "contact_number")
    var contactNumber: String = ""
)