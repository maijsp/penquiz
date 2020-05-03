package com.example.penquiz.ui.setting


import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.penquiz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SettingFragment: Fragment(){

    private lateinit var imageProfile: CircleImageView
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_CHOOSE = 2
    private lateinit var currentPath:String
    private lateinit var root:View
    lateinit var mDatabase: DatabaseReference
    var name: DataSnapshot? = null
    private lateinit var ButtonThai: Button
    private lateinit var ButtonEng: Button
    private lateinit var username: TextView
    private lateinit var user: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Don't use recreate use intent instead
        // but how to use intent in this?
        // sth like this

        root = inflater.inflate(R.layout.fragment_profile,container,false)

        imageProfile= root.findViewById(R.id.camera_image)
        username = root.findViewById(R.id.text_username)
        ButtonThai = root.findViewById(R.id.button_english)
        ButtonEng = root.findViewById(R.id.button_thai)

        savedInstanceState?.let { onRestoreInstanceState(it) }
        user = FirebaseAuth.getInstance().currentUser!!
        username.text = user!!.email


        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        mDatabase!!.keepSynced(true)  //realtime data from firebase
        mDatabase.child(user!!.uid.toString()).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // No Need To implement we don't use it
                // Because we get only image not edit or else
                // I dunno no
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                // get value by using string from java class
                // Because image is retrieve from Firebase
                // URI already include in firebase
                // we use this URI for each user
                // Yes still there
                val image = snapshot.child("image").getValue(String::class.java)
                Picasso.get()
                    .load(image)
                    .into(imageProfile)
            }
        })

        ButtonThai.setOnClickListener {
            setLocale("th")
            activity?.recreate()
        }
        ButtonEng.setOnClickListener {
            setLocale("en")
            activity?.recreate()
        }
        imageProfile.setOnClickListener(View.OnClickListener {
            addOperation(it)
        })

        return root
    }

    fun addOperation(v: View?) {
        val options = arrayOf<String>("Take Photo","Choose from gallery","Delete image profile", "Cancel")
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Choose your profile picture")

        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if(options[item].equals("Take Photo")){
                val takePicture: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)



                var photoUri: Uri? = null
                //activity!!.packageManager -> in case of fragment condition
                if(takePicture.resolveActivity(requireActivity().packageManager) != null) {
                    var photoPath:File? = null
                    try{
                        photoPath = createImage()
                    }catch (e: IOException){
                        e.printStackTrace()
                    }
                    if(photoPath != null){
                        //activity!!.applicationContext -> to get context if it is fragment
                        photoUri = FileProvider.getUriForFile(activity!!.applicationContext, "com.coutocode.cameraexample.fileprovider", photoPath)
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                        startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE)
                        Log.d("Fragment", "EXIF info for file " + photoPath)
                        //  Toast.makeText(activity!!.getApplicationContext(),"photoPath!!",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if(options[item].equals("Choose from gallery")){
                val choosePicture: Intent = Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)


                var photoUri: Uri? = null
                //activity!!.packageManager -> in case of fragment condition
                if(choosePicture.resolveActivity(activity!!.packageManager) != null) {
                    var photoPath:File? = null
                    try{
                        photoPath = createImage()
                    }catch (e: IOException){
                        e.printStackTrace()
                    }
                    if(photoPath != null){
                        //activity!!.applicationContext -> to get context if it is fragment
                        photoUri = FileProvider.getUriForFile(activity!!.applicationContext, "com.coutocode.cameraexample.fileprovider", photoPath)
                        choosePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)


                        startActivityForResult(choosePicture,REQUEST_IMAGE_CHOOSE)
                        Log.d("Fragment", "EXIF info for file " + photoPath)
                        Toast.makeText(activity!!.getApplicationContext(),"photoPath!!",Toast.LENGTH_SHORT).show()
                    }
                }

            }
            else if(options[item].equals("Cancel")){
                dialog.dismiss()
            }
            else if(options[item].equals("Delete image profile")){
                mDatabase.child(user!!.uid).removeValue()
            }

        })
        val dialog = builder.create()
        dialog.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != RESULT_CANCELED){
            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

                val auxFile = File(currentPath)
                val imageBitmap: Bitmap = BitmapFactory.decodeFile(currentPath)
                imageProfile.setImageBitmap(imageBitmap)  //if not work use this
                //handleUpload(imageBitmap)

                //try
                handleUri(Uri.fromFile(auxFile))
            }
            else if(requestCode == 2 && resultCode == RESULT_OK && data != null)
            {
                val uri: Uri? = data.data
                handleUri(uri!!)
            }
        }
    }

    //Aom just create few minute ago
    //this function will be called after users select profile image
    private fun handleUri(uri: Uri) {
        var uid= FirebaseAuth.getInstance().currentUser?.uid
        var reference: StorageReference = FirebaseStorage.getInstance().getReference().child("profileImage")//.child(uid+ ".jpg");
        var uploadTask =  reference.putFile(uri!!)
        uploadTask.addOnSuccessListener{
            reference.downloadUrl.addOnSuccessListener {
                mDatabase.child(uid!!).child("image").setValue(it.toString())
                Toast.makeText(activity!!.getApplicationContext(),"success!!"+it.toString(),Toast.LENGTH_SHORT).show()
                Log.d("DIRECTLINK",it.toString())
                //Picasso.get().load(it).into(imageProfile)
            }
        }
    }



    @Throws(IOException::class)
    private fun createImage(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPath = absolutePath
        }
    }


    private fun setLocale(lang: String) {
        //try new one
        val languageGet= lang
        val res:Resources = resources
        val config = res.configuration
        var locale:Locale = Locale(languageGet)

        Locale.setDefault(locale)
        config.locale = locale
        res.updateConfiguration(config, res.displayMetrics)

        activity?.recreate()
    }

    private fun loadLocate(){
        val prefs: SharedPreferences = activity!!.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val language = prefs.getString("My lanaguage", "")
        setLocale(language!!)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        var user: FirebaseUser? =FirebaseAuth.getInstance().currentUser
        outState.putParcelable("outputFileUri", user?.photoUrl)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.getString("outputFileUri")
    }

}