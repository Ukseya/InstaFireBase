package com.example.instafirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.instafirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth
        firestore = Firebase.firestore


    }

    fun registerClicked(view: View) {
        val etEmail = findViewById<EditText>(R.id.etSignUpEmail)
        val etPassword = findViewById<EditText>(R.id.etSignUpPassword)
        val etPasswordR = findViewById<EditText>(R.id.etSignUpRepeatPassword)
        val etUserName = findViewById<EditText>(R.id.etUserName)
        val userName = etUserName.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val passwordr = etPasswordR.text.toString()
        /*println("this is your email "+email)
        println("this is your password "+password)
        println("this is your passwordr "+passwordr)*/


                if (email.isNotEmpty() && password.isNotEmpty()) {//check if
                    if (password == passwordr) {//check if passwords match

                    auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                        //on success move to the other activity
                        if (!userName.isEmpty()) {
                            firestore.collection("Profile").add(userName).addOnSuccessListener {
                                val intent = Intent(this, FeedActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        .addOnFailureListener {
                            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                    }else{Toast.makeText(this,"Please Enter UserName",Toast.LENGTH_LONG).show()}
                    }.addOnFailureListener {
                        //on failure show the exception
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }

                }else{Toast.makeText(this, "please re-check your password", Toast.LENGTH_LONG).show()}
            } else {//if not show a text warning
                Toast.makeText(this, "please re-check your password/e-mail", Toast.LENGTH_LONG).show()
            }



        }
    fun returnToSignIn(view: View){
        val intentReturnToSignIn = Intent(this, MainActivity::class.java)
        startActivity(intentReturnToSignIn)
        finish()
    }
    }
