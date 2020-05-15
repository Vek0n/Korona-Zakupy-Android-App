package com.maskjs.korona_zakupy.helpers

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout


class DataBindingAdapters {

    companion object {
        @JvmStatic
        @BindingAdapter("app:errorText")
        fun bindErrorText(textInputLayout: TextInputLayout, errorText: String?) {
            textInputLayout.error = errorText
        }
    }
}