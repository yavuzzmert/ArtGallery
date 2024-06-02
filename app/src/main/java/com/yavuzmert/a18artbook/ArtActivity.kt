package com.yavuzmert.a18artbook

import android.Manifest
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
import com.google.android.material.snackbar.Snackbar
import com.yavuzmert.a18artbook.databinding.ActivityArtBinding
import java.lang.Exception

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

    fun saveButtonClicked(view: View){

    }

    fun selectImage(view: View){

      if(ContextCompat.checkSelfPermission(this@ArtActivity, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){

          if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            //rationale
            Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }).show()
        } else {
            //request permission
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
      } else {
          val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
          activityResultLauncher.launch(intentToGallery)
          //intent
      }
    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    val imageData = intentFromResult.data // verdiği uri
                    //binding.imageView.setImageURI(imageData)
                    if(imageData != null){
                        try {
                            if(Build.VERSION.SDK_INT >=28){
                                val source = ImageDecoder.createSource(this@ArtActivity.contentResolver, imageData)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageView.setImageBitmap(selectedBitmap)
                            }else{
                                selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageData)
                                binding.imageView.setImageBitmap(selectedBitmap)
                            }
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if(result){
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                //permission denied
                Toast.makeText(this@ArtActivity,"Permission needed!", Toast.LENGTH_LONG).show()
            }
        }
    }
}

/*
    - phone'da klasörlere erişim izni, google'da manifest permissions diye aratırsak izin çeşitlerinin hepsini görüntüleyebiliriz.
    - note; ilk olarak manifest'e kullanacağımız izni ekliyoruz sonra buradan önce izin verildi mi kontrol ediyoruz daha sonra erişimi sağlıyoruz
    - note; if ile izin verildi mi kontrol ediyoruz (32)
                ve verildmediği için izin istiyoruz
            else kısmında ilk olarak görselin uri'ını yani adresini buluyoruz
                ve daha sonra görseli alıyoruz
            note; izin istemek için, ActivityREsultLauncher abstract yani soyut bir sınıf ve bir sonuc için bu activity'i başlat - izin verildi mi diye geri dönecek kısaca ActivityResultLauncher sınıfı bu gibi durumlarda kullanılacak izin isteme gibi
            note; activityResultLauncher -> galeriye gitmek için
                    permissionLauncher -> dosya seçmek için
                    ve bunları initialize etmek için ayrı bir fun oluşturacağız ve OnCreate altında çağıracağız adı registerLauncher olacak.

 */