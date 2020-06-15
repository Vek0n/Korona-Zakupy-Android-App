package com.maskjs.korona_zakupy.ui.volunteer.history

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.activity.addCallback
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.GetOrderDto
import com.maskjs.korona_zakupy.ui.base.BaseFragment
import com.maskjs.korona_zakupy.utils.LoadingSpinner
import com.maskjs.korona_zakupy.ui.volunteer.VolunteerOrdersListAdapter
import com.maskjs.korona_zakupy.utils.FragmentInitializeHelper
import com.maskjs.korona_zakupy.utils.Interfaces.IDataFragmentHelper
import kotlinx.android.synthetic.main.history_order_details_popup_volunteer.view.*
import kotlinx.android.synthetic.main.rating_popup.view.*
import kotlinx.coroutines.*
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.getViewModel


class HistoryFragment : BaseFragment(),
    IDataFragmentHelper {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapterOrders: VolunteerOrdersListAdapter
    private lateinit var ratingBar: RatingBar
    private lateinit var nothingsHere: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        historyViewModel =
            requireActivity().lifecycleScope.getViewModel<HistoryViewModel>(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val (root, userId, token, context )
                = initialize(inflater, container)

        setItemClickListeners(token)

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
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        val context = requireContext()
        val userId = getUserId()?: ""
        val token = getUserToken()?: ""
        listView = root.findViewById(R.id.listViewHistory) as ListView
        progressBar = root.findViewById(R.id.pBar) as ProgressBar
        nothingsHere = root.findViewById(R.id.nothingHereHistory) as TextView

        return FragmentInitializeHelper(root, userId, token, context)
    }

    override fun setItemClickListeners(token: String) {
        listView.setOnItemClickListener { _, _, position, _ ->
            showOrderDetailsDialog(position, token)
        }
    }

    override suspend fun populateListView(userId: String, token: String, context: Context) {
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
        = historyViewModel.getHistoryOrdersFromRepository(userId, token)


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

    override fun showOrderDetailsDialog(position: Int, token: String) {
        val context = requireContext()

        val dialog =
            HistoryOrderDetailsDialogVolunteer(
                adapterOrders
            )

        dialog.initialize(context)

        dialog.setOrderDetails(
            position,
            context
        )

        val alertDialog = dialog.alertDialog
        val dialogView = dialog.dialogView

        setDialogClickListeners(
            alertDialog,
            dialogView,
            token,
            position
        )
    }

    override fun setDialogClickListeners(
        alertDialog: AlertDialog,
        dialogView: View,
        token: String,
        position: Int
    ) {
        alertDialog.setOnDismissListener {
            refreshFragment()
        }

        dialogView.dismiss_button.setOnClickListener {
            showReviewDialog(position, token)
            alertDialog.dismiss()
        }
    }


    @SuppressLint("InflateParams")
    private fun showReviewDialog(position: Int, token: String){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.rating_popup, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle(R.string.review_of_person_in_quarantine_title)
        ratingBar = dialogView.findViewById(R.id.ratingBar) as RatingBar

        val alertDialog = builder.show()
        val ratingBar = dialogView.ratingBar
        var ratingFloat = 0.0f

        ratingBar.onRatingBarChangeListener = OnRatingBarChangeListener{ _, v, _ ->
            ratingFloat = v
        }

        alertDialog.setOnDismissListener {
            refreshFragment()
        }

        val userId = adapterOrders.getPersonInQuarantineId(position)

        dialogView.sendReviewButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                historyViewModel.sendReview(userId, ratingFloat.toDouble(), token)
            }
            if(ratingFloat == 0.0f)
                Toast.makeText(dialogView.context,getString(R.string.please_review), Toast.LENGTH_SHORT).show()
            else
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
