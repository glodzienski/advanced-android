package com.example.cities.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.cities.R
import com.example.cities.fragment.CitiesListFragment
import com.example.cities.model.City
import kotlinx.android.synthetic.main.item_list_content.view.*

class CityRecyclerViewAdapter(
    private val parentActivity: CitiesListFragment,
    private val values: List<City>
) : RecyclerView.Adapter<CityRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        holder.country.text = item.country

        with(holder.itemView) {
            tag = item
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.txtNome
        val country: TextView = view.txtPais
    }
}