package com.example.openedge

import android.graphics.ImageFormat
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment

class CameraFragment : Fragment() {
    private lateinit var reader: ImageReader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupImageReader(640, 480) // MVP resolution
    }

    fun setupImageReader(width: Int, height: Int) {
        reader = ImageReader.newInstance(width, height, ImageFormat.YUV_420_888, 2)
        reader.setOnImageAvailableListener({ r ->
            val image = r.acquireLatestImage() ?: return@setOnImageAvailableListener
            // TODO: convert to NV21 or Bitmap and send to native
            image.close()
        }, Handler(Looper.getMainLooper()))
    }
}
