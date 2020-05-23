package com.maskjs.korona_zakupy.viewmodels.new_order

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.orders.ProductDto
import com.maskjs.korona_zakupy.helpers.NewOrderRecyclerViewAdapter
import com.maskjs.korona_zakupy.viewmodels.add_product_dialog.AddProductDialogViewModel

class NewOrderViewModel(): ViewModel() {
    val recyclerViewAdapter : NewOrderRecyclerViewAdapter
    lateinit var addProductDialogViewModel: AddProductDialogViewModel
    private val products : MutableList<ProductDto> = mutableListOf(ProductDto("Add new Product",""))

    fun initializeAddProductViewModel(quantity : Array<String>, unit : Array<String>){
        addProductDialogViewModel = AddProductDialogViewModel(quantity,unit)
    }

    fun addProduct(errorMessages: Map<String, String>){
        if(addProductDialogViewModel.checkValidation(errorMessages))
            addProductToList()
    }

    private fun addProductToList(){
        products.add(addProductDialogViewModel.getProductDto())
        recyclerViewAdapter.notifyDataSetChanged()
    }

    fun validateProduct(errorMessages: Map<String,String>){
        addProductDialogViewModel.validate(errorMessages)
    }
    init {
        recyclerViewAdapter = NewOrderRecyclerViewAdapter(products)
    }

}