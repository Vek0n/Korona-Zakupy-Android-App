package com.maskjs.korona_zakupy.ui.volunteer.active

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.GetOrderDto
import com.maskjs.korona_zakupy.utils.LoadingSpinner
import com.maskjs.korona_zakupy.ui.volunteer.VolunteerOrdersListAdapter
import kotlinx.android.synthetic.main.active_order_details_popup.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.Exception

class ActiveOrdersFragment : Fragment() {

    private lateinit var activeOrdersViewModel: ActiveOrdersViewModel
    private  lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapterOrders: VolunteerOrdersListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activeOrdersViewModel =
            ViewModelProviders.of(this).get(ActiveOrdersViewModel::class.java)

        val context = requireContext()
        val root = inflater.inflate(R.layout.fragment_active_orders, container, false)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        val userId = sharedPreferences.getString(R.string.user_id_key.toString(),"")
        val userId = "17d4fb4e-d252-4154-9d44-88693b07e99e"
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0MTEyMUB0ZXN0LmNvbSIsImp0aSI6IjVjMWU1NGM3LTVjZGUtNGUzZS1hMWIwLTM3MDEyMzU4M2U2ZSIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL25hbWVpZGVudGlmaWVyIjoiMTdkNGZiNGUtZDI1Mi00MTU0LTlkNDQtODg2OTNiMDdlOTllIiwiZXhwIjoxNjIxMDk2MTQ5LCJpc3MiOiJodHRwOi8veW91cmRvbWFpbi5jb20iLCJhdWQiOiJodHRwOi8veW91cmRvbWFpbi5jb20ifQ.s_KyGQ0PuxBs0Z_WiBBPYGtCAigBRW4JF6Tl9lBFyws"
        listView = root.findViewById(R.id.listViewActiveOrders) as ListView
        progressBar = root.findViewById(R.id.pBar) as ProgressBar

        CoroutineScope(IO).launch {
            LoadingSpinner().showLoadingDialog(progressBar)
            supervisorScope {
                try {
                    val data = activeOrdersViewModel.getActiveOrdersFromRepository(userId, token)
                    setListViewAdapterOnMainThread(data, context)
                }catch (ex: Exception){
                    val data = arrayListOf<GetOrderDto>()
                    setListViewAdapterOnMainThread(data, context)
                }
            }
            LoadingSpinner().hideLoadingDialog(progressBar)
        }
//        }catch (ex: Exception){
//            val data = arrayListOf<GetOrderDto>()
//            adapterOrders = VolunteerOrdersListAdapter(context, data)
//            listView.adapter = adapterOrders
//        }

        listView.setOnItemClickListener { _, _, position, _ ->
            showActiveOrderDetailDialog(position, userId,token)
        }

        return root
    }

    private suspend fun setListViewAdapterOnMainThread(input: ArrayList<GetOrderDto>, context: Context){
        withContext(Main){
            adapterOrders =
                VolunteerOrdersListAdapter(
                    context,
                    input
                )
            listView.adapter = adapterOrders
        }
    }

    @SuppressLint("InflateParams")
    private fun showActiveOrderDetailDialog(position: Int, userId: String, token: String){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.active_order_details_popup, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle(R.string.order_details)

        val alertDialog = builder.show()

        val productsListView = dialogView.productsVolunteerActiveLV
        val addressTextView = dialogView.addressVolunteerActiveTV
        val dateTextView = dialogView.dateVolunteerActiveTV

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

        dialogView.cancel_order.setOnClickListener {
            CoroutineScope(IO).launch {
                activeOrdersViewModel.unAcceptOrder(userId, orderId, token)
            }
            refreshFragment()
            alertDialog.dismiss()
        }

        dialogView.finish_order.setOnClickListener {
            CoroutineScope(IO).launch {
                activeOrdersViewModel.completeOrder(userId, orderId, token)
            }
            refreshFragment()
            alertDialog.dismiss()
        }
    }

    private fun refreshFragment(){
        val f : FragmentTransaction? = this.fragmentManager?.beginTransaction()
        f?.detach(this)
        f?.attach(this)
        f?.commit()
    }
}