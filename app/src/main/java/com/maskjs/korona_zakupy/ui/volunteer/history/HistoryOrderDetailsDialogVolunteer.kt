package com.maskjs.korona_zakupy.ui.volunteer.history

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.ui.volunteer.VolunteerOrdersListAdapter
import com.maskjs.korona_zakupy.utils.Interfaces.IOrderDetailsDialog
import kotlinx.android.synthetic.main.history_order_details_popup_volunteer.view.*
import kotlin.properties.Delegates

class HistoryOrderDetailsDialogVolunteer(
    private val adapter: VolunteerOrdersListAdapter
): IOrderDetailsDialog {

    lateinit var alertDialog: AlertDialog
    lateinit var dialogView: View
    private lateinit var productsListView: ListView
    private lateinit var dateTextView: TextView
    private lateinit var addressTextView: TextView

    var orderId by Delegates.notNull<Long>()
    private var address: String? = null
    private var productsAdapter: ArrayAdapter<String>? = null
    private var orderDate: String? = null


    override fun initialize(context: Context) {
        this.dialogView = LayoutInflater.from(context).inflate(R.layout.history_order_details_popup_volunteer, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle(R.string.order_details)

        this.alertDialog = builder.show()

        this.productsListView = dialogView.productsVolunteerHistoryLV
        this.addressTextView = dialogView.addressVolunteerHistoryTV
        this.dateTextView = dialogView.dateVolunteerHistoryTV
    }


    override fun setOrderDetails(position: Int, context: Context) {

        getOrderDetails(position, context)

        addressTextView.text = address

        dateTextView.text = orderDate

        productsListView.adapter = productsAdapter
    }

    override fun getOrderDetails(position: Int, context: Context) {

        this.address = adapter
            .getAddress(position)

        this.orderDate = adapter
            .getOrderDate(position)

        this.productsAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            adapter
                .getProducts(position)
        )

        this.orderId = adapter.getOrderId(position).toLong()
    }

}