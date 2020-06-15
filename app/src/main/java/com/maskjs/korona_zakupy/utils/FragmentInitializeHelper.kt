package com.maskjs.korona_zakupy.utils

import android.content.Context
import android.view.View

data class FragmentInitializeHelper(
    val root: View,
    val userId: String,
    val token: String,
    val context: Context
) {
}