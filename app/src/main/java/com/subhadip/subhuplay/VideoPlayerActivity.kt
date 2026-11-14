package com.subhadip.subhuplay

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.ComponentActivity

class VideoPlayerActivity : ComponentActivity() {
    private lateinit var webView: WebView
    private var activeChromeClient: WebChromeClient? = null
    private var customView: View? = null
    private var customViewCallback: WebChromeClient.CustomViewCallback? = null
    private var previousSystemUiVisibility: Int = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoId = intent.getStringExtra(EXTRA_VIDEO_ID).orEmpty()
        if (videoId.length != 11 || !videoId.all { it.isLetterOrDigit() || it == '_' || it == '-' }) {
            finish()
            return
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = true
            settings.loadsImagesAutomatically = true
            webViewClient = WebViewClient()
        }

        activeChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                if (view == null) return
                if (customView != null) {
                    callback?.onCustomViewHidden()
                    return
                }
                customView = view
                customViewCallback = callback
                previousSystemUiVisibility = window.decorView.systemUiVisibility
                (window.decorView as ViewGroup).addView(
                    view,
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                webView.visibility = View.GONE
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    )
            }

            override fun onHideCustomView() {
                val shownView = customView ?: return
                (window.decorView as ViewGroup).removeView(shownView)
                customView = null
                webView.visibility = View.VISIBLE
                window.decorView.systemUiVisibility = previousSystemUiVisibility
                customViewCallback?.onCustomViewHidden()
                customViewCallback = null
            }
        }
        webView.webChromeClient = activeChromeClient

        val html = buildString {
            append("<!doctype html><html><head><meta name='viewport' content='width=device-width, initial-scale=1, maximum-scale=1'>")
            append("<style>html,body{margin:0;padding:0;width:100%;height:100%;background:#000;overflow:hidden}iframe{border:0;width:100%;height:100%}</style>")
            append("</head><body><iframe src='https://www.youtube.com/embed/")
            append(videoId)
            append("?playsinline=1&rel=0' title='Video player' allow='accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share' allowfullscreen></iframe>")
            append("</body></html>")
        }

        webView.loadDataWithBaseURL(
            "https://com.subhadip.subhuplay/",
            html,
            "text/html",
            "UTF-8",
            null
        )
        setContentView(webView)
    }

    override fun onPause() {
        if (::webView.isInitialized) webView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (::webView.isInitialized) webView.onResume()
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if (customView != null) {
            activeChromeClient?.onHideCustomView()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::webView.isInitialized) {
            (webView.parent as? ViewGroup)?.removeView(webView)
            webView.removeAllViews()
            webView.destroy()
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onDestroy()
    }

    companion object {
        const val EXTRA_VIDEO_ID = "extra_video_id"
    }
}
