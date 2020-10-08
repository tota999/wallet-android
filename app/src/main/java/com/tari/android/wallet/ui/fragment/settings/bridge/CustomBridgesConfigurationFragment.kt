package com.tari.android.wallet.ui.fragment.settings.bridge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tari.android.wallet.databinding.FragmentCustomBridgesConfigurationBinding
import com.tari.android.wallet.ui.extension.ThrottleClick

class CustomBridgesConfigurationFragment @Deprecated(
    """Use newInstance() and supply all the 
necessary data via arguments instead, as fragment's default no-op constructor is used by the 
framework for UI tree rebuild on configuration changes"""
) constructor() : Fragment() {

    private lateinit var ui: FragmentCustomBridgesConfigurationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentCustomBridgesConfigurationBinding.inflate(inflater, container, false)
        .also { ui = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ui.backCtaView.setOnClickListener(ThrottleClick { (requireActivity() as Router).back() })
    }

    interface Router {
        fun back()
    }

    companion object {
        @Suppress("DEPRECATION")
        fun newInstance() = CustomBridgesConfigurationFragment()
    }

}
