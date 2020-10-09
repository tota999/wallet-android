package com.tari.android.wallet.ui.fragment.settings.bridge

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.orhanobut.logger.Logger
import com.tari.android.wallet.databinding.FragmentCustomBridgesConfigurationBinding
import com.tari.android.wallet.ui.extension.ThrottleClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


class CustomBridgesConfigurationFragment @Deprecated(
    """Use newInstance() and supply all the 
necessary data via arguments instead, as fragment's default no-op constructor is used by the 
framework for UI tree rebuild on configuration changes"""
) constructor() : Fragment() {

    private lateinit var ui: FragmentCustomBridgesConfigurationBinding

    private val router get() = requireActivity() as Router

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentCustomBridgesConfigurationBinding.inflate(inflater, container, false)
        .also { ui = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ui.backCtaView.setOnClickListener(ThrottleClick { router.back() })
        ui.scanBridgesQrCtaView.setOnClickListener(ThrottleClick {
            afterDelay { router.toQRScanner() }
        })
        ui.uploadBridgesQrCtaView.setOnClickListener(ThrottleClick {
            afterDelay { router.toImageChooser() }
        })
    }

    private fun afterDelay(action: () -> Unit) {
        lifecycleScope.launchWhenResumed {
            delay(100L)
            withContext(Dispatchers.Main) { action() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val arguments = arguments!!
            when (requestCode) {
                arguments.getInt(QR_REQUEST_CODE_KEY) ->
                    data.getStringExtra(arguments.getString(QR_DATA_KEY_KEY))
                        ?.let(::parseBridgesFromQRCodeResult)
                arguments.getInt(IMAGE_REQUEST_CODE_KEY) ->
                    extractQRCodeContentFromImage(data.data!!)?.let(::parseBridgesFromQRCodeResult)
            }
        }
    }

    private fun extractQRCodeContentFromImage(data: Uri): String? {
        val inputStream = requireContext().contentResolver.openInputStream(data)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val intArray = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        val binaryBitmap =
            BinaryBitmap(HybridBinarizer(RGBLuminanceSource(bitmap.width, bitmap.height, intArray)))
        return try {
            MultiFormatReader().decode(binaryBitmap).text
        } catch (e: Exception) {
            Logger.e(e, "Error decoding barcode")
            null
        }
    }

    private fun parseBridgesFromQRCodeResult(result: String) {
        Toast.makeText(requireContext(), result, Toast.LENGTH_LONG).show()
    }

    interface Router {
        fun toQRScanner()
        fun toImageChooser()
        fun back()
    }

    companion object {
        private const val QR_REQUEST_CODE_KEY = "qr_request_code"
        private const val QR_DATA_KEY_KEY = "qr_data_key"
        private const val IMAGE_REQUEST_CODE_KEY = "image_request_code"

        @Suppress("DEPRECATION")
        fun newInstance(
            qrRequestCode: Int,
            qrDataKey: String,
            imageRequestCode: Int,
        ) =
            CustomBridgesConfigurationFragment().also {
                it.arguments = Bundle().apply {
                    putInt(QR_REQUEST_CODE_KEY, qrRequestCode)
                    putString(QR_DATA_KEY_KEY, qrDataKey)
                    putInt(IMAGE_REQUEST_CODE_KEY, imageRequestCode)
                }
            }
    }

}
