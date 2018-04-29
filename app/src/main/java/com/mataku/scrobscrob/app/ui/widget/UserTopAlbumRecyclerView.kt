package com.mataku.scrobscrob.app.ui.widget

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.airbnb.epoxy.EpoxyRecyclerView
import com.mataku.scrobscrob.R

class UserTopAlbumRecyclerView : EpoxyRecyclerView {
    constructor(
            context: Context?
    ) : this(context, null)

    constructor(
            context: Context?, attrs: AttributeSet?
    ) : this(context, attrs, 0)

    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        val gridLayoutManager = GridLayoutManager(context, 2)
        this.addItemDecoration(AlbumItemDecoration.createDefaultDecoration(context!!))

//        val staggeredLayoutManager = StaggeredGridLayoutManager(12, StaggeredGridLayoutManager.VERTICAL)
//        staggeredLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

//        staggeredLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        this.layoutManager = gridLayoutManager
    }

    private class AlbumItemDecoration(space: Int) : RecyclerView.ItemDecoration() {
        private var space: Int = space

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.top = space
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