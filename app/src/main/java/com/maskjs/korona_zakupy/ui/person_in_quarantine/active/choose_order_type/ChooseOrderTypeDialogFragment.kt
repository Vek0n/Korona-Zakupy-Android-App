package com.maskjs.korona_zakupy.ui.person_in_quarantine.active.choose_order_type

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels

import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.FragmentDialogChooseOrderTypeBinding
import com.maskjs.korona_zakupy.ui.new_order.NewOrderActivity


class ChooseOrderTypeDialogFragment : DialogFragment() {

    private lateinit var layoutBinding: FragmentDialogChooseOrderTypeBinding
    private val chooseOrderTypeViewModel: ChooseOrderTypeViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        activity?.layoutInflater?.let { setLayoutBinding(it,  null)}

        setOnImageButtonClickListeners()
        val alertDialog = buildDialog()
        setOnTextViewClickListeners(alertDialog)

        return alertDialog
    }

    private fun setLayoutBinding(inflater: LayoutInflater,container: ViewGroup?){
        layoutBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_dialog_choose_order_type,container,false)
        layoutBinding.chooseOrderTypeViewModel = chooseOrderTypeViewModel
    }


    private fun setOnImageButtonClickListeners(){
        layoutBinding.chooseDog.setOnClickListener{
            chooseOrderTypeViewModel.chooseOrderType(ChooseOrderTypeViewModel.OrderType.DOG)
        }

        layoutBinding.chooseGrocery.setOnClickListener{
            chooseOrderTypeViewModel.chooseOrderType(ChooseOrderTypeViewModel.OrderType.GROCERY)
        }
        layoutBinding.chooseGroceryPlus.setOnClickListener{
            chooseOrderTypeViewModel.chooseOrderType(ChooseOrderTypeViewModel.OrderType.GROCERY18PLUS)
        }
        layoutBinding.choosePharmacy.setOnClickListener {
            chooseOrderTypeViewModel.chooseOrderType(ChooseOrderTypeViewModel.OrderType.PHARMACY)
        }
    }

    private fun buildDialog(): AlertDialog {
        val chooseOrderTypeDialogBuilder = activity?.let { AlertDialog.Builder(it) }!!
        chooseOrderTypeDialogBuilder.setView(layoutBinding.root)
        chooseOrderTypeDialogBuilder.setTitle(getString(R.string.choose_order_type_text))

        return chooseOrderTypeDialogBuilder.create()
    }

    private fun setOnTextViewClickListeners(
        alertDialog: AlertDialog) {
        layoutBinding.chooseOrderButtonCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        layoutBinding.chooseOrderButtonAccept.setOnClickListener {
           if(chooseOrderTypeViewModel.validateOrderType()){
                saveOrderType()
                goToNewOrderActivity()
           }
        }
    }

    private fun saveOrderType(){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit(){
            putString(R.string.new_order_type.toString(), chooseOrderTypeViewModel.orderType)
            commit()
        }
    }

    private fun goToNewOrderActivity(){
        val intent = Intent(context, NewOrderActivity::class.java)
         startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_choose_order_type, container, false)
    }

}
