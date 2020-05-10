package com.maskjs.korona_zakupy.volunteer_ui.active

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.data.orders.GetOrderDto


internal class ViewHolder {
    var nameText: TextView? = null
    var dateText: TextView? = null
    var rating: TextView? = null
    var imageThumbnailUrl: TextView? = null
}
class ActiveOrdersListAdapter(private val context: Context?,
                              private val dataSource: ArrayList<GetOrderDto>) : BaseAdapter() {


    private val inflater: LayoutInflater
            = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }


    override fun getItem(position: Int): Any {
        return dataSource[position]
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
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
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val nameTextView = holder.nameText
        val dateTextView = holder.dateText
        val ratingTextView = holder.rating

        val order = getItem(position) as GetOrderDto

        nameTextView?.text = order.usersInfo[0].firstName
        dateTextView?.text = order.orderDate
        ratingTextView?.text = order.usersInfo[0].rating.toString()

        return view
    }


}