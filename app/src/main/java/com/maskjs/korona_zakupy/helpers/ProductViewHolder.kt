package com.maskjs.korona_zakupy.helpers

import android.view.ContextMenu
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.ProductDto
import com.maskjs.korona_zakupy.databinding.ViewHolderProductBinding
import com.maskjs.korona_zakupy.viewmodels.new_order.NewOrderViewModel
import kotlinx.android.synthetic.main.view_holder_product.view.*

class ProductViewHolder(val view : View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener { //
    private var productViewHolder : ViewHolderProductBinding = DataBindingUtil.bind(view)!!
    private val product =  productViewHolder.textViewProduct
    private val quantity = productViewHolder.textViewQuantity
    private val viewHolderView = view.findViewById(R.id.linear_layout_product) as LinearLayout // bardzo brzydkie
    private lateinit var productClickListener: NewOrderViewModel.OnProductClickListener

    fun  bind(product: ProductDto,productClickListener: NewOrderViewModel.OnProductClickListener) {

        this.productClickListener = productClickListener
        this.product.text = product.product
        this.quantity.text = product.quantity

        view.setOnClickListener {
            if (product.canAddProduct)
                this.productClickListener.onProductClicked(product)
        }
        viewHolderView.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfp: ContextMenu.ContextMenuInfo?
    ) {
        val edit = menu?.add(adapterPosition,1,1,"Edit")
        val delete = menu?.add(adapterPosition,2,2,"Delete")
    }
}