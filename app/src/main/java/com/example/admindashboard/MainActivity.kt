package com.example.admindashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.admindashboard.databinding.ActivityAddBooksBinding
import com.example.admindashboard.databinding.ActivityMainBinding
import com.example.admindashboard.ui.AddBooks

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.books.setOnClickListener {
            startActivity(Intent(this, AddBooks::class.java))
        }
    }
}