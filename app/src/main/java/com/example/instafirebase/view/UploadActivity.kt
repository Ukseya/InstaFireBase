package com.example.instafirebase.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.instafirebase.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp.now
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*
import android.provider.MediaStore.Images

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream


class UploadActivity : AppCompatActivity() {

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedPicture : Uri? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var db: FirebaseFirestore
    private lateinit var storage : FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        registerLauncher()
        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        var view : View



    }
    fun uploadClicked(view : View){

        //universal unique id
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        //val userName =
        val comment = findViewById<EditText>(R.id.etComment)
        val commentText = comment.text.toString()
        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)
        if (selectedPicture != null && commentText.isNotEmpty()) {

            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                //download url -> firestore
                val uploadPictureReference = storage.reference.child("images").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()


                    val postMap = hashMapOf<String, Any>()
                    postMap.put("downloadUrl", downloadUrl)
                    postMap.put("userEmail", auth.currentUser!!.email!!)
                    postMap.put("comment", commentText)
                    postMap.put("Date", now())

                    firestore.collection("Post").add(postMap).addOnSuccessListener {
0
                        finish()
                        val intent = Intent(this,FeedActivity::class.java)
                        startActivity(intent)
                        finish()

                    }.addOnFailureListener {
                        Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }


                }

                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }

        }else if(!commentText.isNotEmpty()){Toast.makeText(this,"Please write a comment",Toast.LENGTH_LONG).show()}
        else{
            Toast.makeText(this,"Please Select a Picture",Toast.LENGTH_LONG).show()
        }

    }
    fun selectImageClicked(view: View){

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permision"){
                    //request permission
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                //request permission
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intentGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //start activity for result
            activityResultLauncher.launch(intentGallery)
        }

    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    val selectImage : ImageView = findViewById(R.id.ivSelectImage)
                    selectedPicture = intentFromResult.data
                    selectedPicture?.let {
                        selectImage.setImageURI(it)
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if (result){
                //permission granted
                val intentGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentGallery)

            }else{
                //permission denied
                Toast.makeText(this,"Permission needed!",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun signOutClicked(view : View){
        auth.signOut()
        Toast.makeText(this,"you have been successfully signed out",Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun profileClicked(view: View){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    /*fun getUserName(userEmail : String):String{
        val userName : String

        val docRef = db.collection("Profile").whereEqualTo("userEmail",userEmail).

        return userName
    }*/

    fun btCameraClicked(view: View){

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.CAMERA)){
                Snackbar.make(view,"permission needed for camera",Snackbar.LENGTH_INDEFINITE).setAction("Give Permision"){
                    //request permission
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }.show()
            }else{
                //request permission
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }else {


            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                Snackbar.make(view, "permission needed for camera", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission") {
                    permissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }.show()
            } else {


                val intentGallery = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //start activity for result
                //activityResultLauncher.launch(intentGallery)
                startActivityForResult(intentGallery, 31)
                intentGallery
            }
        }
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==31){
            val selectImage : ImageView = findViewById(R.id.ivSelectImage)
            var bmp = data?.extras?.get("data") as Bitmap
            selectedPicture = getImageUri(this,bmp)
            selectImage.setImageURI(selectedPicture)
        }

    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }

}