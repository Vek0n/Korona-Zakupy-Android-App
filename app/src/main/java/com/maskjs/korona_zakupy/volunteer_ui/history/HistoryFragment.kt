package com.maskjs.korona_zakupy.volunteer_ui.history

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.helpers.LoadingSpinner
import com.maskjs.korona_zakupy.helpers.VolunteerOrdersListAdapter
import com.maskjs.korona_zakupy.viewmodels.volunteer.HistoryViewModel
import kotlinx.android.synthetic.main.history_order_details_popup_volunteer.view.*
import kotlinx.android.synthetic.main.rating_popup.view.*
import kotlinx.coroutines.*


class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapterOrders: VolunteerOrdersListAdapter
    private lateinit var ratingBar: RatingBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        historyViewModel =
            ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        val context = requireContext()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        val userId = sharedPreferences.getString(R.string.user_id_key.toString(),"")
        val userId = "17d4fb4e-d252-4154-9d44-88693b07e99e"
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0MTEyMUB0ZXN0LmNvbSIsImp0aSI6IjVjMWU1NGM3LTVjZGUtNGUzZS1hMWIwLTM3MDEyMzU4M2U2ZSIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL25hbWVpZGVudGlmaWVyIjoiMTdkNGZiNGUtZDI1Mi00MTU0LTlkNDQtODg2OTNiMDdlOTllIiwiZXhwIjoxNjIxMDk2MTQ5LCJpc3MiOiJodHRwOi8veW91cmRvbWFpbi5jb20iLCJhdWQiOiJodHRwOi8veW91cmRvbWFpbi5jb20ifQ.s_KyGQ0PuxBs0Z_WiBBPYGtCAigBRW4JF6Tl9lBFyws"
        listView = root.findViewById(R.id.listViewHistory) as ListView
        progressBar = root.findViewById(R.id.pBar) as ProgressBar

        CoroutineScope(Dispatchers.IO).launch {
            LoadingSpinner().showLoadingDialog(progressBar)
            supervisorScope {
                try {
                    val data = historyViewModel.getHistoryOrdersFromRepository(userId, token)
                    setListViewAdapterOnMainThread(context, data)
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
            adapterOrders = VolunteerOrdersListAdapter(
                context,
                input
            )
            listView.adapter = adapterOrders
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


    @SuppressLint("InflateParams")
    private fun showHistoryOrderDetailDialog(position: Int, token: String){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.history_order_details_popup_volunteer, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle(R.string.order_details)

        val alertDialog = builder.show()

        val productsListView = dialogView.productsVolunteerHistoryLV
        val addressTextView = dialogView.addressVolunteerHistoryTV
        val dateTextView = dialogView.dateVolunteerHistoryTV

        addressTextView.text = adapterOrders
            .getAddress(position)

        dateTextView.text = adapterOrders
            .getOrderDate(position)

        val productsAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            adapterOrders
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
