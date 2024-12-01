package com.hadadas.contacts.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hadadas.contacts.R
import com.hadadas.contacts.data.ContactsRepositoryImpl
import com.hadadas.contacts.domain.Contact
import com.hadadas.contacts.presentation.ui.screens.ContactsScreen
import com.hadadas.contacts.presentation.ui.screens.PermissionRationaleDialog
import com.hadadas.contacts.presentation.ui.theme.ContactsTheme
import com.hadadas.contacts.presentation.utils.PermissionsHandler
import com.hadadas.contacts.presentation.utils.checkContactPermission
import com.hadadas.contacts.presentation.viewmodel.ContactsViewModel
import com.hadadas.contacts.presentation.viewmodel.ContactsViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val contentResolver = contentResolver
        setContent {
            ContactsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: ContactsViewModel = viewModel(
                        factory = ContactsViewModelFactory(
                            ContactsRepositoryImpl(contentResolver)
                        )
                    )
                    val contacts by viewModel.filteredContacts.collectAsState()
                    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
                    ContactsApp(
                        contacts = contacts,
                        searchQuery = searchQuery,
                        fetchContacts = { viewModel.fetchContacts() },
                        updateSearchQuery = { viewModel.updateSearchQuery(it) }
                    )
                }
            }
        }
    }

    @Composable
    fun ContactsApp(
        contacts: List<Contact> = emptyList(),
        searchQuery: String = "",
        fetchContacts: () -> Unit,
        updateSearchQuery: (String) -> Unit
    ) {

        var showPermissionRationale by remember { mutableStateOf(false) }
        if (showPermissionRationale) {
            PermissionRationaleDialog(
                onDismiss = { showPermissionRationale = false },
                onSettingsRedirect = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }
                    startActivity(intent)
                }
            )
        } else {
            PermissionsHandler(
                onPermissionGranted = { fetchContacts() },
                onPermissionDenied = {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                        showPermissionRationale = true
                    }
                }
            )
        }

        ContactsScreen(
            contacts = contacts,
            searchQuery = searchQuery,
            onSearchQueryChanged = { updateSearchQuery(it) },
            onRefresh = {
                showToastMessage(getString(R.string.refreshing_contacts))
                if (checkContactPermission(this)) {
                    fetchContacts()
                } else {
                    showToastMessage(getString(R.string.permission_denied_cannot_refresh_contacts))
                }
            }
        )
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .show()
    }

    @Preview(showBackground = false)
    @Composable
    fun PreviewContactsApp() {
        val sampleContacts = listOf(
            Contact(name = "Alice Smith", phoneNumber = "123-456-7890"),
            Contact(name = "Bob Johnson", phoneNumber = "234-567-8901")
        )
        ContactsApp(
            contacts = sampleContacts,
            searchQuery = "",
            fetchContacts = {},
            updateSearchQuery = {}
        )
    }
}




