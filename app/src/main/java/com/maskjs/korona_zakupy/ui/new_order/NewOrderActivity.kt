package com.maskjs.korona_zakupy.ui.new_order

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.ProductDto
import com.maskjs.korona_zakupy.databinding.ActivityNewOrderBinding
import com.maskjs.korona_zakupy.ui.base.UserBaseActivity
import com.maskjs.korona_zakupy.utils.NewOrderViewModelFactory
import com.maskjs.korona_zakupy.ui.new_order.add_product_dialog.AddProductDialogFragment
import com.maskjs.korona_zakupy.ui.person_in_quarantine.PersonInQuarantineActivity
import kotlinx.coroutines.*
import java.lang.Exception

class NewOrderActivity : UserBaseActivity(), NewOrderViewModel.OnProductClickListener,
AddProductDialogFragment.OnAddProductClickListener, AddProductDialogFragment.OnEditProductClickListener{
    private lateinit var layoutDataBinding: ActivityNewOrderBinding
    private val newOrderViewModel : NewOrderViewModel by viewModels(){
        NewOrderViewModelFactory("Add Product",this)
    } //resources.getString(R.string.text_view_add_new_product) -> EXCEPTION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_new_order)
        setLayoutDataBinding()
        setOnClickListeners()
    }

    private fun setLayoutDataBinding(){
        layoutDataBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_new_order
        )
        layoutDataBinding.newOrderViewModel = newOrderViewModel
    }

    private fun setOnClickListeners(){
        layoutDataBinding.floatingActionButton2.setOnClickListener{
            placeOrder(getUserId(),getUserToken(),getNewOrderType())
        }
    }

    private fun placeOrder(userId: String?,token:String?,orderType: String?){
        CoroutineScope(Dispatchers.IO).launch {
            try{
               if(newOrderViewModel.tryPlaceOrder(userId?:"null", token?:"null", orderType?:"null"))
                   goToPersonInQuarantineActivity()
                else {
                   withContext(Dispatchers.Main) {
                       showToast(getString(R.string.toast_add_order_validation))
                   }
               }
            }catch (ex : Exception){
                withContext(Dispatchers.Main){
                    showToast(getString(R.string.toast_add_order_error))
                }
            }
        }
    }

    private  fun showToast(toastMessage: String){
        val toast = Toast.makeText(this, toastMessage,Toast.LENGTH_SHORT )
        toast.show()
    }

    override fun onProductClicked(productDto: ProductDto) {
        showProductDialog()
    }

    private fun showProductDialog(){
        val addProductDialogFragment =
            AddProductDialogFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null)
        {
            fragmentTransaction.remove(prev)
        }
        fragmentTransaction.addToBackStack(null)
        addProductDialogFragment.show(fragmentTransaction, "dialog")
    }

    private fun showEditProductDialog(
        product: Triple<String, String, String>){
        val addProductDialogFragment = AddProductDialogFragment.dialogToEdit(product.first, product.second, product.third)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null)
        {
            fragmentTransaction.remove(prev)
        }
        fragmentTransaction.addToBackStack(null)
        addProductDialogFragment.show(fragmentTransaction, "dialog")
    }

    override fun addProduct(addedProduct: ProductDto) {
        newOrderViewModel.addProduct(addedProduct)
    }

    override fun editProduct(editedProduct: ProductDto) {
        newOrderViewModel.editProduct(editedProduct)
    }
    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            1 ->{
                newOrderViewModel.intendToEdit(item.groupId)
                showEditProductDialog(newOrderViewModel.getEditedProduct(item.groupId))
                true
            }
            2 ->{
                newOrderViewModel.deleteProduct(item.groupId)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}
