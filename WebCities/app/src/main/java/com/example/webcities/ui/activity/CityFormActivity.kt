package com.example.webcities.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.webcities.R

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import com.example.webcities.utils.FieldValidator
import com.example.webcities.dummy.CitiesContent
import com.example.webcities.entities.City
import com.example.webcities.repositories.CityRepository
import com.example.webcities.utils.ImageBuilder
import kotlinx.android.synthetic.main.activity_city_form.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CityFormActivity : AppCompatActivity() {

    val CAMERA_REQUEST_CODE = 0
    var imageFilePath: String = ""
    lateinit var fieldValidator: FieldValidator
    lateinit var city: City

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_form)

        if (intent.hasExtra("city_id") && intent.getStringExtra("city_id").isNotEmpty()) {
            city = CitiesContent.ITEM_MAP[intent.getStringExtra("city_id")] as City

            formTitle.text = getString(R.string.edit_city_label)
            edtName.setText(city.nome)
            edtCountry.setText(city.pais)
            if (city.imagePath.isNotEmpty()) {
                imgCity.setImageBitmap(ImageBuilder.prepare(city.imagePath, 1000, 500))
            }
        }

        fieldValidator = FieldValidator(this)

        btnAddPhoto.setOnClickListener {
            try {
                this.startCamera()
            } catch (e: IOException) {
                Log.d("ERRO IO: ", "não foi possível carregar a câmera.")
            }
        }

        btnSave.setOnClickListener { view ->
            if (!this.isValidToSaveCity()) {
                return@setOnClickListener
            }

            this.saveCity()

            val intent = Intent(view.context, CityListActivity::class.java)
            view.context.startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    imgCity.setImageBitmap(ImageBuilder.prepare(imageFilePath, imgCity.width, imgCity.height))
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    File(imageFilePath).deleteOnExit()
                    imageFilePath = ""
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "FOTA_" + timeStamp + "_"

        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val imageFile = File.createTempFile(imageFileName, ".png", storageDir)
        imageFilePath = imageFile.absolutePath

        return imageFile
    }

    private fun isEditing(): Boolean {
        return ::city.isInitialized
    }

    private fun startCamera() {
        val imageFile = createImageFile()
        val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)

        if (callCameraIntent.resolveActivity(packageManager) !== null) {
            val authorities = "$packageName.fileprovider"
            val imageUri = FileProvider.getUriForFile(this, authorities, imageFile)
            callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

            startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
        }
    }

    private fun saveCity() {
        if (this.isEditing()) {
            city.nome = edtName.text.toString()
            city.pais = edtCountry.text.toString()
            if (imageFilePath.isNotEmpty()) {
                File(city.imagePath).deleteOnExit()
            }
            city.imagePath = if (imageFilePath.isNotEmpty()) imageFilePath else city.imagePath

            CityRepository.update(city)
            return
        }

        val city = City(
            CitiesContent.ITEMS.count().toString(),
            edtName.text.toString(),
            edtCountry.text.toString(),
            imageFilePath
        )

        CityRepository.store(city)
    }

    private fun isValidToSaveCity(): Boolean {
        val nameOk = fieldValidator.isEditTextFilled(
            edtName,
            text_input_layout_name,
            "Por favor, preencha o nome da cidade."
        )
        val countryOk = fieldValidator.isEditTextFilled(
            edtCountry,
            text_input_layout_country,
            "Por favor, preencha o nome do país."
        )

        return nameOk && countryOk
    }
}
