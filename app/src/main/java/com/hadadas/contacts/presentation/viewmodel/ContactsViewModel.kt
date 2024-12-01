package com.hadadas.contacts.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadadas.contacts.domain.Contact
import com.hadadas.contacts.domain.ContactsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContactsViewModel(private val repository: ContactsRepository) : ViewModel() {

    init {
        Log.d("ContactsApp", "ContactsViewModel: init")
    }
    private val _searchQuery = MutableStateFlow("") // StateFlow for the search query
    val searchQuery: StateFlow<String> = _searchQuery

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList()) // StateFlow for contacts
    val contacts: StateFlow<List<Contact>> = _contacts

    val filteredContacts: StateFlow<List<Contact>> = _searchQuery
        .combine(_contacts) { query, contacts ->
            if (query.isEmpty()) {
                contacts
            } else {
                contacts.filter {
                    it.name?.contains(query, ignoreCase = true) == true ||
                            it.phoneNumber?.contains(query) == true
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun fetchContacts() {
        viewModelScope.launch {
            try {
                val fetchedContacts = repository.getContacts()
                _contacts.value = fetchedContacts
            } catch (e: Exception) {
                // Handle the error, e.g., show a message to the user
                _contacts.value = emptyList() // or keep the previous state
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}