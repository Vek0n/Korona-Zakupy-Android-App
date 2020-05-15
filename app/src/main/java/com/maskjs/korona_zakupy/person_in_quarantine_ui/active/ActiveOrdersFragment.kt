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
import android.widget.ListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maskjs.korona_zakupy.NewOrderActivity
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.helpers.LoadingSpinner
import com.maskjs.korona_zakupy.helpers.QuarantineOrdersListAdapter
import com.maskjs.korona_zakupy.viewmodels.quarantine.ActiveOrdersViewModel
import kotlinx.android.synthetic.main.quarantine_active_order_details_popup.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActiveOrdersFragment : Fragment() {

    private lateinit var activeOrdersViewModel: ActiveOrdersViewModel
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapterQuarantineOrders: QuarantineOrdersListAdapter
    val fragment: String = "PersonInQuarantine"

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
        val userId = "6ecf2f0d-7b87-44fa-aa5b-bffbe25529e5" //Adam małysz

        listView = root.findViewById(R.id.listViewActiveOrders) as ListView
        progressBar = root.findViewById(R.id.pBar) as ProgressBar

        CoroutineScope(Dispatchers.IO).launch {
            LoadingSpinner().showLoadingDialog(progressBar)

            val data = activeOrdersViewModel.getActiveOrdersFromRepository(userId)
            setListViewAdapterOnMainThread(data, context)

            LoadingSpinner().hideLoadingDialog(progressBar)
        }


        listView.setOnItemClickListener { _, _, position, _ ->
            showActiveOrderDetailDialog(position,userId)
        }
        return root
    }

    private suspend fun setListViewAdapterOnMainThread(
        input: ArrayList<GetOrderDto>,
        context: Context
    ) {
        withContext(Dispatchers.Main) {
            adapterQuarantineOrders = QuarantineOrdersListAdapter(
                context,
                input
            )
            listView.adapter = adapterQuarantineOrders
        }
    }

    private fun showActiveOrderDetailDialog(position: Int, userId: String){
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.quarantine_active_order_details_popup, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle(R.string.order_details)

        val alertDialog = builder.show()

        val productsListView = dialogView.productsQuarantineActiveLV
        val acceptedByTextView = dialogView.acceptedByQuarantineActiveTV
        val dateTextView = dialogView.dateQuarantineActiveTV

        acceptedByTextView.text = adapterQuarantineOrders
            .getFirstName(position)

        dateTextView.text = adapterQuarantineOrders
            .getOrderDate(position)

        val productsAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            adapterQuarantineOrders
                .getProducts(position)
        )
        productsListView.adapter = productsAdapter

        val orderId = adapterQuarantineOrders.getOrderId(position).toLong()

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

    private fun refreshFragment(){
        val f : androidx.fragment.app.FragmentTransaction? = this.fragmentManager?.beginTransaction()
        f?.detach(this)
        f?.attach(this)
        f?.commit()
    }
}