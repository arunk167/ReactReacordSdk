package com.example.reactsdk.screen_recording_v1


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.motion.widget.MotionLayout


/* The `@BindingAdapter("visibleView")` annotation is used to create a custom binding adapter in
Kotlin. This binding adapter is used to bind a boolean value to the visibility property of a view. */




//@BindingAdapter("changeColorsOfLikeDislike")
//fun LMSImageView.changeColorsOfTheView(type: Int) {
//    if (type==1) {
//        this.changeBackground(ThemeConstants.TYPE_THEME)
//    } else{
//
//        this.imageTintList = null
//
//    }
//}

/**
 * The function "sad" sets the visibility mode of a view in a MotionLayout to "ignore" and hides the
 * view.
 *
 * @param view The "view" parameter is the view that you want to modify the visibility of. It can be
 * any type of view, such as a TextView, ImageView, or Button.
 * @param motionLayout The `motionLayout` parameter is an instance of the `MotionLayout` class. It is
 * used to define and manage the motion and transitions between different states of a layout.
 */
fun View.visibleView(show: Boolean) {
    visibility = if (show) {
        View.VISIBLE
    } else View.GONE
}

/* The `fun View.inVisibleView(isShow: Boolean)` function is an extension function for the `View` class
in Kotlin. It is used to set the visibility of a view to either `View.VISIBLE` or `View.INVISIBLE`
based on the value of the `isShow` parameter. */
fun View.inVisibleView(isShow: Boolean) {
    visibility = if (isShow) {
        View.VISIBLE
    } else View.INVISIBLE
}

/* The `fun View.invisible() { visibility = View.INVISIBLE }` function is an extension function for the
`View` class in Kotlin. It sets the visibility of the view to `View.INVISIBLE`, which means the view
will be invisible but still take up space in the layout. */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/* The `fun View.gone() { visibility = View.GONE }` function is an extension function for the `View`
class in Kotlin. It sets the visibility of the view to `View.GONE`, which means the view will be
invisible and will not take up any space in the layout. */
fun View.gone() {
    visibility = View.GONE
}

/* The `fun View.visible() { visibility = View.VISIBLE }` function is an extension function for the
`View` class in Kotlin. It sets the visibility of the view to `View.VISIBLE`, which means the view
will be visible on the screen. */
fun View.visible() {
    visibility = View.VISIBLE
}

/* The `fun Int?.isNullOrZero(): Boolean` function is an extension function for the `Int` class in
Kotlin. It is used to check if an `Int` value is either null or equal to zero. */
fun Int?.isNullOrZero(): Boolean {
    return this == null || this == 0
}

/* The `fun Long?.isNullOrZero(): Boolean` function is an extension function for the `Long` class in
Kotlin. It is used to check if a `Long` value is either null or equal to zero. */
fun Long?.isNullOrZero(): Boolean {
    return this == null || this == 0L
}

/* The `fun Long?.isNullOrNegative(): Boolean` function is an extension function for the `Long` class
in Kotlin. It is used to check if a `Long` value is either null or negative. If the value is null or
less than 0, the function returns true, indicating that the value is either null or negative.
Otherwise, it returns false. */
fun Long?.isNullOrNegative(): Boolean {
    return this == null || this < 0L
}

/* The `fun Int?.isNullOrNegative(): Boolean` function is an extension function for the `Int` class in
Kotlin. It is used to check if an `Int` value is either null or equal to -1. If the value is null or
-1, the function returns true, indicating that the value is either null or negative. Otherwise, it
returns false. */
fun Int?.isNullOrNegative(): Boolean {
    return this == null || this == -1
}

/* The `fun Double?.isNullOrZero(): Boolean` function is an extension function for the `Double` class
in Kotlin. It is used to check if a `Double` value is either null or equal to zero. */
fun Double?.isNullOrZero(): Boolean {
    return this == null || this == 0.0
}

/* The `fun Double?.isNullOrNegative(): Boolean` function is an extension function for the `Double`
class in Kotlin. It is used to check if a `Double` value is either null or negative. */
fun Double?.isNullOrNegative(): Boolean {
    return this == null || this == -1.0
}

/* The `fun Int?.getCharString(): String` function is an extension function for the `Int` class in
Kotlin. It is used to convert an integer value to its corresponding character string representation. */
fun Int?.getCharString(): String {
    if (this == null)
        return ""
    return (this + 65).toChar().toString()
}

/* The `fun Int?.milliSecToMin():Int` function is an extension function for the `Int` class in Kotlin.
It is used to convert milliseconds to minutes. */
fun Int?.milliSecToMin(): Int {
    if (this == null)
        return 0
    return (this / 1000 / 60)
}

/* The `fun String.wordCount(): Int` function is an extension function for the `String` class in
Kotlin. It is used to count the number of words in a given string. */
fun String.wordCount(): Int {
    val trimmedStr = trim()
    return if (trimmedStr.isEmpty()) {
        0
    } else {
        trimmedStr.split("\\s+".toRegex()).size
    }
}


/* The `fun Context.getQuizTypeTitle(type: Int): String` function is an extension function for the
`Context` class in Kotlin. It is used to get the title of a quiz type based on the given type
parameter. */

/* The `fun Context.getQuantityString(resId: Int, quantity: Int?): String` function is an extension
function for the `Context` class in Kotlin. It is used to get a formatted string from the resources
based on a given quantity. */
fun Context.getQuantityString(resId: Int, quantity: Int?): String {
    return resources.getQuantityString(
        resId,
        quantity ?: 0,
        quantity ?: 0
    )
}


/* The `applyPrimaryTint()` function is an extension function for the `ProgressBar` class. It sets the
tint color and tint mode for the progress and indeterminate drawables of the `ProgressBar` to the
primary color of the app theme. */





