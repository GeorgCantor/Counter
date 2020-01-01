package com.georgcantor.counter.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.georgcantor.counter.R

fun Context.showDialog(
    message: CharSequence,
    function: () -> (Unit)
) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setNegativeButton(R.string.no) { _, _ -> }
        .setPositiveButton(R.string.yes) { _, _ -> function() }
        .create()
        .show()
}