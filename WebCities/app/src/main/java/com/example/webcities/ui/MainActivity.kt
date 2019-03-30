package com.example.webcities.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.webcities.R
import com.example.webcities.entities.City
import com.example.webcities.fragment.CityFragment

class MainActivity : AppCompatActivity(), CityFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onListFragmentInteraction(item: City?) {
        item.hashCode()
    }
}
