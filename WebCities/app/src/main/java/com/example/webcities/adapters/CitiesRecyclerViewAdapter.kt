package com.example.webcities.adapters

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.webcities.entities.City
import com.example.webcities.fragments.CityDetailFragment
import com.example.webcities.ui.CityDetailActivity
import com.example.webcities.ui.CityListActivity
import kotlinx.android.synthetic.main.city_list_content.view.*
import com.example.webcities.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Toast
import com.example.webcities.components.DialogOnConfirm
import com.example.webcities.services.CityService


class CitiesRecyclerViewAdapter (
    private val parentActivity: CityListActivity,
    private val values: List<City>,
    private val twoPane: Boolean
) : RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener
    private val onLongClickListener: View.OnLongClickListener

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
        onLongClickListener = View.OnLongClickListener { v ->
            val item = v.tag as City

            DialogOnConfirm.go(
                parentActivity,
                "Atenção",
                "Deseja excluir a cidade ${item.name}?",
                {
                    CityService.destroy(item)
                    this.notifyDataSetChanged()
                    Toast.makeText(parentActivity, "Cidade ${item.name} excluída com sucesso.", Toast.LENGTH_SHORT).show()
                },
                {

                }
            )
            true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_list_content, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.name

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
            setOnLongClickListener(onLongClickListener)
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.id_text
        val contentView: TextView = view.content
    }
}