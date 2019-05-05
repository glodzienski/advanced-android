package com.example.webmoments.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.webmoments.R

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.example.webmoments.entity.Address
import com.example.webmoments.util.FieldValidatorUtil
import com.example.webmoments.dummy.MomentsContent
import com.example.webmoments.entity.Moment
import com.example.webmoments.repository.MomentRepository
import com.example.webmoments.service.ViaCepApiService
import com.example.webmoments.util.ImageBuilderUtil
import com.example.webmoments.util.MaskEditUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_moment_form.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MomentFormActivity : AppCompatActivity(), SensorEventListener {

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
    lateinit var fieldValidatorUtil: FieldValidatorUtil

    /*
    * Entidade da tela, para manipular valores e salvar no banoc
    *
    * */
    lateinit var moment: Moment

    /*
    * Referente ao ViacepService
    *
    * */
    private val BASE_URL = "https://viacep.com.br/ws/"
    private val compositeDisposable = CompositeDisposable()
    private val requestInterface = startRetrofit()
    private lateinit var address: Address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moment_form)

        fieldValidatorUtil = FieldValidatorUtil(this)
        startEdtCepStuffs()
        startCameraStuffs()

        if (intent.hasExtra("moment_id") && intent.getStringExtra("moment_id").isNotEmpty()) {
            moment = MomentsContent.ITEM_MAP[intent.getStringExtra("moment_id")] as Moment

            formTitle.text = getString(R.string.edit_moment_label)
            edtName.setText(moment.nome)
            edtDescription.setText(moment.descricao)
            edtCep.setText(moment.address.cep)
            if (moment.imagePath.isNotEmpty()) {
                imgMoment.setImageBitmap(ImageBuilderUtil.prepare(moment.imagePath, 1000, 500))
            }
        }

        btnSave.setOnClickListener { view ->
            if (!this.isValidToSaveMoment()) {
                return@setOnClickListener
            }

            this.saveMoment()

            val intent = Intent(view.context, MomentListActivity::class.java)
            view.context.startActivity(intent)
        }
    }

    /*
    * Métodos referentes ao salvar, editar dados da tela.
    *
    * */
    private fun isEditing(): Boolean {
        return ::moment.isInitialized
    }

    private fun saveMoment() {
        address.cep = address.cep.replace("-", "")
        if (this.isEditing()) {
            moment.nome = edtName.text.toString()
            moment.descricao = edtDescription.text.toString()
            if (imageFilePath.isNotEmpty()) {
                File(moment.imagePath).deleteOnExit()
            }
            moment.imagePath = if (imageFilePath.isNotEmpty()) imageFilePath else moment.imagePath
            moment.address = address

            MomentRepository.update(moment)
            return
        }

        val moment = Moment(
            MomentsContent.ITEMS.count().toString(),
            edtName.text.toString(),
            edtDescription.text.toString(),
            imageFilePath,
            address
        )

        MomentRepository.store(moment)
    }

    private fun isValidToSaveMoment(): Boolean {
        var cepOk = false
        val nameOk = fieldValidatorUtil.isEditTextFilled(
            edtName,
            text_input_layout_name,
            "Por favor, preencha o nome da momento."
        )
        val descriptionOk = fieldValidatorUtil.isEditTextFilled(
            edtDescription,
            text_input_layout_description,
            "Por favor, preencha uma descrição."
        )

        cepOk = fieldValidatorUtil.isEditTextFilled(
            edtCep,
            text_input_layout_cep,
            "Por favor, preencha um CEP."
        )
        if (edtCep.text.toString().isNotBlank() && address.cep.isNullOrBlank()) {
            text_input_layout_cep.error = "Por favor, preencha um CEP válido"
            cepOk = false
        }

        return nameOk && descriptionOk && cepOk
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
                    imgMoment.setImageBitmap(ImageBuilderUtil.prepare(imageFilePath, imgMoment.width, imgMoment.height))
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
        if (value <= 40) {
            return "${value} lx. Menos que o ideal; A foto não ficará tão boa. Recomendado uso do flash."
        }

        if (value > 40 && value <= 25000) {
            return "${value} lx. Ideal; A foto ficará ótima."
        }

        if (value > 25000 && value <= 100000) {
            return "${value} lx. Muita; A foto ficará ruim. Recomendado local com menos iluminação."
        }

        return "${value} lx. Não foi possível calcular o status da iluminação do ambiente"
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
                } else {
                    address = Address()
                    address_status_value.text = ""
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

    private fun handleResponse(address: Address) {
        if (address.cep.isNullOrEmpty()) {
            text_input_layout_cep.error = "CEP inválido"
            address_status_label.visibility = View.GONE
            address_status_value.visibility = View.GONE
            return
        }

        text_input_layout_cep.error = ""
        this.address = address
        address_status_label.visibility = View.VISIBLE
        address_status_value.visibility = View.VISIBLE
        address_status_value.text = address.toString()
    }

    private fun handleError(error: Throwable) {
        Log.d("ERRO IO: ", "não foi possível consultar o webservice viacep")
    }

}
