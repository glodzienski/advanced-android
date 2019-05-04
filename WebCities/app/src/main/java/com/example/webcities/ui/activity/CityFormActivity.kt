package com.example.webcities.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.webcities.R

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.example.webcities.DTO.AddressDTO
import com.example.webcities.util.FieldValidator
import com.example.webcities.dummy.CitiesContent
import com.example.webcities.entity.City
import com.example.webcities.repository.CityRepository
import com.example.webcities.service.ViaCepApiService
import com.example.webcities.util.ImageBuilder
import com.example.webcities.util.MaskEditUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_city_form.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CityFormActivity : AppCompatActivity(), SensorEventListener {

    /*
    * Referente a camera
    *
    * */
    val CAMERA_REQUEST_CODE = 0
    var imageFilePath: String = ""

    /*
    * Referente a validalação de campos da tela
    *
    * */
    lateinit var fieldValidator: FieldValidator

    /*
    * Entidade da tela, para manipular valores e salvar no banoc
    *
    * */
    lateinit var city: City

    /*
    * Referente ao ViacepService
    *
    * */
    private val BASE_URL = "https://viacep.com.br/ws/"
    private val compositeDisposable = CompositeDisposable()
    private val requestInterface = startRetrofit()
    private lateinit var address: AddressDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_form)
        fieldValidator = FieldValidator(this)

        if (intent.hasExtra("city_id") && intent.getStringExtra("city_id").isNotEmpty()) {
            city = CitiesContent.ITEM_MAP[intent.getStringExtra("city_id")] as City

            formTitle.text = getString(R.string.edit_city_label)
            edtName.setText(city.nome)
            edtCountry.setText(city.pais)
            if (city.imagePath.isNotEmpty()) {
                imgCity.setImageBitmap(ImageBuilder.prepare(city.imagePath, 1000, 500))
            }
        }


        startEdtCepStuffs()
        startCameraStuffs()

        btnSave.setOnClickListener { view ->
            if (!this.isValidToSaveCity()) {
                return@setOnClickListener
            }

            this.saveCity()

            val intent = Intent(view.context, CityListActivity::class.java)
            view.context.startActivity(intent)
        }
    }

    private fun isEditing(): Boolean {
        return ::city.isInitialized
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

        val cepOk = fieldValidator.isEditTextFilled(
            edtCep,
            text_input_layout_cep,
            "Por favor, preencha um CEP."
        )
        // TODO não deixar passar se o cep for invalido

        return nameOk && countryOk
    }

    /*
    * Sessão com lógica da câmera
    *
    * */
    private fun startCameraStuffs() {
        btnAddPhoto.setOnClickListener {
            try {
                this.startCamera()
            } catch (e: IOException) {
                Log.d("ERRO IO: ", "não foi possível carregar a câmera.")
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    imgCity.setImageBitmap(ImageBuilder.prepare(imageFilePath, imgCity.width, imgCity.height))
                    turnOffListenerSensor()
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    File(imageFilePath).deleteOnExit()
                    imageFilePath = ""
                    turnOnListenerSensor()
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

    /*
    * Aqui para baixo, implementação de sensor
    *
    * */

    private val sensorManager: SensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private fun turnOnListenerSensor(): Unit {
        try {
            sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL
            )
            photo_conditions_enviroment_label.visibility = View.VISIBLE
            iluminacao_status_label.visibility = View.VISIBLE
            iluminacao_status_value.visibility = View.VISIBLE
        } catch (e: IOException) {
            Log.d("ERRO IO: ", "Não foi possível ativar listener do sensor de iluminação")
        }
    }

    private fun turnOffListenerSensor(): Unit {
        try {
            sensorManager.unregisterListener(this)
            photo_conditions_enviroment_label.visibility = View.GONE
            iluminacao_status_label.visibility = View.GONE
            iluminacao_status_value.visibility = View.GONE
        } catch (e: IOException) {
            Log.d("ERRO IO: ", "Não foi possível desativar listener do sensor de iluminação")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        iluminacao_status_value.text = event!!.values.zip("XYZ".toList()).fold("") { acc, pair ->
            getIluminacaoStatus(pair.first)
        }
    }

    override fun onResume() {
        super.onResume()

        if (imageFilePath.isNullOrBlank()) {
            turnOnListenerSensor()
        }
    }

    override fun onPause() {
        super.onPause()

        if (imageFilePath.isNullOrBlank()) {
            turnOffListenerSensor()
        }
    }

    /*
    * Valores baseados na tabela do site https://en.wikipedia.org/wiki/Lux
    * */
    private fun getIluminacaoStatus(value: Float): String {
        if (value <= 320) {
            return "Menos que o ideal; A foto não ficará tão boa. Recomendado uso do flash."
        }

        if (value > 320 && value <= 25.000) {
            return "Ideal; A foto ficará ótima."
        }

        if (value > 25.000 && value <= 100.000) {
            return "Muita; A foto ficará ruim. Recomendado local com menos iluminação."
        }

        return "Não foi possível calcular o status da iluminação do ambiente"
    }

    /*
    * Aqui para baixo métodos referentes ao via cep, web service
    *
    * */
    private fun startRetrofit(): ViaCepApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ViaCepApiService::class.java)
    }

    private fun startEdtCepStuffs() {
        edtCep.addTextChangedListener(MaskEditUtil.mask(edtCep, MaskEditUtil.FORMAT_CEP))
        edtCep.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val value = s.toString().replace("-", "")
                if (value.count() == 8) {
                    // Para prevenir reconsultas do cep digitado, já que por alguma razão o event listener cai mais
                    // de uma vez aqui quando digitado um número somente.
                    if (::address.isInitialized && address.cep == value) {
                        return
                    }
                    consultCep(value)
                }
            }
        })
    }

    private fun consultCep(cep: String) {
        compositeDisposable.add(
            requestInterface.show(cep)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    private fun handleResponse(addressDTO: AddressDTO) {
        address = addressDTO
    }

    private fun handleError(error: Throwable) {
        address = AddressDTO()
    }

}
