package com.example.instafirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instafirebase.R
import com.example.instafirebase.adapter.PostAdapter
import com.example.instafirebase.databinding.ActivityFeedBinding
import com.example.instafirebase.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db: FirebaseFirestore = Firebase.firestore
    //val alertDialog = AlertDialog.Builder(this)
    private lateinit var postArrayList : ArrayList<Post>
    // private lateinit var binding : ActivityFeedBinding
    private lateinit var feedAdapter : PostAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        //binding = ActivityFeedBinding.inflate(layoutInflater)
        //val view = binding.root
        //setContentView(view)

        val rvPosts = findViewById<View>(R.id.instaFeed) as RecyclerView



        postArrayList = ArrayList<Post>()

        // Initialize Firebase Auth
        auth = Firebase.auth

        getData()


        feedAdapter = PostAdapter(postArrayList)
        rvPosts.adapter = feedAdapter
        rvPosts.layoutManager = LinearLayoutManager(this)

        //binding.instaFeed.layoutManager = LinearLayoutManager(this)
        //feedAdapter = PostAdapter(postArrayList)
        //binding.instaFeed.adapter = feedAdapter

    }

    private fun getData(){
        db.collection("Post").orderBy("Date", Query.Direction.DESCENDING).addSnapshotListener { value, err ->
            if (err!=null){
                Toast.makeText(this,err.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if (value != null){
                    if (!value.isEmpty){
                        val documents = value.documents
                        postArrayList.clear()
                        for (document in documents) {
                            val comment = document.get("comment") as String
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String

                            val post = Post(userEmail,comment,downloadUrl)
                            postArrayList.add(post)

                            /*println("comment $comment , userEmail $userEmail, downloadUrl $downloadUrl")
                            println(postArrayList)*/


                        }

                        feedAdapter.notifyDataSetChanged()

                    }
                }
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

    fun uploadPhoto(view: View){
        val intent = Intent(this, UploadActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun profileClicked(view: View){
        val intent = Intent(this,ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }
}
