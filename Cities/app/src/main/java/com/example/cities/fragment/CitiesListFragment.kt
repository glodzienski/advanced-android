package com.example.cities.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cities.R
import com.example.cities.adapter.CityRecyclerViewAdapter
import com.example.cities.adapter.CitiesContent
import kotlinx.android.synthetic.main.fragment_cities_list.*

class CitiesListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cities_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        item_list.adapter = CityRecyclerViewAdapter(this, CitiesContent.ITEMS)
        super.onActivityCreated(savedInstanceState)
    }
}
