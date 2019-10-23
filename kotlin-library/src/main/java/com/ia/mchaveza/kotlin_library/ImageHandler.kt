package com.ia.mchaveza.kotlin_library

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.text.format.DateFormat
import android.util.Base64
import android.view.View
import com.android.permissionlibrary.callbacks.PermissionCallback
import com.android.permissionlibrary.managers.PermissionManager
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.util.*

class ImageHandler(private val activity: Activity,
                   private val screenShotListener: ScreenShotHandlerCallback? = null,
                   private val compressionListener: ImageHandlerCallback? = null) : PermissionCallback {

    private var quality = 100
    private var path: String? = null
    private var bitmap: Bitmap? = null
    private var actualImage: File = File("")
    private var currentTask = CurrentTask.None
    private val permissionManager by lazy { PermissionManager(activity, this) }

    /**
     * Takes a screenshot of current displaying screen.
     * You can choose whether you want to save it or not.
     * @param saveIt is true, listener must be provided in constructor
     */
    @Suppress("DEPRECATION")
    fun takeScreenShot(saveIt: Boolean): Bitmap {
        currentTask = CurrentTask.Screenshot

        val view = activity.window.decorView.rootView
        view.isDrawingCacheEnabled = true
        view.measure(View.MeasureSpec.makeMeasureSpec(activity.windowWidth(), View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(activity.windowHeight(), View.MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        view.buildDrawingCache(true)
        bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false

        if (saveIt) {
            checkScreenShotListener()

            val now = Date()
            DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
            path = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg"
            saveScreenShot()
        }

        return bitmap ?: Bitmap.createBitmap(300, 300, Bitmap.Config.ALPHA_8)
    }

    /**
     * It compresses image with custom quality. It also checks if permissions are greanted or not,
     * and request them (if needed). It also performs alternative methods for compressing image
     * without quality loss
     */
    fun compressImage(uri: Uri, desiredQuality: Int) {
        checkImageHandlerListener()
        currentTask = CurrentTask.Compress
        this.quality = desiredQuality
        actualImage = FileUtils.from(activity, uri)

        if (!permissionManager.isPermissingPresentInManifest(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            throw SecurityException("Manifest permission missing. You need to declare WRITE_EXTERNAL_STORAGE permission in the manifest to use this feature.")
        }

        if (permissionManager.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            customCompressImage(actualImage, desiredQuality)
        } else {
            permissionManager.requestSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    /**
     * Compress image with custom compress format. The quality will remain the same
     */
    fun compressImageSafe(actualImage: File, compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG) {
        checkImageHandlerListener()
        Compressor(activity)
                .setCompressFormat(compressFormat)
                .compressToFileAsFlowable(actualImage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setupResourcesToReturn(it)
                }, {
                    compressionListener?.onImageCompressedError(it)
                })
    }

    /**
     * To build your custom image compression. You can check
     * @link https://github.com/zetbaitsu/Compressor
     * for further information
     */
    fun getCompressorInstance() =
            Compressor(activity)

    private fun saveImage() {
        val imageFile = File(path)
        val outputStream = FileOutputStream(imageFile)
        val quality = 100
        bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    private fun saveScreenShot() {
        if (permissionManager.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            saveImage()
        } else {
            permissionManager.requestSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun customCompressImage(actualImage: File, quality: Int) {
        try {
            val compressedImage = Compressor(activity)
                    .setMaxWidth(480)
                    .setMaxHeight(480)
                    .setQuality(quality)
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).absolutePath)
                    .compressToFile(actualImage)
            setupResourcesToReturn(compressedImage)
        } catch (exception: IOException) {
            compressImageSafe(actualImage)
        }
    }

    private fun setupResourcesToReturn(compressedImage: File) {
        val bitmapImage = transformCompressedImageToBitmap(compressedImage)
        compressionListener?.onImageCompressed(bitmapImage,
                getReadableFileSize(compressedImage.length()),
                encodeImage(bitmapImage))
    }


    private fun transformCompressedImageToBitmap(image: File): Bitmap =
            BitmapFactory.decodeFile(image.absolutePath)

    private fun getReadableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }

    private fun encodeImage(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override fun onPermissionGranted(permission: String) {
        when (permission) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                when (currentTask) {
                    CurrentTask.Screenshot -> {
                        saveImage()
                    }
                    CurrentTask.Compress -> {
                        customCompressImage(actualImage, quality)
                    }
                    CurrentTask.None -> {
                    }
                }
            }
        }
    }

    override fun onPermissionDenied(permission: String) {
        when (permission) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                when (currentTask) {
                    CurrentTask.Compress -> {
                        compressImageSafe(actualImage)
                    }
                    CurrentTask.Screenshot -> {
                        screenShotListener?.onScreenShotError(Throwable("Write storage permission was denied. Therefore screenshot could not be saved."))
                    }
                    CurrentTask.None -> {
                    }
                }
            }
        }
    }

    private fun checkScreenShotListener() {
        if (screenShotListener == null) {
            throw NullPointerException("ScreenShotHandlerCallback interface required. You need to pass screenShotListener in constructor.")
        }
    }

    private fun checkImageHandlerListener() {
        if (compressionListener == null) {
            throw NullPointerException("ImageHandlerCallback interface required. You need to pass compressionListener in constructor.")
        }
    }

    interface ImageHandlerCallback {
        fun onImageCompressed(image: Bitmap, fileSize: String, base64Image: String)
        fun onImageCompressedError(error: Throwable)
    }

    interface ScreenShotHandlerCallback {
        fun onScreenShotError(error: Throwable)
    }

}

enum class CurrentTask {
    Screenshot,
    Compress,
    None
}

