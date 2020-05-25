package com.maskjs.korona_zakupy.new_order

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.FragmentDialogAddProductBinding
import com.maskjs.korona_zakupy.helpers.AddProductDialogViewModelFactory
import com.maskjs.korona_zakupy.viewmodels.add_product_dialog.AddProductDialogViewModel


class AddProductDialogFragment : DialogFragment() {

    private  var onAddProductClickListener : OnAddProductClickListener? = null
    private lateinit var dialogBinding: FragmentDialogAddProductBinding
    private val addProductDialogViewModel: AddProductDialogViewModel by viewModels{
        AddProductDialogViewModelFactory(resources.getStringArray(R.array.dialog_number_picker_values),
            resources.getStringArray(R.array.dialog_number_picker_unit)) }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onAddProductClickListener = (context as? OnAddProductClickListener)

        if(onAddProductClickListener == null)
            throw ClassCastException("Error!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.layoutInflater?.let { setLayoutBinding(it,  null)}
        setProductObserver()

        val addProductDialog = buildDialog()

        setDialogClickListeners(addProductDialog)

        return addProductDialog
    }

    private fun setLayoutBinding(inflater: LayoutInflater,container: ViewGroup?){
        dialogBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_dialog_add_product,container,false)
        dialogBinding.addProductDialogViewModel = addProductDialogViewModel
    }

    private fun setProductObserver(){
        addProductDialogViewModel.productTextInputLayout.textContent.observe(this, Observer {
            if( !addProductDialogViewModel.checkValidation(mapOf(Pair("emptyError",getString(R.string.global_empty_field_error))))){
                dialogBinding.dialogLayoutTextInputProductName.error = getString(R.string.global_empty_field_error)
            }
            else{
                dialogBinding.dialogLayoutTextInputProductName.error = null
            }
        })
    }

    private fun setDialogClickListeners(addProductDialog: AlertDialog){
        dialogBinding.dialogAcceptAddProduct.setOnClickListener {
            onPositiveDialogButtonClickedListener(addProductDialog)
        }

        dialogBinding.dialogCancelAddProduct.setOnClickListener {
            dismiss()
        }
    }

    private fun buildDialog(): AlertDialog{
        val addProductDialogBuilder = activity?.let { AlertDialog.Builder(it) }!!
        addProductDialogBuilder.setView(dialogBinding.root)
        addProductDialogBuilder.setTitle(getString(R.string.text_view_add_new_product))

        return addProductDialogBuilder.create()
    }

    private fun onPositiveDialogButtonClickedListener(addProductDialog: AlertDialog){
        if(!addProductDialogViewModel.checkValidation(mapOf(Pair("emptyError",getString(R.string.global_empty_field_error))))) {
            dialogBinding.dialogLayoutTextInputProductName.error = getString(R.string.global_empty_field_error)
        }
        else{
            onAddProductClickListener?.addProduct(addProductDialogViewModel)
            addProductDialog.dismiss()
        }
    }


    interface OnAddProductClickListener{
        fun addProduct(sendAddProductDialogViewModel: AddProductDialogViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            return dialogBinding.root
    }
}

