package com.mataku.scrobscrob.app.ui.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.airbnb.epoxy.EpoxyRecyclerView
import com.mataku.scrobscrob.R

class TopAlbumRecyclerView : EpoxyRecyclerView {
    constructor(
        context: Context
    ) : this(context, null)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : this(context, attrs, 0)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        context.let {
            val gridLayoutManager = androidx.recyclerview.widget.GridLayoutManager(it, 2)
            this.addItemDecoration(AlbumItemDecoration.createDefaultDecoration(it))

//        val staggeredLayoutManager = StaggeredGridLayoutManager(12, StaggeredGridLayoutManager.VERTICAL)
//        staggeredLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

//        staggeredLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            this.layoutManager = gridLayoutManager
        }
    }

    private class AlbumItemDecoration(space: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
        private var space: Int = space

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: androidx.recyclerview.widget.RecyclerView,
            state: androidx.recyclerview.widget.RecyclerView.State
        ) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
        }

        companion object {
            fun createDefaultDecoration(context: Context): AlbumItemDecoration {
                val spacingInPixels = context.resources.getDimensionPixelSize(R.dimen.card_view_spacing)
                return AlbumItemDecoration(spacingInPixels)
            }
        }
    }
}