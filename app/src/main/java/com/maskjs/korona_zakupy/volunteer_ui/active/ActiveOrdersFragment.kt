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
import com.maskjs.korona_zakupy.data.orders.UsersInfoModel
import kotlinx.android.synthetic.main.fragment_active_orders.*

class ActiveOrdersFragment : Fragment() {

    private lateinit var activeOrdersViewModel: ActiveOrdersViewModel
    private  lateinit var listView: ListView

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


////////////////////////////////////////////////////////////////////////////////////////////////////
        val productslist = ArrayList<String>()
        productslist.add("Harnaś")
        productslist.add("Żubr")
        productslist.add("Kustosz")

        val userInfo = ArrayList<UsersInfoModel>()
        val user1 = UsersInfoModel(
            userId = "123",
            address = "Kwiatowe 3",
            name = "Szymon",
            firstName = "Szymon",
            lastName = "Kaczmarek",
            rating = 4,
            photoDirectory = "https://i.imgur.com/KMH51Kr.png"
        )
        userInfo.add(user1)
        val order1 = GetOrderDto(
            orderId = 1,
            orderDate = "10.05.2020",
            products = productslist,
            isFinished = false,
            isActive = true,
            usersInfo = userInfo
        )

        val ordersList = ArrayList<GetOrderDto>()
        ordersList.add(order1)
////////////////////////////////////////////////////////////////////////////////////////////////////


        val adapter = ActiveOrdersListAdapter(context, ordersList)

        listView.adapter = adapter


        listView.setOnItemClickListener { _, _, position, _ ->

            val inflater:LayoutInflater = layoutInflater
            val view = inflater.inflate(R.layout.order_details_popup,null)

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
                adapter
                    .getProducts(position))


            productsListView.adapter = productsAdapter


            cancelButton.setOnClickListener{
                popupWindow.dismiss()
            }

            popupWindow.setOnDismissListener {
                Toast.makeText(context,"Popup closed",Toast.LENGTH_SHORT).show()
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
}
