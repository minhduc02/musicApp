package com.minhduc202.musicapp.extension

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

fun View.isUserInteractionEnabled(enabled: Boolean) {
    isEnabled = enabled
    if (this is ViewGroup && this.childCount > 0) {
        this.children.forEach {
            it.isUserInteractionEnabled(enabled)
        }
    }
}