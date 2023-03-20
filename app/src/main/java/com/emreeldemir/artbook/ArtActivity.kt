package com.emreeldemir.artbook

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.emreeldemir.artbook.databinding.ActivityArtBinding
import com.google.android.material.snackbar.Snackbar

class ArtActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        registerLauncher()

    }

    fun saveButtonClicked (view : View){

    }

    fun selectImage (view : View) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {

                        permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)

                    }).show()

                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                }

            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }

        else {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {

                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                    }).show()

                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }



    }

    private fun registerLauncher() {

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data

                if(intentFromResult != null) {
                    val imageData = intentFromResult.data

                    if (imageData != null) {
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(this@ArtActivity.contentResolver, imageData!!)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageView.setImageBitmap(selectedBitmap)
                            } else {
                                selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageData)
                                binding.imageView.setImageBitmap(selectedBitmap)
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {

                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            } else {

                Toast.makeText(this@ArtActivity, "Permission Needed!", Toast.LENGTH_LONG).show()
            }
        }

    }


}








