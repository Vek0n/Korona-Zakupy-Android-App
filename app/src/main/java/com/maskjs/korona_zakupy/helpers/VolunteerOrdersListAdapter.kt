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
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class VolunteerOrdersListAdapter(private val context: Context,
                                 private val dataSource: ArrayList<GetOrderDto>
) : BaseAdapter() {


    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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

    private fun getFirstName(position: Int): String?{
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

    private fun getPhotoDirectory(order: LinkedTreeMap<*, *>): String?{
        return getUserInfo(order)?.get("photoDirectory") as String?
    }

    private fun getUserInfo(order: LinkedTreeMap<*, *>): LinkedTreeMap<*, *>?{
        val users = order["usersInfo"] as ArrayList<*>
        return if (users.size == 1)
            users[0] as LinkedTreeMap<*, *>?
        else{
            val user = users[0] as LinkedTreeMap<*, *>
            if(user["userRole"] == "PersonInQuarantine") user
            else users[1] as LinkedTreeMap<*, *>?
        }
    }


    fun getPersonInQuarantineId(position: Int): String{
        val order = getItem(position) as LinkedTreeMap<*, *>
        val users = order["usersInfo"] as ArrayList<*>
        var user = users[0] as LinkedTreeMap<*,*>
        return if(user["userRole"] == "PersonInQuarantine") user["userId"] as String
        else {
            user = users[1] as LinkedTreeMap<*, *>
            user["userId"] as String
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {

            view = inflater.inflate(R.layout.list_active_orders_volunteer, parent, false)

            holder =
                ViewHolder()
            holder.nameText = view.findViewById(R.id.nameTextView) as TextView
            holder.rating = view.findViewById(R.id.ratingTextView) as TextView
            holder.address = view.findViewById(R.id.addressTextView) as TextView
            holder.status = view.findViewById(R.id.statusTextView) as TextView
            holder.imageThumbnailUrl = view.findViewById(R.id.avatarThumbnailImageView) as ImageView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val nameTextView = holder.nameText
        val ratingTextView = holder.rating
        val addressTextView = holder.address
        val statusTextView = holder.status
        val thumbnailImageView = holder.imageThumbnailUrl

        val order = getItem(position) as LinkedTreeMap<*, *>

        nameTextView?.text = getFirstName(position)
        ratingTextView?.text = getRating(order).toString()
        addressTextView?.text = getAddress(position)

        statusTextView?.setText(LABEL_NAME_VOLUNTEER[getOrderStatus(order)] ?: R.string.invalid_status)

        statusTextView?.setTextColor(
            ContextCompat.getColor(context, LABEL_COLORS[getOrderStatus(order)] ?: R.color.secondaryTextColor))

        Picasso.get()
//            .load(getPhotoDirectory(order))
            .load("https://i.imgur.com/KMH51Kr.png") //DEBUG
            .into(thumbnailImageView)

        return view
    }

    companion object {
        private val LABEL_COLORS = hashMapOf(
            "InProgress" to R.color.inProgress,
            "Finished" to R.color.finished,
            "Avalible" to R.color.available,
            "AwaitingConfirmation" to R.color.awaitingConfirmation
        )

        private val LABEL_NAME_VOLUNTEER = hashMapOf(
            "InProgress" to R.string.status_in_progress,
            "Finished" to R.string.status_finished,
            "Avalible" to R.string.status_available,
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