package com.maskjs.korona_zakupy.volunteer_ui.active

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.*
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
import kotlinx.android.synthetic.main.fragment_active_orders.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient


class ActiveOrdersFragment : Fragment() {

    private lateinit var activeOrdersViewModel: ActiveOrdersViewModel
    private  lateinit var listView: ListView
    private lateinit var adapterOrders: OrdersListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activeOrdersViewModel =
            ViewModelProviders.of(this).get(ActiveOrdersViewModel::class.java)

        val context = activity
        val root = inflater.inflate(R.layout.fragment_active_orders, container, false)

        listView = root.findViewById(R.id.listViewActiveOrders) as ListView

        CoroutineScope(IO).launch {
            loadActiveOrders()
        }

        listView.setOnItemClickListener { _, _, position, _ ->

            val dialogView = LayoutInflater.from(context).inflate(R.layout.active_order_details_popup, null)
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

            dialogView.Ok_button.setOnClickListener {
                alertDialog.dismiss()
                //TODO
            }
        }

        return root
    }

    private suspend fun loadActiveOrders(){
        setListViewAdapterOnMainThread(
            getDataFromRepository())
    }

    private suspend fun getDataFromRepository(): ArrayList<GetOrderDto>{
        return OrderRepository<GetOrderDto>(OrderDao(OkHttpClient()))
            .getAllOrdersOfUser("dc4d373d-f329-4b4d-afd9-0903520d86d6")
    }

    private suspend fun setListViewAdapterOnMainThread(input: ArrayList<GetOrderDto>){
        withContext(Main){
            adapterOrders = OrdersListAdapter(activity, input)
            listView.adapter = adapterOrders
        }
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////
//        val productslist = ArrayList<String>()
//        productslist.add("Harnaś")
//        productslist.add("Żubr")
//        productslist.add("Kustosz")
//
//        val userInfo = ArrayList<UsersInfoModel>()
//        val user1 = UsersInfoModel(
//            userId = "123",
//            address = "Kwiatowe 3",
//            name = "Szymon",
//            firstName = "Szymon",
//            lastName = "Kaczmarek",
//            rating = 4,
//            photoDirectory = "https://i.imgur.com/KMH51Kr.png"
//        )
//        userInfo.add(user1)
//        val order1 = GetOrderDto(
//            orderId = 1,
//            orderDate = "10.05.2020",
//            products = productslist,
//            isFinished = false,
//            isActive = true,
//            usersInfo = userInfo
//        )
//
//        val ordersList = ArrayList<GetOrderDto>()
//        ordersList.add(order1)
////////////////////////////////////////////////////////////////////////////////////////////////////

}
