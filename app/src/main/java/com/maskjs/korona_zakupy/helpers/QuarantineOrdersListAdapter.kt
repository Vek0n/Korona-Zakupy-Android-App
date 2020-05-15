package com.maskjs.korona_zakupy.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.gson.internal.LinkedTreeMap
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import java.text.SimpleDateFormat


class QuarantineOrdersListAdapter(private val context: Context,
                                  private val dataSource: ArrayList<GetOrderDto>
) : BaseAdapter() {


    private val inflater: LayoutInflater
            = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getProducts(position: Int): List<String> {
        val order = getItem(position) as LinkedTreeMap<*, *>
        return order["products"] as List<String>
    }

    fun getAddress(position: Int): String?{
        val order = getItem(position) as LinkedTreeMap<*, *>
        return getUserInfo(order)?.get("address") as String?
    }

    fun getFirstName(position: Int): String?{
        val order = getItem(position) as LinkedTreeMap<*, *>
        val userInfo = getUserInfo(order)

        return if (userInfo == null) "-"
        else getUserInfo(order)?.get("firstName") as String?
    }

    private fun getRating(order: LinkedTreeMap<*, *>): Double?{
        return getUserInfo(order)?.get("rating") as Double?
    }

    private fun getOrderStatus(order: LinkedTreeMap<*, *>): String?{
        return order["orderStatus"] as String?
    }

    fun getOrderId(position: Int): Double{
        val order = getItem(position) as LinkedTreeMap<*, *>
        return order["orderId"] as Double
    }

    fun getOrderDate(position: Int): String?{
        val order = getItem(position) as LinkedTreeMap<*, *>
        val date = order["orderDate"] as String?
        val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        return formatter.format(parser.parse(date))
    }

    private fun getPhotoDirectory(order: LinkedTreeMap<*, *>, role: String): String?{
        return getUserInfo(order)?.get("photoDirectory") as String?
    }

    private fun getUserInfo(order: LinkedTreeMap<*, *>): LinkedTreeMap<*,*>?{
        val users = order["usersInfo"] as ArrayList<*>
        return if (users.size == 1)
            null
        else{
            val user = users[0] as LinkedTreeMap<*,*>
            if(user["userRole"] == "Volunteer") user
            else users[1] as LinkedTreeMap<*, *>?
        }
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {

            view = inflater.inflate(R.layout.list_active_orders_quarantine, parent, false)

            holder =
                ViewHolder()
            holder.dateText = view.findViewById(R.id.dateTextView) as TextView
            holder.status = view.findViewById(R.id.statusTextView) as TextView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val dateTextView = holder.dateText
        val statusTextView = holder.status

        val order = getItem(position) as LinkedTreeMap<*, *>

        dateTextView?.text = getOrderDate(position)
        statusTextView?.text = getOrderStatus(order)

        statusTextView?.setText(LABEL_NAME_PERSON_IN_QUARANTINE[getOrderStatus(order)] ?: R.string.invalid_status)

        statusTextView?.setTextColor(
            ContextCompat.getColor(context, LABEL_COLORS[getOrderStatus(order)] ?: R.color.secondaryTextColor))

        return view
    }

    companion object {
        private val LABEL_COLORS = hashMapOf(
            "InProgress" to R.color.inProgress,
            "Finished" to R.color.finished,
            "Avalible" to R.color.available,
            "AwaitingConfirmation" to R.color.awaitingConfirmation
        )

        private val LABEL_NAME_PERSON_IN_QUARANTINE= hashMapOf(
            "InProgress" to R.string.status_in_progress_quaranteen,
            "Finished" to R.string.status_finished,
            "Avalible" to R.string.status_available_quaranteen,
            "AwaitingConfirmation" to R.string.status_awaiting_confirmation
        )
    }

    internal class ViewHolder {
        var nameText: TextView? = null
        var dateText: TextView? = null
        var rating: TextView? = null
        var address: TextView? = null
        var status: TextView? = null
        var imageThumbnailUrl: ImageView? = null
    }


}