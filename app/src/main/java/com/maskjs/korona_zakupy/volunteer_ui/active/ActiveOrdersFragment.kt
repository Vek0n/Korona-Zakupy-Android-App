package com.maskjs.korona_zakupy.volunteer_ui.active

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.maskjs.korona_zakupy.R

class ActiveOrdersFragment : Fragment() {

    private lateinit var activeOrdersViewModel: ActiveOrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activeOrdersViewModel =
            ViewModelProviders.of(this).get(ActiveOrdersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_active_orders, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        activeOrdersViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
