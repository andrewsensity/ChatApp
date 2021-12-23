package com.sistecredito.chatapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.sistecredito.chatapp.R
import com.sistecredito.chatapp.data.Datasource
import com.sistecredito.chatapp.data.model.User
import com.sistecredito.chatapp.ui.activity.ChatActivity

class UserAdapter(val context: Context, val listUser: MutableList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.tvName)
        val image = view.findViewById<ImageView>(R.id.ivProfile)
        val message = view.findViewById<TextView>(R.id.lastMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val list = listUser[position]
        val randomPos = (0 until listUser.size).random()
        holder.name.text = list.name
        holder.image.setImageResource(Datasource.ImageList[randomPos])
        holder.message.text = Datasource.messageList[randomPos]

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name", list.name)
            intent.putExtra("uid", list.uid)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listUser.size
}