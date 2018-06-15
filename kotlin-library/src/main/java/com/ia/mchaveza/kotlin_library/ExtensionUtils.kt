package com.ia.mchaveza.kotlin_library

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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

fun FragmentManager.replaceFragment(containerViewId: Int, fragment: Fragment) {
    this
            .beginTransaction()
            .replace(containerViewId, fragment)
            .commit()
}

fun FragmentManager.replaceFragmentAllowingStateLoss(containerViewId: Int, fragment: Fragment) {
    this
            .beginTransaction()
            .replace(containerViewId, fragment)
            .commitAllowingStateLoss()
}

fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) + start

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ViewGroup.inflate(layoutRes: Int): View =
        LayoutInflater.from(context).inflate(layoutRes, this, false)

fun View.setDrawableBackground(drawableResId: Int) {
    this.background = ContextCompat.getDrawable(this.context, drawableResId)

}

fun View.setColorBackground(colorResId: Int) {
    this.setBackgroundColor(ContextCompat.getColor(this.context, colorResId))
}

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




