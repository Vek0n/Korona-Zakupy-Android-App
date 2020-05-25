package com.maskjs.korona_zakupy.ui.new_order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.ProductDto

class ProductRecyclerViewAdapter(private val products: List<ProductDto>,
                                 private val productClickListener: NewOrderViewModel.OnProductClickListener) : RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_product,parent,false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
       return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
      holder.bind(products[position],productClickListener)
    }
}