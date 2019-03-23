package com.mataku.scrobscrob.app.ui.extension

import android.content.Context
import android.view.Gravity
import android.widget.Toast

fun Context.showToastAtCenter(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).also {
        it.setGravity(Gravity.CENTER, 0, 0)
    }.show()
}