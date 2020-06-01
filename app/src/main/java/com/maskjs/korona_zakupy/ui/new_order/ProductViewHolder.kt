package com.maskjs.korona_zakupy.ui.new_order

import android.view.ContextMenu
import android.view.View
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.ProductDto
import com.maskjs.korona_zakupy.databinding.ViewHolderProductBinding

class ProductViewHolder(val view : View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {
    private var productViewHolder : ViewHolderProductBinding = DataBindingUtil.bind(view)!!
    private val product =  productViewHolder.textViewProduct
    private val quantity = productViewHolder.textViewQuantity
    private val openMenus: CardView = view.findViewById(R.id.card_view) as CardView
    private lateinit var productClickListener: NewOrderViewModel.OnProductClickListener

    fun  bind(product: ProductDto, productClickListener: NewOrderViewModel.OnProductClickListener) {

        this.productClickListener = productClickListener
        this.product.text = product.product
        this.quantity.text = product.quantity + " " + product.unit

        view.setOnClickListener {
            if (product.clickingCanAddNewProduct)
                this.productClickListener.onProductClicked(product)
            else
                openMenus.setOnCreateContextMenuListener(this)
        }


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