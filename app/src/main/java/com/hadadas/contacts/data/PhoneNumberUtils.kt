package com.hadadas.contacts.data


fun String.replaceCountryCode(): String {
    return this.replace("+972", "0")
}
