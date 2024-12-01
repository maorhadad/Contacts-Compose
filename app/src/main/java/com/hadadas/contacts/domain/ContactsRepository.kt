package com.hadadas.contacts.domain

interface  ContactsRepository {
    suspend fun getContacts(): List<Contact>
}