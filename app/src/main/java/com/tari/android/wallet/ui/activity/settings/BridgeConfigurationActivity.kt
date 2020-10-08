package com.tari.android.wallet.ui.activity.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tari.android.wallet.R
import com.tari.android.wallet.ui.fragment.settings.bridge.BridgesConfigurationFragment
import com.tari.android.wallet.ui.fragment.settings.bridge.CustomBridgesConfigurationFragment

class BridgeConfigurationActivity : AppCompatActivity(), BridgesConfigurationFragment.Router,
    CustomBridgesConfigurationFragment.Router {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter_from_bottom, R.anim.exit_to_top)
        setContentView(R.layout.activity_fragment_container)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, BridgesConfigurationFragment.newInstance())
                .commit()
        }
    }

    override fun back() = onBackPressed()

    override fun toCustomBridgesConfiguration() =
        addFragment(
            supportFragmentManager.fragments.single(),
            CustomBridgesConfigurationFragment.newInstance(),
            allowStateLoss = true
        )

    // nyarian:
    // allowStateLoss parameter is necessary to resolve device-specific issues like one
    // for samsung devices with biometrics enabled, as after launching the biometric prompt
    // onSaveInstanceState is called, and commit()ing any stuff after onSaveInstanceState is called
    // results into IllegalStateException: Can not perform this action after onSaveInstanceState
    @Suppress("SameParameterValue")
    private fun addFragment(
        sourceFragment: Fragment,
        fragment: Fragment,
        allowStateLoss: Boolean = false
    ) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right
            )
            .hide(sourceFragment)
            .add(R.id.fragment_container_view, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
            .apply { if (allowStateLoss) commitAllowingStateLoss() else commit() }
    }

}
