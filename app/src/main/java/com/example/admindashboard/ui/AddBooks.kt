package com.example.admindashboard.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.admindashboard.R
import com.example.admindashboard.databinding.ActivityAddBooksBinding

class AddBooks : AppCompatActivity() {
    var binding: ActivityAddBooksBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBooksBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        if (binding!!.fe.isChecked) {
            binding!!.one.visibility = View.VISIBLE
            binding!!.two.visibility = View.VISIBLE

            binding!!.three.visibility = View.GONE
            binding!!.four.visibility = View.GONE
            binding!!.five.visibility = View.GONE
            binding!!.six.visibility = View.GONE
            binding!!.seven.visibility = View.GONE
            binding!!.eight.visibility = View.GONE
        }

        else if (binding!!.se.isChecked) {
            binding!!.one.visibility = View.GONE
            binding!!.two.visibility = View.GONE

            binding!!.three.visibility = View.VISIBLE
            binding!!.four.visibility = View.VISIBLE
            binding!!.five.visibility = View.GONE
            binding!!.six.visibility = View.GONE
            binding!!.seven.visibility = View.GONE
            binding!!.eight.visibility = View.GONE
        }

        else if (binding!!.te.isChecked) {
            binding!!.one.visibility = View.GONE
            binding!!.two.visibility = View.GONE

            binding!!.three.visibility = View.GONE
            binding!!.four.visibility = View.GONE
            binding!!.five.visibility = View.VISIBLE
            binding!!.six.visibility = View.VISIBLE
            binding!!.seven.visibility = View.GONE
            binding!!.eight.visibility = View.GONE
        }

        else if (binding!!.te.isChecked) {
            binding!!.one.visibility = View.GONE
            binding!!.two.visibility = View.GONE

            binding!!.three.visibility = View.GONE
            binding!!.four.visibility = View.GONE
            binding!!.five.visibility = View.GONE
            binding!!.six.visibility = View.GONE
            binding!!.seven.visibility = View.VISIBLE
            binding!!.eight.visibility = View.VISIBLE
        }
    }
}