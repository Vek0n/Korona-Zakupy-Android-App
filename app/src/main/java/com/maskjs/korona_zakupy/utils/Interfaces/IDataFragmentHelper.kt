package com.maskjs.korona_zakupy.utils.Interfaces

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.GetOrderDto
import com.maskjs.korona_zakupy.utils.FragmentInitializeHelper

interface IDataFragmentHelper {
    fun initialize(inflater: LayoutInflater, container: ViewGroup?): FragmentInitializeHelper
    fun setItemClickListeners(userId: String, token: String){}
    fun setItemClickListeners(token: String){}
    suspend fun populateListView(userId: String, token: String, context: Context)
    suspend fun getDataFromRepository(userId: String, token: String): ArrayList<GetOrderDto>
    suspend fun handleNullData(data: ArrayList<GetOrderDto>)
    suspend fun setListViewAdapterOnMainThread(input: ArrayList<GetOrderDto>, context: Context)
    fun showOrderDetailsDialog(position: Int, userId: String, token: String){}
    fun showOrderDetailsDialog(position: Int, token: String){}
    fun setDialogClickListeners(
        alertDialog: AlertDialog,
        dialogView: View,
        orderId: Long,
        token: String,
        userId: String
    ){}
    fun setDialogClickListeners(
        alertDialog: AlertDialog,
        dialogView: View,
        token: String,
        position: Int
    ){}





}