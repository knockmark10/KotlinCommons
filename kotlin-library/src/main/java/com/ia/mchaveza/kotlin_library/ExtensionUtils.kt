package com.ia.mchaveza.kotlin_library

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.support.annotation.AnimRes
import android.support.annotation.AnimatorRes
import android.support.annotation.IdRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer
import se.simbio.encryption.Encryption
import java.lang.Exception
import java.util.*

/**
 * Created by mchaveza on 22/12/2017.
 */
fun Drawable.changeDrawableColor(context: Context, color: Int): Drawable {
    val wrappedDrawable = DrawableCompat.wrap(this)
    DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, color))
    DrawableCompat.setTintMode(wrappedDrawable, PorterDuff.Mode.SRC_IN)
    return wrappedDrawable
}

fun FragmentManager.performReplacingTransaction(@IdRes containerViewId: Int, fragment: Fragment,
                                                @AnimRes @AnimatorRes enterAnim: Int? = null, @AnimRes @AnimatorRes exitAnim: Int? = null,
                                                @AnimRes @AnimatorRes popEnterAnim: Int? = null, @AnimRes @AnimatorRes popExitAnim: Int? = null,
                                                backStackTag: String? = null, allowStateLoss: Boolean? = null) {
    val transaction = this.beginTransaction()
    if (enterAnim != null && exitAnim != null) {
        if (popEnterAnim != null && popExitAnim != null) {
            transaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        } else {
            transaction.setCustomAnimations(enterAnim, exitAnim)
        }
    }
    transaction.replace(containerViewId, fragment)
    if (backStackTag != null) {
        transaction.addToBackStack(backStackTag)
    }
    if (allowStateLoss != null && allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

fun FragmentManager.performAddingTransaction(@IdRes containerViewId: Int, fragment: Fragment,
                                             @AnimRes @AnimatorRes enterAnim: Int? = null, @AnimRes @AnimatorRes exitAnim: Int? = null,
                                             @AnimRes @AnimatorRes popEnterAnim: Int? = null, @AnimRes @AnimatorRes popExitAnim: Int? = null,
                                             backStackTag: String? = null, allowStateLoss: Boolean? = null) {
    val transaction = this.beginTransaction()
    if (enterAnim != null && exitAnim != null) {
        if (popEnterAnim != null && popExitAnim != null) {
            transaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        } else {
            transaction.setCustomAnimations(enterAnim, exitAnim)
        }
    }
    transaction.add(containerViewId, fragment)
    if (backStackTag != null) {
        transaction.addToBackStack(backStackTag)
    }
    if (allowStateLoss != null && allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

fun FragmentManager.removeLastFragment() {
    this.popBackStack()
}

fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) + start

fun ClosedRange<Float>.random() =
        start + Random().nextFloat() * (endInclusive - start)

fun ClosedRange<Double>.random() =
        start + Random().nextDouble() * (endInclusive - start)

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.setDrawableBackground(drawableResId: Int) {
    this.background = ContextCompat.getDrawable(this.context, drawableResId)
}

fun View.setBackgroundColor(colorResId: Int) {
    this.setBackgroundColor(ContextCompat.getColor(this.context, colorResId))
}

fun ViewGroup.inflate(layoutRes: Int): View =
        LayoutInflater.from(context).inflate(layoutRes, this, false)

fun Activity.windowHeight(): Int {
    val display = this.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}

fun Activity.windowWidth(): Int {
    val display = this.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}

fun Activity.hideSoftKeyboard(view: View?) {
    view?.let {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun ImageView.loadUrl(url: String, requestOptions: RequestOptions, placeholder: Int = R.drawable.ic_placeholder, error: Int = R.drawable.ic_placeholder) {
    Glide.with(context)
            .load(url)
            .apply(requestOptions
                    .placeholder(placeholder)
                    .error(error))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
}

fun ImageView.loadCircularView(url: String? = null, bitmap: Bitmap? = null, placeholder: Int = R.drawable.ic_rounded_placeholder, error: Int = R.drawable.ic_rounded_placeholder) {
    val res = url ?: bitmap
    Glide.with(this.context)
            .load(res)
            .apply(RequestOptions.circleCropTransform()
                    .placeholder(placeholder)
                    .error(error))
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions.skipMemoryCacheOf(true))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(this)
}

fun ImageView.roundCorners(url: String, cornerRadius: Int) {
    val config = ImageLoaderConfiguration.Builder(context)
            .diskCacheFileNameGenerator(HashCodeFileNameGenerator())
            .build()

    val options = DisplayImageOptions.Builder()
            .displayer(RoundedBitmapDisplayer(cornerRadius))
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build()

    val imageLoader = ImageLoader.getInstance()
    imageLoader.init(config)
    imageLoader.displayImage(url, this, options)
}

fun ImageView.loadBarcode(string: String, barcodeFormat: BarcodeFormat, width: Int = 250, height: Int = 100) {
    val multiFormatWriter = MultiFormatWriter()
    try {
        val bitMatrix = multiFormatWriter.encode(string, barcodeFormat, width, height)
        val barcodeEncoder = BarcodeEncoder()
        val qrCode = barcodeEncoder.createBitmap(bitMatrix)
        this.setImageBitmap(qrCode)
    } catch (writerException: WriterException) {
        writerException.printStackTrace()
    }
}

fun Drawable.overrideColor(backgroundColor: Int) {
    when (this) {
        is GradientDrawable -> setColor(backgroundColor)
        is ShapeDrawable -> paint.color = backgroundColor
        is ColorDrawable -> color = backgroundColor
    }
}

fun View.snack(message: String, length: Int = Snackbar.LENGTH_INDEFINITE) {
    val snack = Snackbar.make(this, message, length)
    snack.setAction(android.R.string.ok, null)
    snack.show()
}

fun String.encrypt(): String =
        CryptoUtils.getInstance().encryptOrNull(this) ?: ""

fun String.customEncrypt(key: String, salt: String, iv: ByteArray) =
        CryptoUtils.getCustomInstance(key, salt, iv).encryptOrNull(this) ?: ""

fun String.decrypt(): String =
        CryptoUtils.getInstance().decryptOrNull(this) ?: ""

fun String.customDecrypt(key: String, salt: String, iv: ByteArray) =
        CryptoUtils.getCustomInstance(key, salt, iv).decryptOrNull(this) ?: ""

fun String.encryptAsync(listener: CryptoUtilsCallback) {
    val encryption = CryptoUtils.getInstance()
    encryption.encryptAsync(this, object : Encryption.Callback {
        override fun onSuccess(p0: String?) {
            listener.onSuccess(p0 ?: "")
        }

        override fun onError(p0: Exception?) {
            listener.onError(Throwable(p0?.message.toString()))
        }
    })
}

fun String.decryptAsync(listener: CryptoUtilsCallback) {
    val decryption = CryptoUtils.getInstance()
    decryption.decryptAsync(this, object : Encryption.Callback {
        override fun onSuccess(p0: String?) {
            listener.onSuccess(p0 ?: "")
        }

        override fun onError(p0: Exception?) {
            listener.onError(Throwable(p0?.message.toString()))
        }
    })
}

fun Activity.getAppVersion(type: AppVersion): String {
    return try {
        val packageInfo = this.packageManager?.getPackageInfo(this.packageName, 0)
        when (type) {
            AppVersion.VersionName -> {
                packageInfo?.versionName ?: ""
            }
            AppVersion.VersionCode -> {
                packageInfo?.versionCode.toString()
            }
        }
    } catch (exception: Exception) {
        throw UnsupportedOperationException("Could not get app version. Caused by getAppVersion kotlin extension")
    }
}

enum class AppVersion {
    VersionName,
    VersionCode
}