package com.mataku.scrobscrob.app.data

import io.realm.DynamicRealm
import io.realm.RealmMigration

class Migration : RealmMigration {
    override fun migrate(realm: DynamicRealm?, oldVersion: Long, newVersion: Long) {
        val schema = realm?.schema
        schema?.let {
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Migration
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}