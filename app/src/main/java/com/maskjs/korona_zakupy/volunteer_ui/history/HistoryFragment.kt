package com.maskjs.korona_zakupy.volunteer_ui.history

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.viewmodels.volunteer.HistoryViewModel
import com.maskjs.korona_zakupy.helpers.OrdersListAdapter
import kotlinx.android.synthetic.main.available_order_details_popup.view.address_text_view
import kotlinx.android.synthetic.main.available_order_details_popup.view.date_text_view
import kotlinx.android.synthetic.main.available_order_details_popup.view.products_list_view
import kotlinx.android.synthetic.main.history_order_details_popup.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var listView: ListView
    private lateinit var adapterOrders: OrdersListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        historyViewModel =
            ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        val context = requireContext()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        val userId = sharedPreferences.getString(R.string.user_id_key.toString(),"")
        val userId = "85b68f59-02ff-456b-b502-cf9830f10b1f"

        listView = root.findViewById(R.id.listViewHistory) as ListView

        CoroutineScope(Dispatchers.IO).launch {
            val data = historyViewModel.getHistoryOrdersFromRepository(userId)
            setListViewAdapterOnMainThread(context, data)
        }

        listView.setOnItemClickListener { _, _, position, _ ->

            val dialogView = LayoutInflater.from(context).inflate(R.layout.history_order_details_popup, null)
            val builder = AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle(R.string.order_details)

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

            dialogView.dismiss_button.setOnClickListener {
                alertDialog.dismiss()
            }
        }
        return root
    }


    private suspend fun setListViewAdapterOnMainThread(context: Context, input: ArrayList<GetOrderDto>) {
        withContext(Dispatchers.Main) {
            adapterOrders = OrdersListAdapter(
                context,
                input
            )
            listView.adapter = adapterOrders
        }
    }

}
