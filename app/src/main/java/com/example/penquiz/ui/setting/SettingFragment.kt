package com.example.penquiz.ui.setting


import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.penquiz.R


class SettingFragment: Fragment(),  View.OnClickListener  {


    private lateinit var imageProfile: ImageButton
    val REQUEST_IMAGE_CAPTURE = 0
    val REQUEST_IMAGE_CHOOSE = 1
    //private var mCurrentPhotoPath: String? = null;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_profile,container,false)

        imageProfile= root.findViewById(R.id.camera_image)

//        imageButton.setOnClickListener{
//            imageSelect(it)
//        }
        imageProfile.setOnClickListener(this)

        return root
//// Inflate the layout for this fragment
////return inflater.inflate(R.layout.fragment_contactus, container, false)
    }

    override fun onClick(v: View?) {

        val options = arrayOf<String>("Take Photo","Choose from gallery","Cancel")
        var builder = AlertDialog.Builder(this.context)
        builder.setTitle("Choose your profile picture")

        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if(options[item].equals("Taken Photo")){
                val takePicture: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE)
            }
            else if(options[item].equals("Choose from gallery")){
                val choosePicture: Intent = Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(choosePicture, REQUEST_IMAGE_CHOOSE);
            }
            else if(options[item].equals("Cancrl")){
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        })


//        { dialog: DialogInterface, which: Int ->
//            Toast.makeText(activity?.applicationContext,
//                options[which], Toast.LENGTH_SHORT).show()
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != RESULT_CANCELED){
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                imageProfile.setImageBitmap(imageBitmap)
                //To get the File for further usage
                //val auxFile = File(mCurrentPhotoPath)
//            val extras = data?.getExtras()
//            val imageBitmap = data?.getExtras()?.get("data") as Bitmap
//            imageButton.setImageBitmap(imageBitmap)

            }

        }

    }



}





