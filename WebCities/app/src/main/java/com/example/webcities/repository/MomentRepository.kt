package com.example.webcities.repository

import com.example.webcities.entity.Moment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MomentRepository {
    companion object {
        private lateinit var instance: DatabaseReference

        // Design Pattern Singleton
        fun getInstance(): DatabaseReference {
            if (::instance.isInitialized) {
                return instance
            }

            instance = FirebaseDatabase.getInstance().getReference("moment")

            return getInstance()
        }

        fun store(moment: Moment): Moment {
            val key = getInstance().push().key
            moment.id = key!!
            getInstance().child(key).setValue(moment)

            return moment
        }

        fun destroy(moment: Moment) {
            getInstance().child(moment.id).ref.removeValue()
        }

        fun update(moment: Moment) {
            getInstance().child(moment.id).ref.setValue(moment)
        }
    }
}