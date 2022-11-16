package com.group.daily_forecast.utils

import android.widget.EditText

object Validator {

    fun EditText.validate(msg: String? = null): Boolean {
        return if (!this.text.isNullOrEmpty()) {
            true
        } else {
            this.error = msg ?: this.hint
            false
        }

    }
}