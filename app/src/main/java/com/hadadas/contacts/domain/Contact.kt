package com.hadadas.contacts.domain

data class Contact(
    val name: String?,
    val phoneNumber: String?,
    val photoUri: String? = null
)
