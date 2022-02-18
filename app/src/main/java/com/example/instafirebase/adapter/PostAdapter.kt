package com.example.instafirebase.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.instafirebase.R
import com.example.instafirebase.databinding.PostActivityBinding
import com.example.instafirebase.model.Post
import com.squareup.picasso.Picasso

class PostAdapter(val postList:ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.PostHolder>() {
/*
    class PostHolder(val binding:PostActivityBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = PostActivityBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.tvUserName.text = postList.get(position).email
        holder.binding.tvComment.text = postList.get(position).comment



    }

    override fun getItemCount(): Int {
        return postList.size
    }

 */
    class PostHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName = itemView.findViewById<TextView>(R.id.tvUserName)
        val comment = itemView.findViewById<TextView>(R.id.tvComment)
        //val profilePhoto = itemView.findViewById<TextView>(R.id.ivProfilePhoto)
        val post = itemView.findViewById<ImageView>(R.id.ivPost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val postView = inflater.inflate(R.layout.post_activity,parent,false)
        return PostHolder(postView)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.userName.text = postList.get(position).email
        holder.comment.text = postList.get(position).comment
        Picasso.get().load(postList.get(position).downloadUrl).into(holder.post)

    }

    override fun getItemCount(): Int {
        return postList.size
    }
}