package com.maskjs.korona_zakupy.utils.Interfaces

import android.content.Context
import com.maskjs.korona_zakupy.ui.person_in_quarantine.QuarantineOrdersListAdapter

interface IOrderDetailsDialog {
    fun initialize(context: Context)
    fun setOrderDetails(position:Int, context: Context)
    fun getOrderDetails(position:Int, context: Context)
}