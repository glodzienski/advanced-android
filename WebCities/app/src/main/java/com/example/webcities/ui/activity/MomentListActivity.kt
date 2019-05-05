package com.example.webcities.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import com.example.webcities.R
import com.example.webcities.adapter.MomentsRecyclerViewAdapter
import com.example.webcities.dummy.MomentsContent
import kotlinx.android.synthetic.main.activity_city_list.*
import kotlinx.android.synthetic.main.city_list.*
import com.example.webcities.entity.Moment
import com.example.webcities.repository.MomentRepository
import com.google.firebase.database.*


class MomentListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false
    private lateinit var instance: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)

        setSupportActionBar(toolbar)
        toolbar.title = "WebCities"

        instance = MomentRepository.getInstance()

        btnNew.setOnClickListener { view ->
            val intent = Intent(view.context, MomentFormActivity::class.java)
            view.context.startActivity(intent)
        }

        if (city_detail_container != null) {
            twoPane = true
        }
    }

    override fun onResume() {
        seedList()
        setupRecyclerView(city_list)
        setRecyclerViewItemTouchListener(city_list)
        super.onResume()
    }

    private fun seedList() {
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                MomentsContent.clear()

                dataSnapshot.children.mapNotNullTo(MomentsContent.ITEMS) {
                    it.getValue<Moment>(Moment::class.java)
                }
                MomentsContent.ITEMS.forEach { city -> MomentsContent.ITEM_MAP.put(city.id, city) }

                city_list.adapter!!.notifyDataSetChanged()
            }
        }
        instance.addValueEventListener(listener)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.let {
            it.adapter = MomentsRecyclerViewAdapter(this, MomentsContent.ITEMS, twoPane)
            it.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun setRecyclerViewItemTouchListener(recyclerView: RecyclerView) {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                val position = p0.adapterPosition
                val toDelete: Moment = MomentsContent.ITEMS.get(position)

                MomentRepository.destroy(toDelete)
                MomentsContent.remove(toDelete)

                recyclerView.adapter!!.notifyItemRemoved(position)
                val toast = Toast.makeText(
                    applicationContext,
                    "Cidade ${toDelete.nome} apagada com sucesso.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}