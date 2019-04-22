package com.example.webcities.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.webcities.entities.City
import com.example.webcities.ui.fragment.CityDetailFragment
import com.example.webcities.ui.activity.CityDetailActivity
import com.example.webcities.ui.activity.CityListActivity
import kotlinx.android.synthetic.main.city_list_content.view.*
import com.example.webcities.R
import com.example.webcities.utils.ImageBuilder

class CitiesRecyclerViewAdapter (
    private val parentActivity: CityListActivity,
    private val values: List<City>,
    private val twoPane: Boolean
) : RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as City
            if (twoPane) {
                val fragment = CityDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(CityDetailFragment.ARG_ITEM_ID, item.id)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.city_detail_container, fragment)
                    .commit()

                return@OnClickListener
            }

            val intent = Intent(v.context, CityDetailActivity::class.java).apply {
                putExtra(CityDetailFragment.ARG_ITEM_ID, item.id)
            }
            v.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_list_content, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.nomeView.text = item.nome
        holder.paisView.text = item.pais
        if (!item.imagePath.equals("")) {
            holder.imageView.setImageBitmap(ImageBuilder.prepare(item.imagePath, 200, 200))
        }

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeView: TextView = view.txtNome
        val paisView: TextView = view.txtPais
        val imageView: ImageView = view.imageView
    }
}