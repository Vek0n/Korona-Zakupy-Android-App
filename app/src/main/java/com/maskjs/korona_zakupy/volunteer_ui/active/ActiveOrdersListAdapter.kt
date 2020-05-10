package com.maskjs.korona_zakupy.volunteer_ui.active

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.google.gson.internal.LinkedTreeMap
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.squareup.picasso.Picasso


class ActiveOrdersListAdapter(private val context: Context?,
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {

            view = inflater.inflate(R.layout.list_active_orders, parent, false)

            holder = ViewHolder()
            holder.nameText = view.findViewById(R.id.nameTextView) as TextView
            holder.dateText = view.findViewById(R.id.dateTextView) as TextView
            holder.rating = view.findViewById(R.id.ratingTextView) as TextView
            holder.imageThumbnailUrl = view.findViewById(R.id.avatarThumbnailImageView) as ImageView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val nameTextView = holder.nameText
        val dateTextView = holder.dateText
        val ratingTextView = holder.rating
        val thumbnailImageView = holder.imageThumbnailUrl

        val order = getItem(position) as LinkedTreeMap<*, *>
        val x = order["usersInfo"] as ArrayList<*>
        val y = x[0] as LinkedTreeMap<*,*>

        nameTextView?.text = y["firstName"] as String?
        dateTextView?.text = order["orderDate"].toString()
        ratingTextView?.text = (y["rating"] as Double?).toString()

        Picasso.get()
            .load(y["photoDirectory"] as String?)
            .into(thumbnailImageView)

        return view
    }

internal class ViewHolder {
    var nameText: TextView? = null
    var dateText: TextView? = null
    var rating: TextView? = null
    var imageThumbnailUrl: ImageView? = null
}}