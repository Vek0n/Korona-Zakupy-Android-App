package com.maskjs.korona_zakupy.volunteer_ui.available

import android.app.AlertDialog
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
import com.maskjs.korona_zakupy.volunteer_ui.OrdersListAdapter
import kotlinx.android.synthetic.main.active_order_details_popup.view.*
import kotlinx.android.synthetic.main.available_order_details_popup.view.*
import kotlinx.android.synthetic.main.available_order_details_popup.view.address_text_view
import kotlinx.android.synthetic.main.available_order_details_popup.view.cancel_button
import kotlinx.android.synthetic.main.available_order_details_popup.view.date_text_view
import kotlinx.android.synthetic.main.available_order_details_popup.view.products_list_view
import kotlinx.android.synthetic.main.fragment_available_orders.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient

class AvailableOrdersFragment : Fragment() {

    private lateinit var availableOrdersViewModel: AvailableOrdersViewModel
    private lateinit var listView: ListView
    private lateinit var adapterOrders: OrdersListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        availableOrdersViewModel =
            ViewModelProviders.of(this).get(AvailableOrdersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_available_orders, container, false)
        val context = activity

        listView = root.findViewById(R.id.listViewAvailableOrders) as ListView

        CoroutineScope(Dispatchers.IO).launch {
            loadActiveOrders()
        }

        listView.setOnItemClickListener { _, _, position, _ ->

            val dialogView = LayoutInflater.from(context).inflate(R.layout.available_order_details_popup, null)
            val builder = AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("Order details")

            val alertDialog = builder.show()

            val productsListView = dialogView.products_list_view
            val addressTextView = dialogView.address_text_view
            val dateTextView = dialogView.date_text_view

            addressTextView.text = adapterOrders
                .getAddress(position)

            dateTextView.text = adapterOrders
                .getOrderDate(position)

            val productsAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_list_item_1,
                adapterOrders
                    .getProducts(position)
            )
            productsListView.adapter = productsAdapter

            dialogView.cancel_button.setOnClickListener {
                alertDialog.dismiss()
            }

            dialogView.accept_button.setOnClickListener {
                alertDialog.dismiss()
                //TODO
            }

        }

        return root
    }


    private suspend fun loadActiveOrders() {
        setListViewAdapterOnMainThread(
            getDataFromRepository()
        )
    }

    private suspend fun getDataFromRepository(): ArrayList<GetOrderDto> {
        return OrderRepository<GetOrderDto>(OrderDao(OkHttpClient()))
            .getAllOrdersOfUser("dc4d373d-f329-4b4d-afd9-0903520d86d6")
    }

    private suspend fun setListViewAdapterOnMainThread(input: ArrayList<GetOrderDto>) {
        withContext(Dispatchers.Main) {
            adapterOrders = OrdersListAdapter(activity, input)
            listView.adapter = adapterOrders
        }
    }

}