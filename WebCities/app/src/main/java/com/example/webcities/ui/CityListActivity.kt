package com.example.webcities.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.example.webcities.R
import com.example.webcities.adapters.CitiesRecyclerViewAdapter
import com.example.webcities.dummy.CitiesContent
import kotlinx.android.synthetic.main.activity_city_list.*
import kotlinx.android.synthetic.main.city_list.*

class CityListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)

        setSupportActionBar(toolbar)
        toolbar.title = "WebCities"

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
