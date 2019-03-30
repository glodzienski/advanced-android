package com.example.webcities.services

import com.example.webcities.dummy.CitiesContent
import com.example.webcities.entities.City

class CityService {
    companion object {
        fun destroy (city: City) {
            CitiesContent.remove(city)
        }
    }
}