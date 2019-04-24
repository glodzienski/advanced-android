package com.example.webcities.repositories

import com.example.webcities.entities.City
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CityRepository {
    companion object {
        private lateinit var instance: DatabaseReference

        // Design Pattern Singleton
        fun getInstance(): DatabaseReference {
            if (::instance.isInitialized) {
                return instance
            }

            instance = FirebaseDatabase.getInstance().getReference("cities")

            return getInstance()
        }

        fun store(city: City): City {
            val key = getInstance().push().key
            city.id = key!!
            getInstance().child(key).setValue(city)

            return city
        }

        fun destroy(city: City) {
            getInstance().child(city.id).ref.removeValue()
        }

        fun update(city: City) {
            getInstance().child(city.id).ref.setValue(city)
        }
    }
}