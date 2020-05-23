package com.maskjs.korona_zakupy.viewmodels.new_order

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
import com.maskjs.korona_zakupy.data.orders.PlaceOrderDto
import com.maskjs.korona_zakupy.data.orders.ProductDto
import com.maskjs.korona_zakupy.helpers.ProductRecyclerViewAdapter
import com.maskjs.korona_zakupy.viewmodels.add_product_dialog.AddProductDialogViewModel
import okhttp3.OkHttpClient

class NewOrderViewModel(): ViewModel() {
    lateinit var addProductDialogViewModel: AddProductDialogViewModel
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

    fun initializeAddProductViewModel(quantity : Array<String>, unit : Array<String>){
        addProductDialogViewModel = AddProductDialogViewModel(quantity,unit)
    }

    suspend fun placeOrder(userId : String){
        orderRepository.placeOrder(PlaceOrderDto(userId,joinQuantityAndUnit()))
    }

    suspend private fun joinQuantityAndUnit(): ArrayList<String>{
        val closedListOfProduct = ArrayList<String>()
        products.forEach() { p -> closedListOfProduct.add(p.product + p.quantity ) }
        return closedListOfProduct

    }

    fun addProduct(errorMessages: Map<String, String>){
        if(addProductDialogViewModel.checkValidation(errorMessages))
            addProductToList()
    }

    private fun addProductToList(){
        products.add(addProductDialogViewModel.getProductDto())
        products.sortBy { p -> p.canAddProduct}
        productRecyclerViewAdapter.notifyDataSetChanged()
    }

    fun validateProduct(errorMessages: Map<String,String>){
        addProductDialogViewModel.validate(errorMessages)
    }

    interface OnProductClickListener{
        fun onProductClicked(productDto: ProductDto)
    }
}