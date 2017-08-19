package com.mataku.scrobscrob.app.model

import io.realm.Realm
import io.realm.RealmObject
import io.realm.Sort
import io.realm.annotations.PrimaryKey

open class Scrobble(
        @PrimaryKey
        open var id: Long = 0,
        open var artistName: String = "",
        open var trackName: String = "",
        open var albumName: String = "",
        open var timeStamp: Long = 0.toLong(),
        open var artwork: String = ""
) : RealmObject() {
    fun getAll(): List<Scrobble> {
        var limit = 20
        var lowerLimit = 1
        val realm = Realm.getDefaultInstance()
        val scrobblesCount = realm.where(Scrobble::class.java).findAll().count()
        if (scrobblesCount < limit) {
            var limit = scrobblesCount
        } else {
            lowerLimit = scrobblesCount - limit + 1
        }

        val result = realm.where(Scrobble::class.java).between("id", lowerLimit, limit).findAllSortedAsync("id", Sort.DESCENDING)
        return result
    }
}