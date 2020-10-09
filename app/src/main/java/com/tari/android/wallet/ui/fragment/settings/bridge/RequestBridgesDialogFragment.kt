package com.tari.android.wallet.ui.fragment.settings.bridge

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import com.tari.android.wallet.R
import com.tari.android.wallet.databinding.FragmentRequestBridgesBinding

class RequestBridgesDialogFragment @Deprecated(
    """Use newInstance() and supply all the 
necessary data via arguments instead, as fragment's default no-op constructor is used by the 
framework for UI tree rebuild on configuration changes"""
) constructor() : DialogFragment() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RequestBridgesDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        FragmentRequestBridgesBinding.inflate(inflater, container, false)
            .also { webView = it.webView }.root

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(TOR_URL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView.destroy()
    }

    companion object {
        private const val TOR_URL = "https://bridges.torproject.org/bridges"

        @Suppress("DEPRECATION")
        fun newInstance() = RequestBridgesDialogFragment()
    }

}
