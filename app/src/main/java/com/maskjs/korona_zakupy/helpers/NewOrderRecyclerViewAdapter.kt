package com.maskjs.korona_zakupy.helpers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.ProductDto

class NewOrderRecyclerViewAdapter(private val products:List<ProductDto>) : RecyclerView.Adapter<NewOrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewOrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_new_order,parent,false)
        return NewOrderViewHolder(view)
    }

    override fun getItemCount(): Int {
       return products.size
    }

    override fun onBindViewHolder(holder: NewOrderViewHolder, position: Int) {
      holder.bind(products[position])
    }
}