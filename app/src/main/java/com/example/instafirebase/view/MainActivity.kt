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
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth


        val currentUser = auth.currentUser

        if (currentUser != null){
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
    fun signInClicked(view : View){
        val etEmail = findViewById<EditText>(R.id.etSignIneMail)
        val etPassword = findViewById<EditText>(R.id.etSignInPassword)
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (email.isNotEmpty()&&password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                //on success move to the other activity
                val intent = Intent(this, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                //on failure show the exception
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }else{Toast.makeText(this,"either email or password is missing!",Toast.LENGTH_LONG).show()}

    }
    fun signUpClicked(view: View){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
        /*val etEmail = findViewById<EditText>(R.id.etSignIneMail)
        val etPassword = findViewById<EditText>(R.id.etSignInPassword)
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (email.isNotEmpty()&&password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                //on success move to the other activity
                val intent = Intent(this,FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                //on failure show the exception
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }else{Toast.makeText(this,"either email or password is missing!",Toast.LENGTH_LONG).show()}
*/
    }
}