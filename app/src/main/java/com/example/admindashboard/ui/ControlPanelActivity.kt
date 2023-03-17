package com.example.admindashboard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.admindashboard.MainActivity
import com.example.admindashboard.R
import com.example.admindashboard.databinding.ActivityControlPanelBinding
import com.google.firebase.database.FirebaseDatabase

class ControlPanelActivity : AppCompatActivity() {

    //
    var binding: ActivityControlPanelBinding? = null

    // declaration of variable
    lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlPanelBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // initialization
        database = FirebaseDatabase.getInstance()

        // set appbar data
        val heading = intent.getStringExtra("heading")
        binding!!.appbarText.text = heading
        binding!!.back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding != null) {
            binding = null
        }
    }

}