package com.maskjs.korona_zakupy.ui.person_in_quarantine.active

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.addCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.GetOrderDto
import com.maskjs.korona_zakupy.ui.base.BaseFragment
import com.maskjs.korona_zakupy.utils.LoadingSpinner
import com.maskjs.korona_zakupy.ui.person_in_quarantine.QuarantineOrdersListAdapter
import com.maskjs.korona_zakupy.utils.FragmentInitializeHelper
import com.maskjs.korona_zakupy.utils.Interfaces.IDataFragmentHelper
import kotlinx.android.synthetic.main.quarantine_active_order_details_popup.view.*
import kotlinx.coroutines.*
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.getViewModel

class ActiveOrdersFragment :BaseFragment(),
    IDataFragmentHelper {

    private lateinit var activeOrdersViewModel: ActiveOrdersViewModel
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapterQuarantineOrders: QuarantineOrdersListAdapter
    private lateinit var nothingHereTV: TextView
    private  var onAddOrderButtonClickListener: OnAddOrderButtonClickListener? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        onAddOrderButtonClickListener =  (context as? OnAddOrderButtonClickListener)
        activeOrdersViewModel = requireActivity().lifecycleScope.getViewModel<ActiveOrdersViewModel>(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val (root, userId, token, context ) = initialize(inflater, container)

        setItemClickListeners(userId, token)

        CoroutineScope(Dispatchers.IO).launch {
            LoadingSpinner().showLoadingDialog(progressBar)

            populateListView(userId, token, context)

            LoadingSpinner().hideLoadingDialog(progressBar)
        }


        return root
    }

    override fun initialize(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInitializeHelper{
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            onBackPress?.leaveApp()
        }
        val root = inflater.inflate(R.layout.fragment_active_orders_quarantine, container, false)

        val addNewOrderButton = root.findViewById(R.id.addNewOrderButton) as FloatingActionButton
        addNewOrderButton.setOnClickListener {
            showChooseOrderTypeDialog()
        }
        val context = requireContext()

        val userId = getUserId()?: ""
        val token = getUserToken()?: ""

        listView = root.findViewById(R.id.listViewActiveOrders) as ListView
        progressBar = root.findViewById(R.id.pBar) as ProgressBar
        nothingHereTV = root.findViewById(R.id.nothingHereActiveQuarantine) as TextView

        return FragmentInitializeHelper(root, userId, token, context)
    }

    override fun setItemClickListeners(userId: String, token: String){
        listView.setOnItemClickListener { _, _, position, _ ->
            showOrderDetailsDialog(position,userId, token)
        }
    }

    override suspend fun populateListView(userId: String, token: String, context: Context){
        supervisorScope {
            try {
                val data = getDataFromRepository(userId, token)
                setListViewAdapterOnMainThread(data, context)
                handleNullData(data)
            }catch (ex: Exception){
                val data = arrayListOf<GetOrderDto>()
                setListViewAdapterOnMainThread(data, context)
            }
        }
    }

    override suspend fun getDataFromRepository(userId: String, token: String): ArrayList<GetOrderDto>
            = activeOrdersViewModel.getActiveOrdersFromRepository(userId, token)


    override suspend fun handleNullData(data: ArrayList<GetOrderDto>){
        if (data.size == 0){
            withContext(Dispatchers.Main){
                nothingHereTV.visibility = View.VISIBLE
            }
        }
    }

    private fun showChooseOrderTypeDialog(){
        onAddOrderButtonClickListener?.showChooseOrderTypeDialog()
    }

    override suspend fun setListViewAdapterOnMainThread(
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

    override fun showOrderDetailsDialog(position: Int, userId: String, token: String){
        val context = requireContext()

        val dialog =
            ActiveOrderDetailsDialogQuarantine(
                adapterQuarantineOrders
            )

        dialog.initialize(context)

        dialog.setOrderDetails(
            position,
            context
        )

        val alertDialog = dialog.alertDialog
        val dialogView = dialog.dialogView
        val orderId = dialog.orderId

        setDialogClickListeners(
            alertDialog,
            dialogView,
            orderId,
            token,
            userId
        )
    }

    override fun setDialogClickListeners(
        alertDialog: AlertDialog,
        dialogView: View,
        orderId: Long,
        token: String,
        userId: String
    ){
        alertDialog.setOnDismissListener {
            refreshFragment()
        }

        dialogView.cancel_order_q.setOnClickListener {
            refreshFragment()
            CoroutineScope(Dispatchers.IO).launch {
                activeOrdersViewModel.deleteOrder(orderId, token)
            }
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