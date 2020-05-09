package com.maskjs.korona_zakupy.volunteer_ui.available

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.maskjs.korona_zakupy.R

class AvailableOrdersFragment : Fragment() {

    private lateinit var availableOrdersViewModel: AvailableOrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        availableOrdersViewModel =
            ViewModelProviders.of(this).get(AvailableOrdersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_available_orders, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        availableOrdersViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
