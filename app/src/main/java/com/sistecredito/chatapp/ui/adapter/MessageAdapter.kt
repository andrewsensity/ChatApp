package com.sistecredito.chatapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sistecredito.chatapp.R
import com.sistecredito.chatapp.data.model.Message

class MessageAdapter(val context: Context, val listMessage: MutableList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE    = 1
    val ITEM_SENT       = 2

    class MessageReceiveViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val messageReceive = view.findViewById<TextView>(R.id.tvReceive)
    }

    class MessageSentViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val messageSent = view.findViewById<TextView>(R.id.tvSend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.receive, parent, false)
            return MessageReceiveViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sent, parent, false)
            return MessageSentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val list: Message

        if (holder.javaClass == MessageSentViewHolder::class.java) {
            list = listMessage[position]
            holder as MessageSentViewHolder
            holder.messageSent.text = list.message
        } else {
            list = listMessage[position]
            holder as MessageReceiveViewHolder
            holder.messageReceive.text = list.message
        }
    }

    override fun getItemCount(): Int = listMessage.size

    override fun getItemViewType(position: Int): Int {
        val list = listMessage[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(list.senderId)) {
            return ITEM_SENT
        } else {
            return ITEM_RECEIVE
        }
    }
}