package com.mataku.scrobscrob.core.entity

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
        var limit = 20
        var lowerLimit = 1
        val realm = Realm.getDefaultInstance()
        val scrobblesCount = realm.where(Scrobble::class.java).findAll().count()
        if (scrobblesCount < limit) {
            limit = scrobblesCount
        } else {
            lowerLimit = scrobblesCount - limit + 1
        }

        return realm.where(Scrobble::class.java).between("id", lowerLimit, limit).sort("id", Sort.DESCENDING).findAll()
    }

    fun count(): Int {
        val realm = Realm.getDefaultInstance()
        val size = realm.where(Scrobble::class.java).findAll().size
        return size
    }

    fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.where(Scrobble::class.java).findAll().deleteAllFromRealm()
        }
    }
}