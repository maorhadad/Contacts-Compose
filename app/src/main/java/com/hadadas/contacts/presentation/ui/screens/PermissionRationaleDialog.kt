package com.hadadas.contacts.presentation.ui.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hadadas.contacts.R


@Composable
fun PermissionRationaleDialog(
    onDismiss: () -> Unit,
    onSettingsRedirect: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.permission_required)) },
        text = {
            Text(stringResource(R.string.the_app_needs_access_to_your_contacts_to_display_them_please_grant_the_permission))
        },
        confirmButton = {
            TextButton(onClick = onSettingsRedirect) {
                Text(stringResource(R.string.open_settings))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
