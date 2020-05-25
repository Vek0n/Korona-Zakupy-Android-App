package com.maskjs.korona_zakupy

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import com.maskjs.korona_zakupy.data.orders.ProductDto
import com.maskjs.korona_zakupy.databinding.ActivityNewOrderBinding
import com.maskjs.korona_zakupy.new_order.AddProductDialogFragment
import com.maskjs.korona_zakupy.viewmodels.add_product_dialog.AddProductDialogViewModel
import com.maskjs.korona_zakupy.viewmodels.new_order.NewOrderViewModel
import kotlinx.coroutines.*
import java.lang.Exception

class NewOrderActivity : AppCompatActivity(), NewOrderViewModel.OnProductClickListener,
AddProductDialogFragment.OnAddProductClickListener{
    private lateinit var layoutDataBinding: ActivityNewOrderBinding
    private val newOrderViewModel : NewOrderViewModel by viewModels()
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_new_order)
        setLayoutDataBinding()
        setOnClickListeners()
    }
    private fun setLayoutDataBinding(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        newOrderViewModel.initializeRecyclerView(getString(R.string.text_view_add_new_product),this)
        layoutDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_new_order)
        layoutDataBinding.recyclerView.adapter = newOrderViewModel.productRecyclerViewAdapter
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

    private fun getUserId(): String?{
        return  sharedPreferences?.getString(R.string.user_id_key.toString(),"")
    }

    private fun getUserToken(): String?{

        return  sharedPreferences?.getString(R.string.user_token_key.toString(),"")
    }

    private fun getNewOrderType(): String?{
        val editor= sharedPreferences?.edit(){
            putString(R.string.new_order_type.toString(), "Grocery")
            commit()
        }
        return sharedPreferences?.getString(R.string.new_order_type.toString(),"Dog")
    }

    private fun goToPersonInQuarantineActivity(){
        val intent = Intent(this, PersonInQuarantineActivity::class.java)
        startActivity(intent)
    }

    private  fun showToast(toastMessage: String){
        val toast = Toast.makeText(this, toastMessage,Toast.LENGTH_SHORT )
        toast.show()
    }

    override fun onProductClicked(productDto: ProductDto) {
        showProductDialog()
    }

    private fun showProductDialog(){
        val addProductDialogFragment = AddProductDialogFragment()
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
        val addProductDialogFragment = AddProductDialogFragment.newInstance(product.first, product.second, product.third)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null)
        {
            fragmentTransaction.remove(prev)
        }
        fragmentTransaction.addToBackStack(null)
        addProductDialogFragment.show(fragmentTransaction, "dialog")
    }

    override fun addProduct(sendAddProductDialogViewModel: AddProductDialogViewModel) {
        newOrderViewModel.addProduct(mapOf(Pair("emptyError",getString(R.string.global_empty_field_error))),
        sendAddProductDialogViewModel)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            1 ->{

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
