package com.maskjs.korona_zakupy.helpers

import android.view.View
import android.widget.ProgressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadingSpinner {
    suspend fun showLoadingDialog(progressBar: ProgressBar){
        withContext(Dispatchers.Main){
            progressBar.visibility = View.VISIBLE
        }
    }

    suspend fun hideLoadingDialog(progressBar: ProgressBar){
        withContext(Dispatchers.Main){
            progressBar.visibility = View.GONE
        }
    }
}