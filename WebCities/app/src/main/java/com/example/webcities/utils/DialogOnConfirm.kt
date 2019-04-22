package com.example.webcities.utils

import android.app.AlertDialog
import android.content.Context

class DialogOnConfirm {
    companion object {
        fun go (
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
}