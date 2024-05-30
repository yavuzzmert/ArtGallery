package com.yavuzmert.a18artbook

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.yavuzmert.a18artbook.databinding.ActivityArtBinding

class ArtActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }

    fun saveButtonClicked(view: View){

    }

    fun selectImage(view: View){

      if(ContextCompat.checkSelfPermission(this@ArtActivity, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            //rational
            Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {

                //request permission


            }).show()

        } else {
            //request permission

        }

      } else {
          val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
          //intent
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
 */