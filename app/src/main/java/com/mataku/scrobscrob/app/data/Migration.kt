package com.mataku.scrobscrob.app.data

import io.realm.DynamicRealm
import io.realm.RealmMigration
import io.realm.RealmObjectSchema
import io.realm.RealmSchema

class Migration : RealmMigration {
    override fun migrate(realm: DynamicRealm?, oldVersion: Long, newVersion: Long) {
        val schema: RealmSchema = realm!!.schema
        if (oldVersion == 0L) {
            /*
             *  Schema Version 0 to 1
             *   removed timeStamp: Long
             *
             *  app Version <= 0.0.2
             */
            val scrobbleSchema: RealmObjectSchema = schema.get("Scrobble")
            scrobbleSchema.removeField("timeStamp")
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Migration
    }
}