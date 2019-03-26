package com.example.twentyfive

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import kotlinx.android.synthetic.main.activity_picture.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PictureActivity : AppCompatActivity() {

    val CAMERA_REQUEST_CODE = 0
    lateinit var imageFilePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        btnCamera.setOnClickListener {
            try {
                val imageFile =  createImageFile()
                val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)

                if (callCameraIntent.resolveActivity(packageManager) !== null) {
                    val authorities = packageName + ".fileprovider"
                    val imageUri = FileProvider.getUriForFile(this, authorities, imageFile)
                    callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

                    startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
                }
            }
            catch (e: IOException) {
                Log.d("ERRO IO: ", "não foi possível carregar a câmera.")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    imageView3.setImageBitmap(prepareImage())
                }
            }
        }
    }

    private fun prepareImage (): Bitmap {
        val imageViewWidth = imageView3.width
        val imageViewHeight = imageView3.height

        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, bmOptions)
        val bitmapWidth = bmOptions.outWidth
        val bitoutHeight = bmOptions.outHeight
        val scaleFactor = Math.min(bitmapWidth/imageViewWidth, bitoutHeight/imageViewHeight)

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize =scaleFactor

        return BitmapFactory.decodeFile(imageFilePath, bmOptions)
    }
}
