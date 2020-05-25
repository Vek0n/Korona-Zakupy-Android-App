package com.maskjs.korona_zakupy.data.orders

data class ProductDto(
    var product: String,
    var quantity: String,
    var unit: String,
    var isSendToEdit: Boolean = false,
    val clickingCanAddNewProduct: Boolean = false
    ) {

    fun edit(editedProductDto: ProductDto){
        product = editedProductDto.product
        quantity = editedProductDto.quantity
        unit  = editedProductDto.unit
        isSendToEdit = false
    }

}