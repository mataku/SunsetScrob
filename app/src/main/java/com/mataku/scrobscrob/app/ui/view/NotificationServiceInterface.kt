package com.mataku.scrobscrob.app.ui.view

import com.mataku.scrobscrob.app.model.Track

interface NotificationServiceInterface {
    fun sendTrackInfoToReceiver(track: Track)
}