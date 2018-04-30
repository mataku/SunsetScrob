package com.mataku.scrobscrob.app.ui.widget

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.customtabs.CustomTabsIntent
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.GlideApp
import com.mataku.scrobscrob.app.model.entity.Album
import com.mataku.scrobscrob.databinding.ModelTopAlbumViewBinding
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class UserTopAlbumView : ConstraintLayout {

    private lateinit var binding: ModelTopAlbumViewBinding

    constructor(
            context: Context?
    ) : this(context, null)

    constructor(
            context: Context?, attrs: AttributeSet?
    ) : this(context, attrs, 0)

    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        context ?: return
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.model_top_album_view, this, true)
//        val padding: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F, resources.displayMetrics).toInt()
//        val imageSize = context.resources.displayMetrics.widthPixels / 2 - (padding * 2)
//
//
//        val lp = binding.modelTopAlbumArtwork.layoutParams
//        lp.width = imageSize
//        lp.height = imageSize
//        binding.modelTopAlbumArtwork.layoutParams = lp
    }

    @ModelProp
    fun setAlbum(album: Album) {
        binding.modelTopAlbumArtist.text = album.artist.name
        binding.modelTopAlbumTrack.text = album.name

        val imageUrl = if (album.imageList.size < 3) {
            album.imageList.last().imageUrl
        } else {
            album.imageList[3].imageUrl
        }

        GlideApp.with(context)
                .load(imageUrl)
                .transform(RoundedCornersTransformation(45, 0, RoundedCornersTransformation.CornerType.BOTTOM))
                .fitCenter()
                .error(R.drawable.no_image)
                .into(binding.modelTopAlbumArtwork)

        if (!TextUtils.isEmpty(album.url)) {
            binding.modelTopAlbumCard.setOnClickListener {
                val customTabsIntent = CustomTabsIntent.Builder().build()
                customTabsIntent.launchUrl(context, Uri.parse(album.url))
            }
        }
    }

    @ModelProp
    fun setImageSize(size: Int) {
        val padding: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F, resources.displayMetrics).toInt()
        val lp = binding.modelTopAlbumArtwork.layoutParams
        val imageSize = size - (padding)
        lp.width = imageSize
        lp.height = imageSize
        binding.modelTopAlbumArtwork.layoutParams = lp
    }
}