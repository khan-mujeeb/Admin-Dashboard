package com.example.admindashboard.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.admindashboard.MainActivity
import com.example.admindashboard.R
import com.example.admindashboard.adapter.ManageBookAdapter
import com.example.admindashboard.data.Pdf
import com.example.admindashboard.databinding.ActivityControlPanelBinding
import com.example.admindashboard.utils.FirebaseUtils.bookRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ControlPanelActivity : AppCompatActivity() {

    //
    var binding: ActivityControlPanelBinding? = null

    // declaration of variable
    lateinit var database: FirebaseDatabase
    lateinit var from: String
    private var uriValue: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: AlertDialog
    var year: String = ""
    var department: String = ""
    var semester: String = ""
    var subject: String = ""
    var heading = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlPanelBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        variableInit()
        subscribeUi()
        subscribeOnClickEvent()
    }

    private fun subscribeUi() {
        binding!!.appbarText.text = heading

        bookRef.child(department)
            .child(year)
            .child(semester + "_sem")
            .child(subject)
            .addValueEventListener(object : ValueEventListener {
                val booksList = mutableListOf<Pdf>()

                override fun onDataChange(snapshot: DataSnapshot) {
                    booksList.clear()
                    for (item in snapshot.children) {
                        val books = item.getValue(Pdf::class.java)!!
                        booksList.add(books)
                    }
                    println("mujeeb $department $year $semester$subject $booksList ")
                    val adapter =  ManageBookAdapter(
                        booksList,
                        this@ControlPanelActivity,
                        department,
                        year,
                        semester,
                        subject
                    )
                    binding!!.manageRc.adapter = adapter
                    val itemTouchHelper = ItemTouchHelper(adapter.SwipeToDeleteCallback(adapter))
                    itemTouchHelper.attachToRecyclerView(binding!!.manageRc)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun variableInit() {
        database = FirebaseDatabase.getInstance()
        intent.getStringExtra("heading")
        from = intent.getStringExtra("from")!!
        year = intent.getStringExtra("year")!!
        department = intent.getStringExtra("department")!!
        semester = intent.getStringExtra("sem")!!
        subject = intent.getStringExtra("subject")!!
        auth = Firebase.auth
        heading = intent.getStringExtra("heading")!!
        binding!!.appbarText.text = heading
    }

    private fun subscribeOnClickEvent() {
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