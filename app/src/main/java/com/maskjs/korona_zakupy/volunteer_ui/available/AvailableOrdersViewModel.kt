package com.maskjs.korona_zakupy.volunteer_ui.available

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AvailableOrdersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is available Fragment"
    }
    val text: LiveData<String> = _text
}