package com.example.webcities.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.webcities.ui.fragment.CityDetailFragment
import com.example.webcities.R
import com.example.webcities.dummy.CitiesContent
import com.example.webcities.entities.City
import com.example.webcities.utils.ImageBuilder
import kotlinx.android.synthetic.main.activity_city_detail.*

class CityDetailActivity : AppCompatActivity() {

    lateinit var city: City

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_detail)
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        city = CitiesContent.ITEM_MAP[intent.getStringExtra(CityDetailFragment.ARG_ITEM_ID)] as City

        btnNew.setOnClickListener { view ->
            val intent = Intent(view.context, CityFormActivity::class.java).apply {
                putExtra("city_id", city.id)
            }
            view.context.startActivity(intent)
        }


        if (savedInstanceState == null) {
            if (city.imagePath.isNotEmpty()) {
                imgCity.setImageBitmap(ImageBuilder.prepare(
                    city.imagePath,
                    1000,
                    500
                ))
            }
            val fragment = CityDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        CityDetailFragment.ARG_ITEM_ID,
                        intent.getStringExtra(CityDetailFragment.ARG_ITEM_ID)
                    )
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.city_detail_container, fragment)
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, CityListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
