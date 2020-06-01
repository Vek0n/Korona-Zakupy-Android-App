package com.maskjs.korona_zakupy.ui.person_in_quarantine

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.ui.base.UserBaseActivity
import com.maskjs.korona_zakupy.ui.person_in_quarantine.active.ActiveOrdersFragment
import com.maskjs.korona_zakupy.ui.person_in_quarantine.active.choose_order_type.ChooseOrderTypeDialogFragment

class PersonInQuarantineActivity : UserBaseActivity(),ActiveOrdersFragment.OnAddOrderButtonClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_in_quarantine)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_active_quarantine,
                R.id.navigation_history_quarantine
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun showChooseOrderTypeDialog(){
        val chooseOrderTypeDialogFragment =
            ChooseOrderTypeDialogFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("ChooseOrderDialog")
        if (prev != null)
        {
            fragmentTransaction.remove(prev)
        }
        fragmentTransaction.addToBackStack(null)
       chooseOrderTypeDialogFragment.show(fragmentTransaction, "ChooseOrderDialog")
    }
}
