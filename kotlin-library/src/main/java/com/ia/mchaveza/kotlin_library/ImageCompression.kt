package com.ia.mchaveza.kotlin_library

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Base64
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.DecimalFormat

@Deprecated("Use ImageHandler instead for better performance and easier integration.")
class ImageCompression(private val mContext: Context, private val mListener: ImageCompressionCallback) {

    @Deprecated("Use ImageHandler's methods instead for better performance and easier integration.")
    fun compressImage(uri: Uri) {
        val chosenImage = FileUtils.from(mContext, uri)
        customCompressImage(chosenImage)
    }

    private fun customCompressImage(actualImage: File) {
        try {
            val compressedImage = Compressor(mContext)
                    .setMaxWidth(480)
                    .setMaxHeight(480)
                    .setQuality(75)
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
        mListener.onImageCompressed(bitmapImage,
                getReadableFileSize(compressedImage.length()),
                encodeImage(bitmapImage))
    }

    private fun compressImageSafe(actualImage: File) {
        Compressor(mContext)
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                .compressToFileAsFlowable(actualImage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setupResourcesToReturn(it)
                }, {
                    mListener.onImageCompressedError(it)
                })
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

    @Deprecated("Use ImageHandler's interface instead for better performance and easier integration.")
    interface ImageCompressionCallback {
        fun onImageCompressed(image: Bitmap, fileSize: String, base64Image: String)
        fun onImageCompressedError(error: Throwable)
    }

}