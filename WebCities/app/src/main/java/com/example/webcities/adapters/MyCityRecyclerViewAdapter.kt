package com.example.webcities.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.example.webcities.fragments.CityFragment.OnListFragmentInteractionListener
import com.example.webcities.R
import com.example.webcities.entities.City

import kotlinx.android.synthetic.main.fragment_city.view.*

class MyCityRecyclerViewAdapter(
    private val mValues: List<City>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyCityRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as City
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_city, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.id
        holder.mNameView.text = item.nome

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mNameView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }
}
