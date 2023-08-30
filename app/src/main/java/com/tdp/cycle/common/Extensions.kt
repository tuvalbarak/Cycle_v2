package com.tdp.cycle.common

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.maps.model.LatLng
import com.tdp.cycle.R
import com.tdp.cycle.models.responses.Location

/** Use this functions to change visibility of a view*/
fun View.gone() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

// This function converts decimal degrees to radians
fun Float.deg2rad(): Float = (this * Math.PI / 180.0f).toFloat()

// This function converts radians to decimal degrees
fun Float.rad2deg(): Float = (this * 180.0f / Math.PI).toFloat()

//LatLng to Location
fun LatLng.toLocation() = Location(latitude, longitude)

//Location to LatLng
fun Location.toLatLng() = LatLng(lat ?: 0.0, lng ?: 0.0)

/** Safely Navigate between fragments
 *  Prevents crushes related to accidentally performing multiple calls to 'navigate'
 *  */
fun NavController.safeNavigate(navDirections: NavDirections?) {
    navDirections?.let { directions ->
        currentDestination?.getAction(directions.actionId)?.run { navigate(directions) }
    }
}

/** Loading image using Glide */
fun ImageView.loadImage(
    src: Any?,
    @DrawableRes placeholder: Int = R.drawable.ic_car,
    shouldRoundImg: Boolean = false
) {
    if (shouldRoundImg) {
        Glide.with(this)
            .load(src)
            .placeholder(placeholder)
            .error(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .circleCrop()
            .into(this)
    } else {
        Glide.with(this)
            .load(src)
            .placeholder(placeholder)
            .error(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(this)
    }
}

/** prints logs with the file name as the tag + TAG as postfix (i.e. 'GeneralExtensionsTAG') */
fun Fragment.logd(message: String?) {
    Log.d("${this::class.java.simpleName}TAG", message ?: "")
}

fun Activity.logd(message: String?) {
    Log.d("${this::class.java.simpleName}TAG", message ?: "")
}

fun Dialog.logd(message: String?) {
    Log.d("${this::class.java.simpleName}TAG", message ?: "")
}

fun Context.logd(message:String?) {
    Log.d("${this::class.java.simpleName}TAG", message ?: "")
}

fun ViewModel.logd(message:String?) {
    Log.d("${this::class.java.simpleName}TAG", message ?: "")
}

fun Fragment.loge(message: String?) {
    Log.e("${this::class.java.simpleName}TAG", message ?: "")
}

fun Context.loge(message:String?) {
    Log.e("${this::class.java.simpleName}TAG", message ?: "")
}

fun Activity.loge(message: String?) {
    Log.e("${this::class.java.simpleName}TAG", message ?: "")
}

fun Dialog.loge(message: String?) {
    Log.e("${this::class.java.simpleName}TAG", message ?: "")
}

fun ViewModel.loge(message: String?) {
    Log.e("${this::class.java.simpleName}TAG", message ?: "")
}

/** Returns true if 'this' is null */
fun Any?.isNull() = this == null

/** Returns true if 'this' is not null */
fun Any?.isNotNull() = this != null


fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}