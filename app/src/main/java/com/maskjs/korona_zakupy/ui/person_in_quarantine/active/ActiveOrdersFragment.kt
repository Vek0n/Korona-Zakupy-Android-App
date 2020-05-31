package com.maskjs.korona_zakupy.ui.person_in_quarantine.active

import android.annotation.SuppressLint
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
import com.maskjs.korona_zakupy.ui.new_order.NewOrderActivity
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.GetOrderDto
import com.maskjs.korona_zakupy.ui.new_order.add_product_dialog.AddProductDialogFragment
import com.maskjs.korona_zakupy.utils.LoadingSpinner
import com.maskjs.korona_zakupy.ui.person_in_quarantine.QuarantineOrdersListAdapter
import kotlinx.android.synthetic.main.quarantine_active_order_details_popup.view.*
import kotlinx.coroutines.*

class ActiveOrdersFragment : Fragment() {

    private lateinit var activeOrdersViewModel: ActiveOrdersViewModel
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapterQuarantineOrders: QuarantineOrdersListAdapter
    private  var onAddOrderButtonClickListener: OnAddOrderButtonClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onAddOrderButtonClickListener =  (context as? OnAddOrderButtonClickListener)
    }

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
            showChooseOrderTypeDialog()
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val userId = sharedPreferences.getString(R.string.user_id_key.toString(),"")
        val token = sharedPreferences.getString(R.string.user_token_key.toString(),"")
        listView = root.findViewById(R.id.listViewActiveOrders) as ListView
        progressBar = root.findViewById(R.id.pBar) as ProgressBar

        CoroutineScope(Dispatchers.IO).launch {
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


        listView.setOnItemClickListener { _, _, position, _ ->
            showActiveOrderDetailDialog(position,userId, token)
        }
        return root
    }

    private fun showChooseOrderTypeDialog(){
        onAddOrderButtonClickListener?.showChooseOrderTypeDialog()
    }

    private suspend fun setListViewAdapterOnMainThread(
        input: ArrayList<GetOrderDto>,
        context: Context
    ) {
        withContext(Dispatchers.Main) {
            adapterQuarantineOrders =
                QuarantineOrdersListAdapter(
                    context,
                    input
                )
            listView.adapter = adapterQuarantineOrders
        }
    }

    @SuppressLint("InflateParams")
    private fun showActiveOrderDetailDialog(position: Int, userId: String, token: String){
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
                activeOrdersViewModel.completeOrder(userId, orderId, token)
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

    interface OnAddOrderButtonClickListener{
        fun showChooseOrderTypeDialog()
    }

}