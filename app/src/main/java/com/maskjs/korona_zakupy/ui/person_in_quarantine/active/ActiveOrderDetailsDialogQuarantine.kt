package com.maskjs.korona_zakupy.ui.person_in_quarantine.active

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.ui.person_in_quarantine.QuarantineOrdersListAdapter
import com.maskjs.korona_zakupy.utils.Interfaces.IOrderDetailsDialog
import kotlinx.android.synthetic.main.quarantine_active_order_details_popup.view.*
import kotlin.properties.Delegates

class ActiveOrderDetailsDialogQuarantine(
    private val adapter: QuarantineOrdersListAdapter
): IOrderDetailsDialog {

    lateinit var alertDialog: AlertDialog
    lateinit var dialogView: View
    private lateinit var productsListView: ListView
    private lateinit var acceptedByTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var ratingStar: ImageView

    var orderId by Delegates.notNull<Long>()
    private var acceptedByName: String? = null
    private var productsAdapter: ArrayAdapter<String>? = null
    private var rating: Double? = null
    private var orderDate: String? = null


    override fun initialize(context: Context){
        this.dialogView =
            LayoutInflater.from(context).inflate(R.layout.quarantine_active_order_details_popup, null)

        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle(R.string.order_details)

        this.alertDialog = builder.show()

        this.productsListView = dialogView.productsQuarantineActiveLV
        this.acceptedByTextView = dialogView.acceptedByQuarantineActiveTV
        this.dateTextView = dialogView.dateQuarantineActiveTV
        this.ratingTextView = dialogView.quarantineActiveRating
        this.ratingStar = dialogView.imageView8

    }


    override fun setOrderDetails(
        position: Int,
        context: Context
    ) {

        getOrderDetails(position, context)

        if (rating != null) {
            ratingTextView.visibility = View.VISIBLE
            ratingStar.visibility = View.VISIBLE
            ratingTextView.text = rating.toString()
        }

        productsListView.adapter = productsAdapter

        acceptedByTextView.text = acceptedByName

        dateTextView.text = orderDate
    }


    override fun getOrderDetails(
        position: Int,
        context: Context
    ) {
        this.productsAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            adapter
                .getProducts(position)
        )

        this.rating = adapter
            .getRating(position)

        this.acceptedByName = adapter
            .getFirstName(position)

        this.orderDate = adapter
            .getOrderDate(position)

        this.orderId = adapter
            .getOrderId(position).toLong()
    }
}