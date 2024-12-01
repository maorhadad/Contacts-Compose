package com.hadadas.contacts.data

import android.content.ContentResolver
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import com.hadadas.contacts.domain.Contact
import com.hadadas.contacts.domain.ContactsRepository

class ContactsRepositoryImpl(private val contentResolver: ContentResolver) : ContactsRepository {
    override suspend fun getContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val phoneNumbersSet = mutableSetOf<String>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Photo.PHOTO_URI
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace("+972", "0")
                val photoUri = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Photo.PHOTO_URI))
                val normalizedNumberNoCountryCode = phoneNumber.replaceCountryCode()
                val normalizedNumber = PhoneNumberUtils.normalizeNumber(normalizedNumberNoCountryCode)
                if (!name.isNullOrEmpty() && phoneNumbersSet.add(normalizedNumber)) {
                    contacts.add(Contact(name, normalizedNumber, photoUri))
                }
            }
        }
        return contacts
    }
}