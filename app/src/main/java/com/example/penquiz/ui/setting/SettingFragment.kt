package com.example.penquiz.ui.setting


import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.penquiz.R
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SettingFragment: Fragment(),View.OnClickListener {


    private lateinit var imageProfile: ImageButton
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_CHOOSE = 2
    var currentPath:String? = null
    //private var mCurrentPhotoPath: String? = null;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_profile,container,false)

        imageProfile= root.findViewById(R.id.camera_image)
        imageProfile.setOnClickListener(View.OnClickListener {
            addOperation(it)
        })


        return root
//// Inflate the layout for this fragment
////return inflater.inflate(R.layout.fragment_contactus, container, false)
    }


    fun addOperation(v: View?) {

        val options = arrayOf<String>("Take Photo","Choose from gallery","Cancel")
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Choose your profile picture")

        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if(options[item].equals("Take Photo")){
                val takePicture: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                if(takePicture.resolveActivity(packageManager) != null) {
//                    var photoPath:File? = null
//                    try{
//                        photoPath = createImage()
//                    }catch (e: IOException){
//                        e.printStackTrace()
//                    }
//                    if(photoPath != null){
//                        var photoUri = FileProvider.getUriForFile(this, "com.coutocode.cameraexample.fileprovider", photoPath)
//                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//                        startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE)
//
//                    }
//                }
               // takePicture.putExtra(MediaStore.EXTRA_OUTPUT, REQUEST_IMAGE_CAPTURE)


                   startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE)


            }
            else if(options[item].equals("Choose from gallery")){
                val choosePicture: Intent = Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(choosePicture, REQUEST_IMAGE_CHOOSE);
            }
            else if(options[item].equals("Cancel")){
                dialog.dismiss()
            }

        })
        val dialog = builder.create()
        dialog.show()

//        { dialog: DialogInterface, which: Int ->
//            Toast.makeText(activity?.applicationContext,
//                options[which], Toast.LENGTH_SHORT).show()
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != RESULT_CANCELED){
            if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
//                val imageBitmap = data.extras?.get("data") as Bitmap
//                imageProfile.setImageBitmap(imageBitmap)

                val uri: Uri? = data.data
                imageProfile.setImageURI(uri);
                //To get the File for further usage
                //val auxFile = File(mCurrentPhotoPath)
//            val extras = data?.getExtras()
//            val imageBitmap = data?.getExtras()?.get("data") as Bitmap
//            imageButton.setImageBitmap(imageBitmap)

            }
            else if(requestCode == 1 && resultCode == RESULT_OK && data != null)
            {
                val  imageBitmap = data.extras?.get("data") as Bitmap
//                try {
//                    val file =File(currentPath)
//                    val uri = Uri.fromFile(file)
//                    imageProfile.setImageURI(uri)
//                }catch (e: IOException)
//                {
//                    e.printStackTrace()
//                }
                imageProfile.setImageBitmap(imageBitmap)
            }

        }

    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }


//    fun createImage(): File{
//        val timestamp = SimpleDateFormat("yyyMMss_HHmmss").format(Date())
//        val imageName = "JPEG_"+timestamp+"_"
//        var storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        var image = File.createTempFile(imageName, ".jpg",storageDir)
//        currentPath = image.absolutePath
//        return image
//    }


}





