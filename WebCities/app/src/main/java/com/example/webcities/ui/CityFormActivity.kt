package com.example.webcities.ui
import android.content.Intent
import com.example.webcities.R

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.webcities.dummy.CitiesContent
import com.example.webcities.entities.City
import com.example.webcities.services.CityService
import kotlinx.android.synthetic.main.activity_city_form.*

class CityFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_form)

        btnSave.setOnClickListener { view ->
            // TODO salva cidade
            // TODO validação
            val city = City(
                CitiesContent.ITEM_MAP.count().toString(),
                edtName.text.toString(),
                edtCountry.text.toString()
            )
            CityService.store(city)

            val intent = Intent(view.context, CityListActivity::class.java)
            view.context.startActivity(intent)
        }
    }
}
