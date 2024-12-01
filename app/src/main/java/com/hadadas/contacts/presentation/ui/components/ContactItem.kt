package com.hadadas.contacts.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hadadas.contacts.R
import com.hadadas.contacts.domain.Contact

@Composable
fun ContactItem(contact: Contact) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberAsyncImagePainter(
                model = contact.photoUri,
                placeholder = painterResource(R.drawable.baseline_place_holder_profile_48), // Placeholder drawable
                error = painterResource(R.drawable.baseline_place_holder_profile_48) // Fallback drawable
            ),
            contentDescription = "Contact Image",
            modifier = Modifier
                .size(40.dp)
                .clip(androidx.compose.foundation.shape.CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center) {
            Text(text = contact.name ?: "No Name")
            Text(text = contact.phoneNumber ?: "No Number")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ContactItemPreview() {
    ContactItemPreviewInner(isDarkTheme = false)
}

@Preview(showBackground = true)
@Composable
fun ContactItemDarkPreview() {
    ContactItemPreviewInner(isDarkTheme = true)
}

@Composable
private fun ContactItemPreviewInner(isDarkTheme: Boolean) {
    val exampleContact = Contact("John Doe", "+123456789", "https://example.com/photo.jpg")

    Surface(
        color = if (isDarkTheme) androidx.compose.ui.graphics.Color.Black else androidx.compose.ui.graphics.Color.White
    ) {
        ContactItem(contact = exampleContact)
    }
}
