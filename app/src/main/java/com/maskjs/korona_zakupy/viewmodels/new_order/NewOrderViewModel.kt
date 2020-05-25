package com.maskjs.korona_zakupy.viewmodels.new_order

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
import com.maskjs.korona_zakupy.data.orders.PlaceOrderDto
import com.maskjs.korona_zakupy.data.orders.ProductDto
import com.maskjs.korona_zakupy.helpers.ProductRecyclerViewAdapter
import com.maskjs.korona_zakupy.viewmodels.add_product_dialog.AddProductDialogViewModel
import kotlinx.coroutines.*
import okhttp3.OkHttpClient

class NewOrderViewModel(): ViewModel() {
   // lateinit var addProductDialogViewModel: AddProductDialogViewModel
    lateinit var productRecyclerViewAdapter : ProductRecyclerViewAdapter
    private lateinit var onProductClickListener : OnProductClickListener
    private lateinit var products : MutableList<ProductDto>
    private val orderRepository = OrderRepository<PlaceOrderDto>(orderDao = OrderDao(OkHttpClient()))

    fun initializeRecyclerView(initialText: String,onProductClickListener: OnProductClickListener){
        products = mutableListOf(ProductDto(initialText,"",true))
        this.onProductClickListener = onProductClickListener
        onProductClickListener?.let {
            productRecyclerViewAdapter = ProductRecyclerViewAdapter(products, onProductClickListener)
        }
    }

//    fun initializeAddProductViewModel(quantity : Array<String>, unit : Array<String>){
//        addProductDialogViewModel = AddProductDialogViewModel(quantity,unit)
//    }

  suspend fun tryPlaceOrder(userId:String,token:String,orderType: String) : Boolean{
       if(!checkValidation())
           return false
       placeOrder(userId,token,orderType)

       return true
   }

    private fun checkValidation() : Boolean{
        return products.size >= 2
    }

    private suspend fun placeOrder(userId: String, token:String, orderType: String){
        orderRepository.placeOrder(PlaceOrderDto(userId,orderType,joinQuantityAndUnit()),token)
    }

    private fun joinQuantityAndUnit(): ArrayList<String>{
        val closedListOfProduct = ArrayList<String>()
        products.forEach() { p -> if(!p.canAddProduct)closedListOfProduct.add(p.product + " " + p.quantity ) }
        return closedListOfProduct

    }

//    fun addProduct(errorMessages: Map<String, String>){
//        if(addProductDialogViewModel.checkValidation(errorMessages))
//            addProductToList()
//    }

    fun addProduct(errorMessages: Map<String, String>, addProductDialogViewModel: AddProductDialogViewModel){
        if(addProductDialogViewModel.checkValidation(errorMessages))
            addProductToList(addProductDialogViewModel)
    }

    private fun addProductToList(addProductDialogViewModel: AddProductDialogViewModel){
        products.add(addProductDialogViewModel.getProductDto())
        products.sortBy { p -> p.canAddProduct}
        productRecyclerViewAdapter.notifyDataSetChanged()
    }

//    fun validateProduct(errorMessages: Map<String,String>){
//        addProductDialogViewModel.validate(errorMessages)
//    }

    interface OnProductClickListener{
        fun onProductClicked(productDto: ProductDto)
    }
}