package com.maskjs.korona_zakupy.helpers

import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maskjs.korona_zakupy.data.orders.ProductDto
import com.maskjs.korona_zakupy.databinding.ViewHolderProductBinding
import com.maskjs.korona_zakupy.viewmodels.new_order.NewOrderViewModel

class ProductViewHolder(val view : View) : RecyclerView.ViewHolder(view) {
    private var productViewHolder : ViewHolderProductBinding = DataBindingUtil.bind(view)!!
    private val product : TextView
    private val quantity : TextView

    init {
        product = productViewHolder.textViewProduct
        quantity = productViewHolder.textViewQuantity
    }

    fun  bind(product: ProductDto,productClickListener: NewOrderViewModel.OnProductClickListener) {
        this.product.text = product.product
        this.quantity.text = product.quantity
        if (product.canAddProduct) {
            view.setOnClickListener {
                productClickListener.onProductClicked(product)
            }
        }
    }
}