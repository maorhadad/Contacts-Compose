package com.hadadas.contacts.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hadadas.contacts.R
import com.hadadas.contacts.domain.Contact
import com.hadadas.contacts.presentation.ui.components.ContactItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    contacts: List<Contact>,
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit,
    onRefresh: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar
        TextField(
            value = searchQuery,
            onValueChange = { onSearchQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text(text = stringResource(R.string.search_contacts)) },
            shape = RoundedCornerShape(16.dp), // Makes the TextField rounded
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        )

        Button(
            onClick = onRefresh,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(stringResource(R.string.refresh))
        }

        // Contacts list
        if (contacts.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.no_contacts_found),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(contacts) { contact ->
                    ContactItem(contact)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewContactsScreen() {
    val sampleContacts = listOf(
        Contact(name = "John Doe", phoneNumber = "123456343490"),
        Contact(name = "Jane Smith", phoneNumber = "1234564545"),
        Contact(name = "Alice Johnson", phoneNumber = "1234567545")
    )

    ContactsScreen(
        contacts = sampleContacts,
        onRefresh = { /* Do nothing for preview */ },
        onSearchQueryChanged = { /* Do nothing for preview */ }
    )
}

@Preview
@Composable
fun PreviewEmptyContactsScreen() {
    val sampleContacts = emptyList<Contact>()

    ContactsScreen(
        contacts = sampleContacts,
        onRefresh = { /* Do nothing for preview */ },
        onSearchQueryChanged = { /* Do nothing for preview */ }
    )
}
