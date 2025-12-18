package com.eldroid.tanukiramenandroid.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.eldroid.tanukiramenandroid.backend.network.NetworkConfig
import com.eldroid.tanukiramenandroid.backend.network.RetrofitClient
import com.eldroid.tanukiramenandroid.backend.repo.Repository
import com.eldroid.tanukiramenandroid.databinding.ActivityLoginBinding
import com.eldroid.tanukiramenandroid.ui.main.MainActivity
import com.eldroid.tanukiramenandroid.ui.main.WebViewActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.net.NetworkInterface

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//
//        if (prefs.getBoolean("IS_LOGGED_IN", false)) {
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

                    val prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)

                    prefs.edit()
                        .putLong("USER_ID", response.body()!!.userId)
                        .putString("USERNAME", response.body()!!.username)
                        .putString("NAME", response.body()!!.name)
                        .putString("ROLE", response.body()!!.role.roleName)
                        .putBoolean("IS_LOGGED_IN", true)
                        .apply()

                    val userJson = """
                    {
                      "userId": ${response.body()!!.userId},
                      "username": "${response.body()!!.username}",
                      "role": "${response.body()!!.role.roleName}",
                      "name": "${response.body()!!.name}"
                    }
                    """.trimIndent()

                    if ("CHEF" == response.body()!!.role.roleName) {

                        val intent = Intent(this@LoginActivity, WebViewActivity::class.java)

                        intent.putExtra(
                            "URL",
                            "${NetworkConfig.FRONTEND_URL}/Frontend/restaurant/public/chef.html"
                        )
                        intent.putExtra(
                            "BACKEND_URL",
                            NetworkConfig.BASE_URL
                        )
                        intent.putExtra("USER_DATA", userJson)
                        startActivity(intent)
                        return@launch
                    } else if ("CASHIER" == response.body()!!.role.roleName) {

                        val intent = Intent(this@LoginActivity, WebViewActivity::class.java)

                        intent.putExtra(
                            "URL",
                            "${NetworkConfig.FRONTEND_URL}/Frontend/restaurant/public/payment.html"
                        )
                        intent.putExtra(
                            "BACKEND_URL",
                            NetworkConfig.BASE_URL
                        )
                        intent.putExtra("USER_DATA", userJson)
                        startActivity(intent)
                        return@launch
                    }

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("USER_DATA", userJson)
                    startActivity(intent)
                    finish()
                } else {
                    Snackbar.make(binding.root, "Error: ${response.code()}", Snackbar.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Error: ${response.code()}")
                }
            }
        }
    }

    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT)
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