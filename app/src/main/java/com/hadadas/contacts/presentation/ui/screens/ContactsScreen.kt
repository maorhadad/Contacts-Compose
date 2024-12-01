package com.hadadas.contacts.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
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
        Row(
            verticalAlignment = Alignment.CenterVertically, // Centers items vertically in the Row
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { onSearchQueryChanged(it) },
                modifier = Modifier
                    .weight(1f) // Fills the remaining space in the row
                    .padding(end = 8.dp) // Adds spacing between the TextField and the Button
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(24.dp)
                    ),
                placeholder = { Text(text = stringResource(R.string.search_contacts)) },
                shape = RoundedCornerShape(12.dp) // Makes the TextField rounded
            )

            Image(
                painter = painterResource(id = R.drawable.baseline_refresh_24),
                contentDescription = stringResource(R.string.refresh),
                modifier = Modifier
                    .size(48.dp) // Adjust size as needed
                    .clip(CircleShape) // Makes the image circular
                    .clickable { onRefresh() } // Add click functionality
                    .background(MaterialTheme.colorScheme.primary),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
        }

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
