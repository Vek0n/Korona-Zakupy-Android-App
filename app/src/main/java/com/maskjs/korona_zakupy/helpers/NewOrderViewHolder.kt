package com.maskjs.korona_zakupy.helpers

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.ProductDto

class NewOrderViewHolder( val view : View) : RecyclerView.ViewHolder(view) {
    private val product =  view.findViewById(R.id.text_view_product) as TextView
    private val quantity =  view.findViewById(R.id.text_view_quantity) as TextView

    fun  bind(product: ProductDto){
        this.product.text = product.product
        this.quantity.text = product.quantity
    }
}