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
    private var renderer: GLRenderer? = null
    private var glView: android.opengl.GLSurfaceView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        glView = view.findViewById<android.opengl.GLSurfaceView>(com.example.openedge.R.id.glSurfaceView)
        renderer = GLRenderer()
        glView?.setEGLContextClientVersion(2)
        glView?.setRenderer(renderer)
        glView?.renderMode = android.opengl.GLSurfaceView.RENDERMODE_WHEN_DIRTY
        setupImageReader(640, 480) // MVP resolution
    }

    fun setupImageReader(width: Int, height: Int) {
        val backgroundHandler = Handler(Looper.getMainLooper())
        reader = ImageReader.newInstance(width, height, ImageFormat.YUV_420_888, 2)
        reader.setOnImageAvailableListener({ r ->
            val image = r.acquireLatestImage() ?: return@setOnImageAvailableListener
            val nv21 = imageToNV21(image)
            image.close()

            // Call native function
            val processedRGBA = NativeBridge.processNV21(nv21, width, height, true)

            // Pass processedRGBA to OpenGL texture
            renderer?.updateFrame(processedRGBA!!, width, height)
            glView?.requestRender()
        }, backgroundHandler)
    }

    private fun imageToNV21(image: android.media.Image): ByteArray {
        val width = image.width
        val height = image.height
        val ySize = width * height
        val uvSize = width * height / 2
        val nv21 = ByteArray(ySize + uvSize)

        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        var yPos = 0
        while (yBuffer.hasRemaining()) {
            nv21[yPos++] = yBuffer.get()
        }

        val pixelStride = vBuffer.remaining() / (width * height / 4)
        val rowStride = image.planes[1].rowStride
        var uvPos = ySize
        for (j in 0 until height / 2) {
            for (i in 0 until width / 2) {
                nv21[uvPos++] = vBuffer.get(j * rowStride + i * pixelStride)
                nv21[uvPos++] = uBuffer.get(j * rowStride + i * pixelStride)
            }
        }

        return nv21
    }
}
