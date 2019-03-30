package com.example.webcities.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.example.webcities.R
import com.example.webcities.adapter.CitiesRecyclerViewAdapter
import com.example.webcities.dummy.CitiesContent
import kotlinx.android.synthetic.main.activity_city_list.*
import kotlinx.android.synthetic.main.city_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [CityDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class CityListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        btnNew.setOnClickListener { view ->
            // TODO abre uma nova tela, onde será possivel realizar o cadastro de uma nova cidade
        }

        if (city_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(city_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter =
            CitiesRecyclerViewAdapter(this, CitiesContent.ITEMS, twoPane)
    }
}
