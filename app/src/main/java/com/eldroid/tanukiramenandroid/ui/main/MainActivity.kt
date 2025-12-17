package com.eldroid.tanukiramenandroid.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.eldroid.tanukiramenandroid.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    lateinit var prefs: SharedPreferences

    var userId by Delegates.notNull<Long>()
    lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        userId = prefs.getLong("USER_ID", 0)
        name = prefs.getString("NAME", "").toString()



        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                    as NavHostFragment

        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)

        supportFragmentManager.beginTransaction()
            .replace(R.id.cart_bottom_sheet, CartBottomSheetFragment())
            .commit()
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            currentFocus!!.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }
}