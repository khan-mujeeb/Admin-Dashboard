package com.example.admindashboard.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admindashboard.MainActivity
import com.example.admindashboard.data.NotificationModel
import com.example.admindashboard.databinding.ActivityNotifficationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class NotifficationActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private var binding: ActivityNotifficationBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifficationBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        val email = auth.currentUser!!.email
        val list = email!!.split("_", "@").map { it.trim() }
        val name = "${list[0]} ${list[1]}"

        binding!!.push.setOnClickListener {
            setNotificationToFirebase(name)
            startActivity(Intent(this, MainActivity::class.java))
        }


    }

    private fun setNotificationToFirebase(name: String) {
        val randomkey = database.getReference("notification").push().key
        val text = binding!!.notification.text.toString()

        val notification = NotificationModel(
            randomkey!!,
            text,
            name
        )

        database.getReference("notification")
            .child(randomkey)
            .setValue(notification)
            .addOnCompleteListener {
                Toast.makeText(this, "Posted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding != null) {
            binding = null
        }
    }
}