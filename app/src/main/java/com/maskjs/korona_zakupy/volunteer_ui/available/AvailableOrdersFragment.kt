package com.maskjs.korona_zakupy.volunteer_ui.available

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.helpers.LoadingSpinner
import com.maskjs.korona_zakupy.viewmodels.volunteer.AvailableOrdersViewModel
import com.maskjs.korona_zakupy.helpers.VolunteerOrdersListAdapter
import kotlinx.android.synthetic.main.available_order_details_popup.view.*
import kotlinx.android.synthetic.main.available_order_details_popup.view.cancel_button
import kotlinx.coroutines.*
import kotlin.Exception

class AvailableOrdersFragment : Fragment() {

    private lateinit var availableOrdersViewModel: AvailableOrdersViewModel
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapterOrders: VolunteerOrdersListAdapter
    val fragment: String = "Volunteer"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        availableOrdersViewModel =
            ViewModelProviders.of(this).get(AvailableOrdersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_available_orders, container, false)
        val context = requireContext()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
       // val userId = sharedPreferences.getString(R.string.user_id_key.toString(),"")
        val userId = "85b68f59-02ff-456b-b502-cf9830f10b1f"

        listView = root.findViewById(R.id.listViewAvailableOrders) as ListView
        progressBar = root.findViewById(R.id.pBar) as ProgressBar

        CoroutineScope(Dispatchers.IO).launch {
            LoadingSpinner().showLoadingDialog(progressBar)
            supervisorScope {
                try{
                    val data = availableOrdersViewModel.getAvailableOrdersFromRepository()
                    setListViewAdapterOnMainThread(context, data)
                }catch (ex: Exception){
                    val data = arrayListOf<GetOrderDto>()
                    setListViewAdapterOnMainThread(context, data)
                }
            }
            LoadingSpinner().hideLoadingDialog(progressBar)
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            showAvailableOrderDetailDialog(position, userId)
        }
        return root
    }


    private suspend fun setListViewAdapterOnMainThread(context: Context, input: ArrayList<GetOrderDto>) {
        withContext(Dispatchers.Main) {
            adapterOrders = VolunteerOrdersListAdapter(
                context,
                input
            )
            listView.adapter = adapterOrders
        }
    }

    @SuppressLint("InflateParams")
    private fun showAvailableOrderDetailDialog(position: Int, userId: String){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.available_order_details_popup, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle(R.string.order_details)

        val alertDialog = builder.show()

        val productsListView = dialogView.productsVolunteerAvailableLV
        val addressTextView = dialogView.addressVolunteerAvailableTV
        val dateTextView = dialogView.dateVolunteerAvailableTV

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


        dialogView.cancel_button.setOnClickListener {
            refreshFragment()
            alertDialog.dismiss()
        }

        dialogView.accept_button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                availableOrdersViewModel.acceptOrder(userId, orderId)
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