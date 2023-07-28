package com.example.admindashboard.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.admindashboard.MainActivity
import com.example.admindashboard.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null
    private lateinit var auth: FirebaseAuth
    private val stateMachineName = "State Machine 1"
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        auth = Firebase.auth

        onEmailFoucus()
        onPasswordFoucus()
        eyePostionChange()

        subscribeOnClickEvents()
    }

    private fun subscribeOnClickEvents() {
        binding!!.loginBtn.setOnClickListener {
            binding!!.password.clearFocus()

            val email = binding!!.email.text.toString()
            val password = binding!!.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                LoginAccount(email, password)
            } else {
                Toast.makeText(this, "Enter Id and Password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun LoginAccount(email: String, password: String) {


        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@LoginActivity) { task ->
                if (task.isSuccessful) {
                    Handler(mainLooper).postDelayed({
                        binding!!.loginCharacter.controller.fireState(stateMachineName, "success")
                    }, 2000)

                    Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, MainActivity::class.java))
                } else {

                    binding!!.password.text = null
                    Handler(mainLooper).postDelayed({
                        binding!!.loginCharacter.controller.fireState(stateMachineName, "fail")
                    }, 2000)

                    Toast.makeText(this, "Invalid Id or Password", Toast.LENGTH_SHORT).show()
                }

            }

    }

    private fun eyePostionChange() {
        binding!!.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    binding!!.loginCharacter.controller.setNumberState(
                        stateMachineName,
                        "Look",
                        2 * p0!!.length.toFloat()
                    )
                } catch (_: Exception) {

                }
            }

            override fun afterTextChanged(p0: Editable?) {


            }

        })
    }

    private fun onPasswordFoucus() {
//        binding!!.email.clearFocus()

        binding!!.password.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding!!.loginCharacter.controller.setBooleanState(
                    stateMachineName,
                    "hands_up",
                    true
                )
            } else {
                binding!!.loginCharacter.controller.setBooleanState(
                    stateMachineName,
                    "hands_up",
                    false
                )
            }
        }
    }

    private fun onEmailFoucus() {
        binding!!.email.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding!!.loginCharacter.controller.setBooleanState(
                    stateMachineName = stateMachineName,
                    inputName = "Check",
                    value = true
                )
            } else {
                binding!!.loginCharacter.controller.setBooleanState(
                    stateMachineName = stateMachineName,
                    inputName = "Check",
                    value = false
                )
            }
        }
    }

    // releasing resources
    override fun onDestroy() {
        super.onDestroy()
        if (binding != null) {
            binding = null
        }
    }
}