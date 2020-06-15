package com.maskjs.korona_zakupy.ui.volunteer.active

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.addCallback
import androidx.fragment.app.FragmentTransaction
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.GetOrderDto
import com.maskjs.korona_zakupy.ui.base.BaseFragment
import com.maskjs.korona_zakupy.utils.LoadingSpinner
import com.maskjs.korona_zakupy.ui.volunteer.VolunteerOrdersListAdapter
import com.maskjs.korona_zakupy.utils.FragmentInitializeHelper
import com.maskjs.korona_zakupy.utils.Interfaces.IDataFragmentHelper
import kotlinx.android.synthetic.main.active_order_details_popup.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.getViewModel
import kotlin.Exception

class ActiveOrdersFragment : BaseFragment(),
    IDataFragmentHelper {

    private lateinit var activeOrdersViewModel: ActiveOrdersViewModel
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapterOrders: VolunteerOrdersListAdapter
    private lateinit var nothingsHere: TextView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activeOrdersViewModel = requireActivity().lifecycleScope.getViewModel<ActiveOrdersViewModel>(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val (root, userId, token, context) = initialize(inflater, container)

        setItemClickListeners(userId, token)

        CoroutineScope(IO).launch {
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

        val context = requireContext()
        val root = inflater.inflate(R.layout.fragment_active_orders, container, false)

        val userId = getUserId()?: ""
        val token = getUserToken()?: ""

        listView = root.findViewById(R.id.listViewActiveOrders) as ListView
        progressBar = root.findViewById(R.id.pBar) as ProgressBar
        nothingsHere = root.findViewById(R.id.nothingHereActiveVolunteer)

        return FragmentInitializeHelper(root, userId, token, context)
    }

    override fun setItemClickListeners(userId: String, token: String){
        listView.setOnItemClickListener { _, _, position, _ ->
            showOrderDetailsDialog(position, userId,token)
        }
    }


    override suspend fun populateListView(
        userId: String,
        token: String,
        context: Context
    ){
        supervisorScope {
            try {
                val data = getDataFromRepository(userId, token)
                handleNullData(data)
                setListViewAdapterOnMainThread(data, context)
            }catch (ex: Exception){
                val data = arrayListOf<GetOrderDto>()
                setListViewAdapterOnMainThread(data, context)
            }
        }
    }

    override suspend fun handleNullData(data: ArrayList<GetOrderDto>){
        if (data.size == 0){
            withContext(Dispatchers.Main){
                nothingsHere.visibility = View.VISIBLE
            }
        }
    }

    override suspend fun getDataFromRepository(userId: String, token: String)
            = activeOrdersViewModel.getActiveOrdersFromRepository(userId, token)

    override suspend fun setListViewAdapterOnMainThread(input: ArrayList<GetOrderDto>, context: Context){
        withContext(Main){
            adapterOrders =
                VolunteerOrdersListAdapter(
                    context,
                    input
                )
            listView.adapter = adapterOrders
        }
    }


    override fun showOrderDetailsDialog(position: Int, userId: String, token: String) {
        val context = requireContext()

        val dialog =
            ActiveOrderDetailsDialogVolunteer(
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
