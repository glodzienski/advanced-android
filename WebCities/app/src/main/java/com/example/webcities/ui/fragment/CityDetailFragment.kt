package com.example.webcities.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.webcities.R
import com.example.webcities.dummy.CitiesContent
import com.example.webcities.entities.City
import kotlinx.android.synthetic.main.activity_city_detail.*
import kotlinx.android.synthetic.main.city_detail.view.*

class CityDetailFragment : Fragment() {

    private lateinit var item: City

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = CitiesContent.ITEM_MAP[it.getString(ARG_ITEM_ID)] as City
                activity?.toolbar_layout?.title = item?.nome
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.city_detail, container, false)

        item?.let {
            rootView.city_name.text = it.nome
            rootView.city_country.text = it.pais
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}
