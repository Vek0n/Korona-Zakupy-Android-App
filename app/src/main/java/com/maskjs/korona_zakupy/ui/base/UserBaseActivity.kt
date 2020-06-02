package com.maskjs.korona_zakupy.ui.base

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.maskjs.korona_zakupy.R

abstract class UserBaseActivity : BaseActivity(), BaseFragment.OnBackPress {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.global_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.global_menu_logout -> logOut()
            else -> return true
        }
    }

    private fun logOut() : Boolean{
        cleanSavedData()
        goToMainActivity()
        return true
    }

     private fun cleanSavedData(){
         setUserId(null)
         setUserToken(null)
         setUserRole(null)
     }


    override fun leaveApp(){
        moveTaskToBack(true)
        this.finishAndRemoveTask()
    }
}
