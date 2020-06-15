package com.maskjs.korona_zakupy.ui.volunteer.available

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.GetOrderDto
import com.maskjs.korona_zakupy.ui.base.BaseFragment
import com.maskjs.korona_zakupy.utils.LoadingSpinner
import com.maskjs.korona_zakupy.ui.volunteer.VolunteerOrdersListAdapter
import com.maskjs.korona_zakupy.utils.FragmentInitializeHelper
import com.maskjs.korona_zakupy.utils.Interfaces.IDataFragmentHelper
import kotlinx.android.synthetic.main.available_order_details_popup.view.*
import kotlinx.android.synthetic.main.available_order_details_popup.view.cancel_button
import kotlinx.coroutines.*
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.getViewModel
import kotlin.Exception

class AvailableOrdersFragment : BaseFragment(),
    IDataFragmentHelper {

    private lateinit var availableOrdersViewModel: AvailableOrdersViewModel
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapterOrders: VolunteerOrdersListAdapter
    private lateinit var nothingsHere: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        availableOrdersViewModel =
            requireActivity().lifecycleScope.getViewModel<AvailableOrdersViewModel>(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val (root, userId, token, context)
                = initialize(inflater, container)

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
    ): FragmentInitializeHelper {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            onBackPress?.leaveApp()
        }

        val root = inflater.inflate(R.layout.fragment_available_orders, container, false)
        val context = requireContext()

        val userId = getUserId()?: ""
        val token = getUserToken()?: ""

        listView = root.findViewById(R.id.listViewAvailableOrders) as ListView
        progressBar = root.findViewById(R.id.pBar) as ProgressBar
        nothingsHere = root.findViewById(R.id.nothingHereAvailableVolunteer)

        return FragmentInitializeHelper(root, userId, token, context)
    }


    override suspend fun populateListView(userId: String, token: String, context: Context) {
        supervisorScope {
            try{
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
       = availableOrdersViewModel.getAvailableOrdersFromRepository(token)



    override suspend fun handleNullData(data: ArrayList<GetOrderDto>) {
        if (data.size == 0){
            withContext(Dispatchers.Main){
                nothingsHere.visibility = View.VISIBLE
            }
        }
    }


    override suspend fun setListViewAdapterOnMainThread(input: ArrayList<GetOrderDto>, context: Context) {
        withContext(Dispatchers.Main) {
            adapterOrders =
                VolunteerOrdersListAdapter(
                    context,
                    input
                )
            listView.adapter = adapterOrders
        }
    }

    override fun setItemClickListeners(userId: String, token: String) {
        listView.setOnItemClickListener { _, _, position, _ ->
            showOrderDetailsDialog(position, userId, token)
        }
    }

    override fun showOrderDetailsDialog(position: Int, userId: String, token: String) {
        val context = requireContext()

        val dialog =
            AvailableOrderDetailsDialogVolunteer(
                adapterOrders
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
    ) {
        alertDialog.setOnDismissListener {
            refreshFragment()
        }


        dialogView.cancel_button.setOnClickListener {
            refreshFragment()
            alertDialog.dismiss()
        }

        dialogView.accept_button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                availableOrdersViewModel.acceptOrder(userId, orderId, token)
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