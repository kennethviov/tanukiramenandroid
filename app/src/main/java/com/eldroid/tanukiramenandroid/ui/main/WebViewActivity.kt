package com.eldroid.tanukiramenandroid.ui.main

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eldroid.tanukiramenandroid.R
import com.eldroid.tanukiramenandroid.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_web_view)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("URL") ?: return
        val backendUrl = intent.getStringExtra("BACKEND_URL") ?: "http://10.0.2.2:8080"

        val userJson = intent.getStringExtra("USER_DATA")

        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {

                    userJson?.let {
                        view?.evaluateJavascript(
                            """
                                localStorage.setItem('currentUser', JSON.stringify($userJson));
                            """.trimIndent(),
                            null
                        )
                    }

                    view?.evaluateJavascript(
                        "window.BACKEND_URL = '$backendUrl';",
                        null
                    )
                }
            }

            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            loadUrl(url)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    // Show confirmation dialog
                    androidx.appcompat.app.AlertDialog.Builder(this@WebViewActivity)
                        .setTitle("Exit")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes") { _, _ ->
                            finish()
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        })
    }

}