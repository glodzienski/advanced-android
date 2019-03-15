package com.example.myfirstproject.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myfirstproject.R
import com.example.myfirstproject.model.Cidade

class CidadeListaFragment : Fragment() {

    private val listaCidade: List<Cidade> = getData()

    lateinit var listaCidadeRecycle: RecyclerView

    private fun getData(): List<Cidade> {
        return listOf(
            Cidade("teste"),
            Cidade("teste"),
            Cidade("teste")
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(R.layout.fragment_cidade_list, container)

        return inflate
    }
}
