package com.example.reactsdk.screen_recording_v1

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.util.PatternsCompat

import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern


/* The `isValidEmail()` function is an extension function for the `String` class in Kotlin. It is used
to check if a given string is a valid email address. */
fun String.isValidEmail(): Boolean {
    return try {
        !TextUtils.isEmpty(this) && PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()
    } catch (ex: NullPointerException) {
        false
    }
}

/* The `fun String.blank(): Boolean` is an extension function for the `String` class in Kotlin. It is
used to check if a given string is blank or empty. */
fun String.blank(): Boolean {
    return trim().isEmpty()
}

/* The `isPasswordValid()` function is an extension function for the `String` class in Kotlin. It is
used to check if a given string is a valid password based on certain criteria. */

//            + "(?=.*[a-z])(?=.*[A-Z])"
//            + "(?=.*[@#$%^&+=!*])"
//            + "(?=\\S+$).{6,40}$")
//
//    val pattern = Pattern.compile(expression)
//    val matcher = pattern.matcher(this)
//    return matcher.matches()


/**
 * The function "call" takes a string parameter and returns the path of a file if it exists, otherwise
 * it returns the original string.
 * 
 * @param p1 The parameter `p1` is a string representing a file path or a URI.
 * @return the path of a file specified by the input parameter `p1`. If the path is found, it is
 * returned. Otherwise, the original input parameter `p1` is returned.
 */

fun String.isValidPinCode(): Boolean {
    val regex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";

    val p: Pattern = Pattern.compile(regex)
    // If the pin code is empty
    // return false
    if (this == null) {
        return false
    }
    val m: Matcher = p.matcher(this)
    return m.matches()
}
