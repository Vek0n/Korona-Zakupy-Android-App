package com.maskjs.korona_zakupy.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.internal.LinkedTreeMap
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat


class OrdersListAdapter(private val context: Context?,
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

    private fun getUserInfo(order: LinkedTreeMap<*, *>): LinkedTreeMap<*,*>{
        val x = order["usersInfo"] as ArrayList<*>
        return if (x.size == 1)
            x[0] as LinkedTreeMap<*,*>
        else{
            val user = x[0] as LinkedTreeMap<*,*>
            if(user["userRole"] == "PersonInQuarantine") user
            else x[1] as LinkedTreeMap<*, *>
        }
        //TODO FIND USER BY ROLE
    }


    fun getProducts(position: Int): List<String> {
        val order = getItem(position) as LinkedTreeMap<*, *>
        return order["products"] as List<String>
    }

    fun getAddress(position: Int): String?{
        val order = getItem(position) as LinkedTreeMap<*, *>
        return getUserInfo(order)["address"] as String?
    }

    private fun getFirstName(order: LinkedTreeMap<*, *>): String?{
        return getUserInfo(order)["firstName"] as String?
    }

    private fun getRating(order: LinkedTreeMap<*, *>): Double?{
        return getUserInfo(order)["rating"] as Double?
    }

    private fun getStatus(order: LinkedTreeMap<*, *>): String?{
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
        return getUserInfo(order)["photoDirectory"] as String?
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {

            view = inflater.inflate(R.layout.list_active_orders, parent, false)

            holder =
                ViewHolder()
            holder.nameText = view.findViewById(R.id.nameTextView) as TextView
            //holder.dateText = view.findViewById(R.id.addressTextView) as TextView
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

        nameTextView?.text = getFirstName(order)
        ratingTextView?.text = getRating(order).toString()
        addressTextView?.text = getAddress(position)
        statusTextView?.text = getStatus(order)

        Picasso.get()
            .load(getPhotoDirectory(order))
            .into(thumbnailImageView)

        return view
    }

    internal class ViewHolder {
        var nameText: TextView? = null
        var dateText: TextView? = null
        var rating: TextView? = null
        var address: TextView? = null
        var status: TextView? = null
        var imageThumbnailUrl: ImageView? = null
    }}