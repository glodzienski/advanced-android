package com.example.myfirstproject.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myfirstproject.R
import com.example.myfirstproject.model.Cidade

import kotlinx.android.synthetic.main.fragment_cidade.view.*

class ListaCidadeAdapter(
    private val context: Context,
    private val listaCidades: List<Cidade>
) : RecyclerView.Adapter<ListaCidadeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.cidade_item, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listaCidades.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaCidades[position]

        holder.let {
            it.nome.text = item.nome
            it.pais.text = item.pais
            // evento do botão, que vai passar informações pro fragmento de edição. ou de visualização.
        }
    }

    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val nome: TextView = mView.item_number
        val pais: TextView = mView.item_number
//        val foto: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + nome.text + "'"
        }
    }
}
