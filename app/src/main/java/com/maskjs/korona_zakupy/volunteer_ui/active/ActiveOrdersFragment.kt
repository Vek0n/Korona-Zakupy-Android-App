package com.maskjs.korona_zakupy.volunteer_ui.active

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.data.orders.UsersInfoModel
import kotlinx.android.synthetic.main.fragment_active_orders.view.*

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

        val root = inflater.inflate(R.layout.fragment_active_orders, container, false)

        listView = root.findViewById(R.id.listViewActiveOrders) as ListView

///////////////////////////////////////////////////////////////////////////////////
        val productslist = ArrayList<String>()
        productslist.add("Harnaś")

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
////////////////////////////////////////////////////////////////////////////////

        val adapter = ActiveOrdersListAdapter(activity, ordersList)

        listView.adapter = adapter

        return root
    }
}
