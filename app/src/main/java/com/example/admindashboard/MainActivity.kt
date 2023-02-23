package com.example.admindashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.admindashboard.databinding.ActivityMainBinding
import com.example.admindashboard.ui.AddBooks
import com.example.admindashboard.ui.NotifficationActivity

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.books.setOnClickListener {
            startActivity(Intent(this, AddBooks::class.java))
//            Animatoo.animateShrink(this);
        }

        binding!!.floatingBtn.setOnClickListener {
            startActivity(Intent(this, NotifficationActivity::class.java))
//            Animatoo.animateShrink(this);
        }
    }
}