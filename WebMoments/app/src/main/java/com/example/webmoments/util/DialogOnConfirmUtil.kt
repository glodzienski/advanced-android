package com.example.webmoments.util

import android.app.AlertDialog
import android.content.Context

object DialogOnConfirmUtil {
    fun go(
        context: Context,
        title: String,
        message: String,
        onPositive: () -> Unit?,
        onNegative: () -> Unit?
    ) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Sim") { dialog, id ->
                onPositive()
            }
            .setNegativeButton("Não") { dialog, id ->
                onNegative()
            }

        builder.create().show()
    }
}