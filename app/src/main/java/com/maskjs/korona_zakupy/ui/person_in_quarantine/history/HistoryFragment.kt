package com.maskjs.korona_zakupy.ui.person_in_quarantine.history

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProviders
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.GetOrderDto
import com.maskjs.korona_zakupy.ui.base.BaseFragment
import com.maskjs.korona_zakupy.utils.LoadingSpinner
import com.maskjs.korona_zakupy.ui.person_in_quarantine.QuarantineOrdersListAdapter
import kotlinx.android.synthetic.main.quarantine_history_order_details_popup.view.*
import kotlinx.android.synthetic.main.rating_popup.view.*
import kotlinx.coroutines.*
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.getViewModel

class HistoryFragment : BaseFragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapterQuarantineOrders: QuarantineOrdersListAdapter
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

        CoroutineScope(Dispatchers.IO).launch {
            LoadingSpinner().showLoadingDialog(progressBar)
            supervisorScope {
                try {
                    val data = historyViewModel.getHistoryOrdersFromRepository(userId, token)
                    setListViewAdapterOnMainThread(context, data)
                    if (data.size == 0){
                        withContext(Dispatchers.Main){
                            nothingsHere.visibility = View.VISIBLE
                        }
                    }
                }catch (ex: Exception){
                    val data = arrayListOf<GetOrderDto>()
                    setListViewAdapterOnMainThread(context, data)
                }

            }
            LoadingSpinner().hideLoadingDialog(progressBar)
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            showHistoryOrderDetailDialog(position, token)
        }
        return root
    }


    private suspend fun setListViewAdapterOnMainThread(context: Context, input: ArrayList<GetOrderDto>) {
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
    private fun showReviewDialog(position: Int, token: String){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.rating_popup, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle(R.string.review_of_volunteer_title)
        ratingBar = dialogView.findViewById(R.id.ratingBar) as RatingBar

        val alertDialog = builder.show()
        val ratingBar = dialogView.ratingBar
        var ratingFloat = 0.0f

        ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, v, _ ->
            ratingFloat = v
        }

        alertDialog.setOnDismissListener {
            refreshFragment()
        }

        val userId = adapterQuarantineOrders.getVolunteerId(position)

        dialogView.sendReviewButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                historyViewModel.sendReview(userId, ratingFloat.toDouble(), token)
            }

            if(ratingFloat == 0.0f)
                Toast.makeText(dialogView.context,
                    getString(R.string.please_review),
                    Toast.LENGTH_SHORT)
                .show()
            else
                alertDialog.dismiss()
        }
    }

    @SuppressLint("InflateParams")
    private fun showHistoryOrderDetailDialog(position: Int, token:String){
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.quarantine_history_order_details_popup, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle(R.string.order_details)

        val alertDialog = builder.show()

        val productsListView = dialogView.productsQuarantineHistoryLV
        val acceptedByTextView = dialogView.acceptedByQuarantineHistoryTV
        val dateTextView = dialogView.dateQuarantineHistoryTV

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

        alertDialog.setOnDismissListener {
            refreshFragment()
        }

        dialogView.dismiss_button.setOnClickListener {
            showReviewDialog(position, token)
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
