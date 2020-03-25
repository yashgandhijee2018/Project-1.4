package com.demo.incampus

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_addpfp.*
import com.demo.incampus.Walkthrough

class AddPfP: AppCompatActivity() {
    private var flag=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addpfp)

        addpfpbtn.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

                    requestPermissions(permissions, 1001)
                }
                else{
                    pickImagefromGallery()
                }
            }
            else{
                pickImagefromGallery()
            }
        }
        done.setOnClickListener {
            if(flag!=1){
                Toast.makeText(this,"You did not select any profile picture!",Toast.LENGTH_SHORT).show()
            }
            startActivity(Intent(this,Walkthrough::class.java))
        }
    }

    private fun pickImagefromGallery(){
        val int = Intent(Intent.ACTION_PICK)
        int.type = "image/*"
        startActivityForResult(int, 1000)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            1001 -> {
                if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickImagefromGallery()
                }
                else{
                    Toast.makeText(this,"Permission was denied!",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 1000){
            imageView.setImageURI(data!!.data)
            flag=1
        }
    }
}
