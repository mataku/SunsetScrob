package com.mataku.scrobscrob.app.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.GlideApp

class ImageViewBinding {

    @BindingAdapter("imageUrl")
    fun setImageUrl(view: ImageView, url: String) {
        GlideApp.with(view.context)
                .load(url)
                .error(R.drawable.no_image)
                .into(view)
    }
}