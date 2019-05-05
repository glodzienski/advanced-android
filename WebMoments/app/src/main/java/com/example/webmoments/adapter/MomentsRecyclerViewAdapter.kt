package com.example.webmoments.adapter

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.webmoments.entity.Moment
import com.example.webmoments.ui.fragment.MomentDetailFragment
import com.example.webmoments.ui.activity.MomentDetailActivity
import com.example.webmoments.ui.activity.MomentListActivity
import kotlinx.android.synthetic.main.moment_list_content.view.*
import com.example.webmoments.R
import com.example.webmoments.ui.activity.MomentFormActivity
import com.example.webmoments.util.DialogOnConfirmUtil
import com.example.webmoments.util.ImageBuilderUtil

class MomentsRecyclerViewAdapter(
    private val parentActivity: MomentListActivity,
    private val values: List<Moment>,
    private val twoPane: Boolean
) : RecyclerView.Adapter<MomentsRecyclerViewAdapter.ViewHolder>() {

    private var onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Moment
            if (twoPane) {
                val fragment = MomentDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(MomentDetailFragment.ARG_ITEM_ID, item.id)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.moment_detail_container, fragment)
                    .commit()

                return@OnClickListener
            }

            val intent = Intent(v.context, MomentDetailActivity::class.java).apply {
                putExtra(MomentDetailFragment.ARG_ITEM_ID, item.id)
            }
            v.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.moment_list_content, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.nomeView.text = item.nome
        holder.descricaoView.text = item.descricao
        if (item.imagePath.isNotEmpty()) {
            holder.imageView.setImageBitmap(ImageBuilderUtil.prepare(item.imagePath, 200, 200))
        }

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
            setOnLongClickListener(View.OnLongClickListener { v ->
                DialogOnConfirmUtil.go(
                    v.context,
                    "Atenção",
                    "Deseja editar o momento ${item.nome}?",
                    {
                        val intent = Intent(v.context, MomentFormActivity::class.java).apply {
                            putExtra("moment_id", item.id)
                        }
                        v.context.startActivity(intent)
                    },
                    {
                        // Do nothing mesmo.
                    }
                )

                true
            })
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeView: TextView = view.txtNome
        val descricaoView: TextView = view.txtDescricao
        val imageView: ImageView = view.imageView
    }
}