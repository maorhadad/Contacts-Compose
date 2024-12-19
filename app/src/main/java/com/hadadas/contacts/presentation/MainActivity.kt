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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
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
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding(),
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
    // Generate sample contacts
    fun generateSampleContacts(): List<Contact> {
        return listOf(
            Contact("Alice Johnson", "123-456-7890"),
            Contact("Bob Smith", "234-567-8901"),
            Contact("Charlie Brown", "345-678-9012"),
            Contact("Diana Prince", "456-789-0123"),
            Contact("Edward Norton", "567-890-1234"),
            Contact("Fiona Carter", "678-901-2345"),
            Contact("George Wilson", "789-012-3456"),
            Contact("Helen White", "890-123-4567"),
            Contact("Irene Foster", "901-234-5678"),
            Contact("Jack Daniels", "123-345-6789"),
            Contact("Karen Adams", "234-456-7890"),
            Contact("Liam Taylor", "345-567-8901"),
            Contact("Mona Stevens", "456-678-9012"),
            Contact("Nathan Drake", "567-789-0123"),
            Contact("Olivia Miller", "678-890-1234"),
            Contact("Paul Harris", "789-901-2345"),
            Contact("Quinn Johnson", "890-012-3456"),
            Contact("Rachel Green", "901-123-4567"),
            Contact("Sam Evans", "123-234-5678"),
            Contact("Tina Brown", "234-345-6789")
        )
    }

    @Preview(name = "MDPI", showBackground = false, device = "spec:width=480dp,height=800dp,dpi=160")
    @Composable
    fun PreviewContactsAppMDPI() {
        ContactsApp(
            contacts = generateSampleContacts(),
            searchQuery = "",
            fetchContacts = {},
            updateSearchQuery = {}
        )
    }

    @Preview(name = "HDPI", showBackground = false, device = "spec:width=480dp,height=800dp,dpi=240")
    @Composable
    fun PreviewContactsAppHDPI() {
        ContactsApp(
            contacts = generateSampleContacts(),
            searchQuery = "",
            fetchContacts = {},
            updateSearchQuery = {}
        )
    }

    @Preview(name = "XHDPI", showBackground = false, device = "spec:width=480dp,height=800dp,dpi=320")
    @Composable
    fun PreviewContactsAppXHDPI() {
        ContactsApp(
            contacts = generateSampleContacts(),
            searchQuery = "",
            fetchContacts = {},
            updateSearchQuery = {}
        )
    }

    @Preview(name = "XXHDPI", showBackground = false, device = "spec:width=480dp,height=800dp,dpi=480")
    @Composable
    fun PreviewContactsAppXXHDPI() {
        ContactsApp(
            contacts = generateSampleContacts(),
            searchQuery = "",
            fetchContacts = {},
            updateSearchQuery = {}
        )
    }

    @Preview(name = "16:9 Screen", showBackground = false, device = "spec:width=640dp,height=360dp")
    @Composable
    fun PreviewContactsApp16_9() {
        ContactsApp(
            contacts = generateSampleContacts(),
            searchQuery = "",
            fetchContacts = {},
            updateSearchQuery = {}
        )
    }

    @Preview(name = "7 Inch Tablet", showBackground = false, device = "spec:width=600dp,height=1024dp,dpi=240")
    @Composable
    fun PreviewContactsApp7InchTablet() {
        ContactsApp(
            contacts = generateSampleContacts(),
            searchQuery = "",
            fetchContacts = {},
            updateSearchQuery = {}
        )
    }

    @Preview(name = "10 Inch Tablet", showBackground = false, device = "spec:width=800dp,height=1280dp,dpi=240")
    @Composable
    fun PreviewContactsApp10InchTablet() {
        ContactsApp(
            contacts = generateSampleContacts(),
            searchQuery = "",
            fetchContacts = {},
            updateSearchQuery = {}
        )
    }
}




