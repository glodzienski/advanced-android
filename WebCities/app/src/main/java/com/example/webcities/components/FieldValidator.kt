package com.example.webcities.components

import android.view.WindowManager
import android.app.Activity
import android.content.Context
import android.support.design.widget.TextInputLayout
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


class FieldValidator(val context: Context) {

    fun isEditTextFilled(editText: EditText, textInputLayout: TextInputLayout, message: String): Boolean? {
        val value = editText.text.toString().trim { it <= ' ' }
        if (value.isEmpty()) {
            textInputLayout.error = message
            hideKeyboardFrom(editText)
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }

        return true
    }

    fun isEditTextEmail(editText: EditText, textInputLayout: TextInputLayout, message: String): Boolean? {
        val value = editText.text.toString().trim { it <= ' ' }
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.error = message
            hideKeyboardFrom(editText)
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }

    private fun hideKeyboardFrom(view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}