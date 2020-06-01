package com.maskjs.korona_zakupy.ui.person_in_quarantine.active.choose_order_type


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels

import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.FragmentWalkingDogDescriptionDialogBinding
import com.maskjs.korona_zakupy.ui.person_in_quarantine.PersonInQuarantineActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class WalkingDogDescriptionDialogFragment : DialogFragment() {
    private val walkingDodDescriptionViewModel : WalkingDodDescriptionViewModel by viewModels()
    private lateinit var layoutBinding : FragmentWalkingDogDescriptionDialogBinding
    private var sharedPreferences: SharedPreferences? = null
    private var onAddOrder : OnAddOrder? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onAddOrder = context as? OnAddOrder
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setSharedPreferences()
        activity?.layoutInflater?.let { setLayoutBinding(it,  null)}


        val alertDialog = buildDialog()
        setOnButtonClickListeners(alertDialog)

        return alertDialog
    }

    private fun setSharedPreferences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private fun setLayoutBinding(inflater: LayoutInflater,container: ViewGroup?){
        layoutBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_walking_dog_description_dialog,container,false)
        layoutBinding.walkingDogDescriptionViewModel = walkingDodDescriptionViewModel
    }

    private fun buildDialog() : AlertDialog {
        val walkingDogDescriptionAlertDialogBuilder = activity?.let { androidx.appcompat.app.AlertDialog.Builder(it) }!!
        walkingDogDescriptionAlertDialogBuilder.setView(layoutBinding.root)
        walkingDogDescriptionAlertDialogBuilder.setTitle(getString(R.string.dog_describe_title))

        return walkingDogDescriptionAlertDialogBuilder.create()
    }

    private fun setOnButtonClickListeners(alertDialog: AlertDialog){
        layoutBinding.dogButtonCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        layoutBinding.dogButtonAccept.setOnClickListener {
           placeOrder(getUserId()!!,getUserToken()!!,getNewOrderType()!!)
        }
    }

    private fun placeOrder(userId: String,token:String,orderType: String){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                walkingDodDescriptionViewModel.placeOrder(userId,token,orderType)
                onAddOrder?.restart()

            }catch (ex : Exception){
                withContext(Dispatchers.Main){
                }
            }
        }
    }

    private fun getUserId(): String?{
        return  sharedPreferences?.getString(R.string.user_id_key.toString(),"")
    }

    private fun getUserToken(): String?{

        return  sharedPreferences?.getString(R.string.user_token_key.toString(),"")
    }

    private fun getNewOrderType(): String?{

        return sharedPreferences?.getString(R.string.new_order_type.toString(),"")
    }


    interface OnAddOrder{
        fun restart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_walking_dog_description_dialog, container, false)
    }
}

