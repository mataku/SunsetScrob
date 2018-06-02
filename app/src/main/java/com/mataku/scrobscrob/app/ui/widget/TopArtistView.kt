package com.mataku.scrobscrob.app.ui.widget

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.GlideApp
import com.mataku.scrobscrob.app.model.entity.Artist
import com.mataku.scrobscrob.databinding.ModelTopArtistViewBinding

@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class TopArtistView : androidx.constraintlayout.widget.ConstraintLayout {

    private lateinit var binding: ModelTopArtistViewBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.model_top_artist_view, this, true)
    }

    @ModelProp
    fun setArtist(artist: Artist) {
        val image = artist.image

        val imageUrl = if (image == null) {
            null
        } else if (image.size < 3) {
            image.last().imageUrl
        } else {
            image[3].imageUrl
        }
        binding.modelTopArtistName.text = artist.name
        val resources = context.resources
        binding.modelTopArtistPlaycount.text =
                when (artist.playcount) {
                    null -> ""
                    "1" -> resources.getString(R.string.playcount, "1")
                    else -> {
                        resources.getString(R.string.playcounts, artist.playcount)
                    }
                }
        GlideApp.with(context)
                .load(imageUrl)
                .error(R.drawable.no_image)
                .into(binding.modelTopArtistArtwork)

        if (!TextUtils.isEmpty(artist.url)) {
            binding.modelTopArtistCard.setOnClickListener {
                val customTabsIntent = CustomTabsIntent.Builder().build()
                customTabsIntent.launchUrl(context, Uri.parse(artist.url))
            }
        }
    }


    @ModelProp
    fun setImageSize(size: Int) {
        val padding: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F, resources.displayMetrics).toInt()
        val lp = binding.modelTopArtistArtwork.layoutParams
        val imageSize = size - (padding)
        lp.width = imageSize
        lp.height = imageSize
        binding.modelTopArtistArtwork.layoutParams = lp
    }
}