package com.eldroid.tanukiramenandroid.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.eldroid.tanukiramenandroid.backend.network.RetrofitClient
import com.eldroid.tanukiramenandroid.backend.repo.Repository
import com.eldroid.tanukiramenandroid.databinding.ActivityLoginBinding
import com.eldroid.tanukiramenandroid.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//
//        if (prefs.getBoolean("IS_LOGGED_IN", true)) {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }


        binding.loginButton.setOnClickListener {

            val username = binding.username

            if (username.text.toString().isEmpty()) {
                Snackbar.make(binding.root, "Username cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val repo = Repository()

                val response = repo.login(username.text.toString())

                if (response.isSuccessful) {
                    username.text.clear()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)

                    val prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)

                    prefs.edit()
                        .putLong("USER_ID", response.body()!!.userId)
                        .putString("USERNAME", response.body()!!.username)
                        .putString("NAME", response.body()!!.name)
                        .putBoolean("IS_LOGGED_IN", true)
                        .apply()

                    startActivity(intent)
                    finish()
                } else {
                    Snackbar.make(binding.root, "Error: ${response.code()}", Snackbar.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Error: ${response.code()}")
                }
            }
        }
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