package com.maskjs.korona_zakupy.ui.volunteer.active

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.addCallback
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.GetOrderDto
import com.maskjs.korona_zakupy.ui.base.BaseFragment
import com.maskjs.korona_zakupy.utils.LoadingSpinner
import com.maskjs.korona_zakupy.ui.volunteer.VolunteerOrdersListAdapter
import kotlinx.android.synthetic.main.active_order_details_popup.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.Exception

class ActiveOrdersFragment : BaseFragment() {

    private lateinit var activeOrdersViewModel: ActiveOrdersViewModel
    private  lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapterOrders: VolunteerOrdersListAdapter
    private lateinit var nothingsHere: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            onBackPress?.leaveApp()
        }

        activeOrdersViewModel =
            ViewModelProviders.of(this).get(ActiveOrdersViewModel::class.java)

        val context = requireContext()
        val root = inflater.inflate(R.layout.fragment_active_orders, container, false)

        val userId = getUserId()?: ""
        val token = getUserToken()?: ""

        listView = root.findViewById(R.id.listViewActiveOrders) as ListView
        progressBar = root.findViewById(R.id.pBar) as ProgressBar
        nothingsHere = root.findViewById(R.id.nothingHereActiveVolunteer)

        CoroutineScope(IO).launch {
            LoadingSpinner().showLoadingDialog(progressBar)
            supervisorScope {
                try {
                    val data = activeOrdersViewModel.getActiveOrdersFromRepository(userId, token)
                    setListViewAdapterOnMainThread(data, context)
                    if (data.size == 0){
                        withContext(Dispatchers.Main){
                            nothingsHere.visibility = View.VISIBLE
                        }
                    }
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
