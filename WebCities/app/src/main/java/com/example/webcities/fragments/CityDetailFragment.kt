package com.example.webcities.fragments

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

/**
 * A fragment representing a single City detail screen.
 * This fragment is either contained in a [CityListActivity]
 * in two-pane mode (on tablets) or a [CityDetailActivity]
 * on handsets.
 */
class CityDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: City? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = CitiesContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
                activity?.toolbar_layout?.title = item?.name
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.city_detail, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            rootView.city_detail.text = it.pais
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
