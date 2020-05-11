package com.maskjs.korona_zakupy.volunteer_ui.active

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
import kotlinx.android.synthetic.main.fragment_active_orders.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
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

            val inflater: LayoutInflater = layoutInflater
            val view = inflater.inflate(R.layout.active_order_details_popup, null)

            val cancelButton = view.findViewById<Button>(R.id.Ok_button)
            val productsListView = view.findViewById<ListView>(R.id.products_list_view)
            val addressTextView = view.findViewById<TextView>(R.id.address_text_view)

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

            addressTextView.text = adapterOrders
                .getAddress(position)

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


            TransitionManager.beginDelayedTransition(listViewActiveOrders)
            popupWindow.showAtLocation(
                listViewActiveOrders, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )
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
