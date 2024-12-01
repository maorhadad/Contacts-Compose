package com.hadadas.contacts.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hadadas.contacts.data.ContactsRepositoryImpl
import com.hadadas.contacts.presentation.ui.screens.ContactsScreen
import com.hadadas.contacts.presentation.ui.screens.PermissionRationaleDialog
import com.hadadas.contacts.presentation.ui.theme.ContactsTheme
import com.hadadas.contacts.presentation.viewmodel.ContactsViewModel
import com.hadadas.contacts.presentation.viewmodel.ContactsViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val contentResolver = contentResolver
        setContent {
            ContactsTheme {
                Surface {
                    ContactsApp(
                        viewModel = viewModel(
                            factory = ContactsViewModelFactory(
                                ContactsRepositoryImpl(contentResolver)
                            )
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun ContactsApp(viewModel: ContactsViewModel) {
        val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
        val contacts by viewModel.filteredContacts.collectAsState()
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
                onPermissionGranted = { viewModel.fetchContacts() },
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
            onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
            onRefresh = {
                if (checkContactPermission()) {
                    viewModel.fetchContacts()
                } else {
                    showPermissionDeniedMessage()
                }
            }
        )
    }


    private fun checkContactPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionDeniedMessage() {
        Toast.makeText(this, "Permission denied. Cannot refresh contacts.", Toast.LENGTH_SHORT)
            .show()
    }
}



