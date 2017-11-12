package com.mataku.scrobscrob.app.model

import com.mataku.scrobscrob.app.util.AppUtil
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.Sort
import io.realm.annotations.PrimaryKey

open class Scrobble(
        @PrimaryKey
        open var id: Long = 0,
        open var artistName: String = "",
        open var trackName: String = "",
        open var albumName: String = "",
        open var artwork: String = ""
) : RealmObject() {
    fun getCurrentTracks(): RealmResults<Scrobble> {
        var limit = AppUtil().latestScrobbleCountToDisplay
        var lowerLimit = 1
        val realm = Realm.getDefaultInstance()
        val scrobblesCount = realm.where(Scrobble::class.java).findAll().count()
        if (scrobblesCount < limit) {
            limit = scrobblesCount
        } else {
            lowerLimit = scrobblesCount - limit + 1
        }

        val result = realm.where(Scrobble::class.java).between("id", lowerLimit, limit).findAllSorted("id", Sort.DESCENDING)
        return result
    }

    fun count(): Int {
        val realm = Realm.getDefaultInstance()
        val size = realm.where(Scrobble::class.java).findAll().size
        return size
    }
}