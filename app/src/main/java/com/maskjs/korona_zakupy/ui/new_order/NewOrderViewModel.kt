package com.maskjs.korona_zakupy.ui.new_order

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.PlaceOrderDto
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.ProductDto
import okhttp3.OkHttpClient

class NewOrderViewModel(initialText: String,
                        onProductClickListener: OnProductClickListener?,
                        private val orderRepository: OrderRepository<PlaceOrderDto>): ViewModel() {
    private var products : MutableList<ProductDto> = mutableListOf(
        ProductDto(
            initialText,
            "",
            "",
            false,
            true
        )
    )
    val productRecyclerViewAdapter =
        ProductRecyclerViewAdapter(
            products,
            onProductClickListener!!
        )

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
        orderRepository.placeOrder(
            PlaceOrderDto(
                userId,
                orderType,
                joinQuantityAndUnit()
            ),token)
    }

    private fun joinQuantityAndUnit(): ArrayList<String>{
        val closedListOfProduct = ArrayList<String>()
        products.forEach() { p -> if(!p.clickingCanAddNewProduct)closedListOfProduct.add(p.product + " " + p.quantity ) }
        return closedListOfProduct

    }

    fun addProduct(addedProductDto: ProductDto){
            products.add(addedProductDto)
            products.sortBy { p -> p.clickingCanAddNewProduct }
            productRecyclerViewAdapter.notifyDataSetChanged()
    }

    fun deleteProduct(position: Int){
        products.removeAt(position)
        productRecyclerViewAdapter.notifyDataSetChanged()
    }

    fun editProduct(editedProductDto: ProductDto){
        products.find { p -> p.isSendToEdit }?.edit(editedProductDto)
        productRecyclerViewAdapter.notifyDataSetChanged()
    }

    fun intendToEdit(position: Int){
        products[position].isSendToEdit = true
    }

    fun getEditedProduct(position: Int) : Triple<String, String, String> {
        val editedProduct = products[position]
        val productName = editedProduct.product
        val quantity =editedProduct.quantity
        val unit = editedProduct.unit
        return  Triple(productName, quantity, unit )
    }

    interface OnProductClickListener{
        fun onProductClicked(productDto: ProductDto)
    }
}