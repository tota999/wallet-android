package com.tari.android.wallet.ui.fragment.settings.bridge

import androidx.fragment.app.Fragment

class BridgesConfigurationFragment @Deprecated(
    """Use newInstance() and supply all the 
necessary data via arguments instead, as fragment's default no-op constructor is used by the 
framework for UI tree rebuild on configuration changes"""
) constructor() : Fragment() {

    companion object {
        @Suppress("DEPRECATION")
        fun newInstance() = BridgesConfigurationFragment()
    }

}
