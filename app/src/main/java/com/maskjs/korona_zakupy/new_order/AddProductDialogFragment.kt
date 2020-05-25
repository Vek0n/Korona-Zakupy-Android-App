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
    private lateinit var addProductDialogBuilder : AlertDialog.Builder
    private val addProductDialogViewModel: AddProductDialogViewModel by viewModels{
        AddProductDialogViewModelFactory(resources.getStringArray(R.array.dialog_number_picker_values),
            resources.getStringArray(R.array.dialog_number_picker_unit)) }
   // private lateinit var addProductDialogViewModel: AddProductDialogViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onAddProductClickListener = (context as? OnAddProductClickListener)
       // addProductDialogViewModel = AddProductDialogViewModel(resources.getStringArray(R.array.dialog_number_picker_values),
        //    resources.getStringArray(R.array.dialog_number_picker_unit))

        if(onAddProductClickListener == null)
            throw ClassCastException("Error!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.layoutInflater?.let { setLayoutBinding(it,  null)}
        setProductObserver()

        addProductDialogBuilder = activity?.let { AlertDialog.Builder(it) }!! // could be Error ?
        addProductDialogBuilder.setView(dialogBinding.root)
        addProductDialogBuilder.setTitle(getString(R.string.text_view_add_new_product)) //getString(R.string.text_view_add_new_product)
        addProductDialogBuilder.setPositiveButton(getString(R.string.dialog_positive_button_text)) { dialog, xd ->
            onPositiveDialogButtonClickedListener() }
        addProductDialogBuilder.setNegativeButton(getString(R.string.dialog_negative_button_text)){_,_-> }
        return addProductDialogBuilder.create()
    }

    private fun onPositiveDialogButtonClickedListener(){
            onAddProductClickListener?.addProduct(addProductDialogViewModel)
    }


    interface OnAddProductClickListener{
        fun addProduct(sendAddProductDialogViewModel: AddProductDialogViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          //  setLayoutBinding(inflater, container)
           // setProductObserver()

            return dialogBinding.root
        }

    private fun setLayoutBinding(inflater: LayoutInflater,container: ViewGroup?){
        dialogBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_dialog_add_product,container,false)
        dialogBinding.addProductDialogViewModel = addProductDialogViewModel
    }

    private fun setProductObserver(){
        addProductDialogViewModel.productTextInputLayout.textContent.observe(this, Observer {
           if( addProductDialogViewModel.checkValidation(mapOf(Pair("emptyError",getString(R.string.global_empty_field_error))))){
               dialogBinding.dialogLayoutTextInputProductName.error = getString(R.string.global_empty_field_error)
           }
            else{
               dialogBinding.dialogLayoutTextInputProductName.error = null
           }
        })
    }

    }

