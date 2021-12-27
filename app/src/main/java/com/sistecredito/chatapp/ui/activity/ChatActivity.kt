package com.sistecredito.chatapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sistecredito.chatapp.data.model.Message
import com.sistecredito.chatapp.data.model.User
import com.sistecredito.chatapp.databinding.ActivityChatBinding
import com.sistecredito.chatapp.ui.adapter.MessageAdapter

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var mDbRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var listMessage: MutableList<Message>
    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDbRef = FirebaseDatabase.getInstance().getReference()
        mAuth = FirebaseAuth.getInstance()
        listMessage = mutableListOf()

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name
        supportActionBar?.setIcon(R.drawable.notification_bg)

        val adapter = MessageAdapter(this, listMessage)
        binding.recyclerChat.adapter = adapter
        binding.recyclerChat.layoutManager = LinearLayoutManager(this)

        mDbRef.child("chat").child(senderRoom!!).child("messages").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listMessage.clear()
                for (postSnapshot in snapshot.children) {
                    val message = postSnapshot.getValue(Message::class.java)
                    listMessage.add(message!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.fabSend.setOnClickListener {
            val message = binding.etMessage.text.toString()
            val messageObject = Message(message, senderUid)

            mDbRef.child("chat").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chat").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            binding.etMessage.setText("")
        }
    }
}