package com.example.admindashboard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.admindashboard.MainActivity
import com.example.admindashboard.R
import com.example.admindashboard.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        auth = Firebase.auth



        binding!!.loginBtn.setOnClickListener {
            val email = binding!!.email.text.toString()
            val password = binding!!.password.text.toString()

            println("$email $password")

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@LoginActivity) { task->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                    }else {
                        binding!!.password.text = null
                        Toast.makeText(this, "Invalid Id or Password", Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }

    // releasing resources
    override fun onDestroy() {
        super.onDestroy()
        if (binding!=null) {
            binding = null
        }
    }
}