package com.example.webmoments.util

import android.view.WindowManager
import android.app.Activity
import android.content.Context
import android.support.design.widget.TextInputLayout
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


class FieldValidatorUtil(val context: Context) {

    fun isEditTextFilled(editText: EditText, textInputLayout: TextInputLayout, message: String): Boolean {
        val value = editText.text.toString().trim { it <= ' ' }
        if (value.isEmpty()) {
            textInputLayout.error = message
            hideKeyboardFrom(editText)
            return false
        }

        textInputLayout.isErrorEnabled = false
        return true
    }

    private fun hideKeyboardFrom(view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}