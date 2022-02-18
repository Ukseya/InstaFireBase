package com.example.instafirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.instafirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = Firebase.auth
        firestore = Firebase.firestore

    }


    fun signOutClicked(view : View){
        auth.signOut()
        Toast.makeText(this,"you have been successfully signed out", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun uploadPhoto(view: View){
        val intent = Intent(this, UploadActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun setUserName(view: View){

    }
}