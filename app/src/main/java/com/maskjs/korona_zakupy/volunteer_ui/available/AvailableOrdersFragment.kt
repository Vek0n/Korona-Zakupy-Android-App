package com.maskjs.korona_zakupy.volunteer_ui.available

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

            val inflater: LayoutInflater = layoutInflater
            val view = inflater.inflate(R.layout.available_order_details_popup, null)

            val cancelButton = view.findViewById<Button>(R.id.cancel_button)
            val productsListView = view.findViewById<ListView>(R.id.products_list_view)

            val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )

            popupWindow.elevation = 10.0F

            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn


            val slideOut = Slide()
            slideOut.slideEdge = Gravity.TOP
            popupWindow.exitTransition = slideOut


            val productsAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_list_item_1,
                adapterOrders
                    .getProducts(position)
            )

            productsListView.adapter = productsAdapter

            cancelButton.setOnClickListener {
                popupWindow.dismiss()
            }

            popupWindow.setOnDismissListener {
                Toast.makeText(context, "Popup closed", Toast.LENGTH_SHORT).show()
            }

            TransitionManager.beginDelayedTransition(listViewAvailableOrders)
            popupWindow.showAtLocation(
                listViewAvailableOrders, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )
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