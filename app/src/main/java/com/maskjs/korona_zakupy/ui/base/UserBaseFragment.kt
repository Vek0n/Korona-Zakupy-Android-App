package com.maskjs.korona_zakupy.ui.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.maskjs.korona_zakupy.R


abstract class UserBaseFragment : Fragment() {
    protected var onBackPress : OnBackPress? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onBackPress = context as? OnBackPress

    }

    interface OnBackPress{
       fun leaveApp()
    }
}
