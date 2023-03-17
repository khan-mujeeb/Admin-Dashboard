package com.example.admindashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.admindashboard.databinding.ActivityMainBinding
import com.example.admindashboard.ui.AddBooks
import com.example.admindashboard.ui.ControlPanelActivity
import com.example.admindashboard.ui.NotifficationActivity

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        // go to add books
        binding!!.books.setOnClickListener {
            val intent = Intent(this, AddBooks::class.java)
            intent.putExtra("heading", "Add Books")
            intent.putExtra("from", "add_books")
            startActivity(intent)
        }

        // go to add notification
        binding!!.floatingBtn.setOnClickListener {
            startActivity(Intent(this, NotifficationActivity::class.java))
        }

        // go to add pyq
        binding!!.pyq.setOnClickListener {
            val intent2 = Intent(this, AddBooks::class.java)
            intent2.putExtra("heading", "Add PYQs")
            intent2.putExtra("from", "pyq")
            startActivity(intent2)
        }

        // go to control panel
        binding!!.controlPanel.setOnClickListener {
            val intent2 = Intent(this, ControlPanelActivity::class.java)
            intent2.putExtra("heading", "Control Panel")
            startActivity(intent2)
        }

    }

}