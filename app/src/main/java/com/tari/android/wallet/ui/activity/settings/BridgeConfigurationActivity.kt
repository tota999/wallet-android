package com.tari.android.wallet.ui.activity.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tari.android.wallet.R
import com.tari.android.wallet.ui.fragment.settings.bridge.BridgesConfigurationFragment

class BridgeConfigurationActivity : AppCompatActivity() {

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

}
