package com.maskjs.korona_zakupy

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.maskjs.korona_zakupy.databinding.ActivityNewOrderBinding
import com.maskjs.korona_zakupy.databinding.DialogAddProductBinding
import com.maskjs.korona_zakupy.viewmodels.new_order.NewOrderViewModel


class NewOrderActivity : AppCompatActivity() {
    private lateinit var layoutDataBinding: ActivityNewOrderBinding
    private lateinit var addProductDialogDataBinding: DialogAddProductBinding
    private lateinit var addProductDialogBuilder: AlertDialog.Builder
    private val newOrderViewModel : NewOrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)
        setLayoutDataBinding()

        layoutDataBinding.floatingActionButton2.setOnClickListener{
            addProduct()
        }
    }
    private fun setLayoutDataBinding(){
        layoutDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_new_order)
        layoutDataBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        layoutDataBinding.recyclerView.adapter = newOrderViewModel.recyclerViewAdapter

    }

    private fun addProduct(){
        initializeDialogBinding()
        setProductObserver()
        buildDialog()
        showDialog()
    }

    private fun initializeDialogBinding(){
        newOrderViewModel.initializeAddProductViewModel(resources.getStringArray(R.array.dialog_number_picker_values),
        resources.getStringArray(R.array.dialog_number_picker_unit))
        addProductDialogDataBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.dialog_add_product,null,false)
        addProductDialogDataBinding.addProductDialogViewModel = newOrderViewModel.addProductDialogViewModel
    }

    private fun setProductObserver(){
        newOrderViewModel.addProductDialogViewModel.productTextInputLayout.textContent.observe(this, Observer {
            newOrderViewModel.validateProduct(mapOf(Pair("emptyError",getString(R.string.global_empty_field_error))))
        })
    }

    private fun buildDialog(){
        addProductDialogBuilder = AlertDialog.Builder(this)
        addProductDialogBuilder.setView(addProductDialogDataBinding.root)
        addProductDialogBuilder.setTitle("Add product:")
        addProductDialogBuilder.setPositiveButton("ADD") { dialog, _ -> onPositiveDialogButtonClickedListener()}
        addProductDialogBuilder.setNegativeButton("CANCEL"){_,_-> }
    }

    private fun showDialog(){
        addProductDialogBuilder.create().show()
    }

    private fun onPositiveDialogButtonClickedListener(){
       newOrderViewModel.addProduct(mapOf(Pair("emptyError",getString(R.string.global_empty_field_error))))
    }
}
