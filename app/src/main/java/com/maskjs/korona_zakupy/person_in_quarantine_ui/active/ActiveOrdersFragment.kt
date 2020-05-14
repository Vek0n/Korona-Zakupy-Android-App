package com.maskjs.korona_zakupy.person_in_quarantine_ui.active

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maskjs.korona_zakupy.NewOrderActivity
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.RegisterActivity
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.helpers.OrdersListAdapter
import com.maskjs.korona_zakupy.viewmodels.quarantine.ActiveOrdersViewModel
import kotlinx.android.synthetic.main.active_order_details_popup.view.*
import kotlinx.android.synthetic.main.available_order_details_popup.view.*
import kotlinx.android.synthetic.main.available_order_details_popup.view.address_text_view
import kotlinx.android.synthetic.main.available_order_details_popup.view.date_text_view
import kotlinx.android.synthetic.main.available_order_details_popup.view.products_list_view
import kotlinx.android.synthetic.main.quarantine_active_order_details_popup.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActiveOrdersFragment : Fragment() {

    private lateinit var activeOrdersViewModel: ActiveOrdersViewModel
    private lateinit var listView: ListView
    private lateinit var adapterOrders: OrdersListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activeOrdersViewModel =
            ViewModelProviders.of(this).get(ActiveOrdersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_active_orders_quarantine, container, false)
        val context = requireContext()

        val addNewOrderButton = root.findViewById(R.id.addNewOrderButton) as FloatingActionButton
        addNewOrderButton.setOnClickListener {
            val intent = Intent(context, NewOrderActivity::class.java)
            startActivity(intent)
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        val userId = sharedPreferences.getString(R.string.user_id_key.toString(),"")
        val userId = "69d717d5-a97a-43d0-86b1-37605e6585bc" //Adam małysz

        listView = root.findViewById(R.id.listViewActiveOrders) as ListView

        CoroutineScope(Dispatchers.IO).launch {
            val data = activeOrdersViewModel.getActiveOrdersFromRepository(userId)
            setListViewAdapterOnMainThread(data, context)
        }


        listView.setOnItemClickListener { _, _, position, _ ->

            val dialogView =
                LayoutInflater.from(context).inflate(R.layout.quarantine_active_order_details_popup, null)
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

            val orderId = adapterOrders.getOrderId(position).toLong()

            alertDialog.setOnDismissListener {
                refreshFragment()
            }

            dialogView.cancel_order_q.setOnClickListener {
                refreshFragment()
                alertDialog.dismiss()
            }

            dialogView.ok_button.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    activeOrdersViewModel.completeOrder(userId, orderId)
                }
                refreshFragment()
                alertDialog.dismiss()
            }
        }
        return root
    }

    private suspend fun setListViewAdapterOnMainThread(
        input: ArrayList<GetOrderDto>,
        context: Context
    ) {
        withContext(Dispatchers.Main) {
            adapterOrders = OrdersListAdapter(
                context,
                input
            )
            listView.adapter = adapterOrders
        }
    }
    private fun refreshFragment(){
        val f : androidx.fragment.app.FragmentTransaction? = this.fragmentManager?.beginTransaction()
        f?.detach(this)
        f?.attach(this)
        f?.commit()
    }
}