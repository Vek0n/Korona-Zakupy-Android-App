package com.maskjs.korona_zakupy

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.maskjs.korona_zakupy.data.orders.ProductDto
import com.maskjs.korona_zakupy.databinding.ActivityNewOrderBinding
import com.maskjs.korona_zakupy.databinding.DialogAddProductBinding
import com.maskjs.korona_zakupy.viewmodels.new_order.NewOrderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.defaultSharedPreferences


class NewOrderActivity : AppCompatActivity(), NewOrderViewModel.OnProductClickListener {
    private lateinit var layoutDataBinding: ActivityNewOrderBinding
    private lateinit var addProductDialogDataBinding: DialogAddProductBinding
    private lateinit var addProductDialogBuilder: AlertDialog.Builder
    private val newOrderViewModel : NewOrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)
        setLayoutDataBinding()

        layoutDataBinding.floatingActionButton2.setOnClickListener{
            placeOrder(getUserId())
        }
    }
    private fun setLayoutDataBinding(){
        newOrderViewModel.initializeRecyclerView(getString(R.string.text_view_add_new_product),this)
        layoutDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_new_order)
        layoutDataBinding.recyclerView.adapter = newOrderViewModel.productRecyclerViewAdapter
    }

    private fun placeOrder(userId: String){
        CoroutineScope(Dispatchers.IO).launch {
            newOrderViewModel.placeOrder(userId)
        }

    }

    private fun getUserId(): String{
        val sharedPreferences = defaultSharedPreferences
        return  sharedPreferences.getString(R.string.user_id_key.toString(),"")
    }

    private fun showProductDialog(){
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
        addProductDialogBuilder.setTitle(getString(R.string.text_view_add_new_product))
        addProductDialogBuilder.setPositiveButton(getString(R.string.dialog_positive_button_text)) { dialog, _ -> onPositiveDialogButtonClickedListener()}
        addProductDialogBuilder.setNegativeButton(getString(R.string.dialog_negative_button_text)){_,_-> }
    }

    private fun showDialog(){
        addProductDialogBuilder.create().show()
    }

    private fun onPositiveDialogButtonClickedListener(){
       newOrderViewModel.addProduct(mapOf(Pair("emptyError",getString(R.string.global_empty_field_error))))
    }

    override fun onProductClicked(productDto: ProductDto) {
        showProductDialog()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater.inflate(R.menu.menu_product,menu)
    }
}
