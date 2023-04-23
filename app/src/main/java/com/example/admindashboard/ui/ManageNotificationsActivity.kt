package com.example.admindashboard.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.admindashboard.R
import com.example.admindashboard.adapter.NotificationAdapter
import com.example.admindashboard.data.NotificationModel
import com.example.admindashboard.databinding.ActivityManageNotificationsBinding
import com.example.admindashboard.utils.FirebaseUtils.notificationRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ManageNotificationsActivity : AppCompatActivity() {

    private lateinit var dialog: AlertDialog
    lateinit var binding: ActivityManageNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startLoadingScreen()
        notificationRef.addValueEventListener(object : ValueEventListener {
            val notificationlist = mutableListOf<NotificationModel>()
            override fun onDataChange(snapshot: DataSnapshot) {
                notificationlist.clear()
                for (item in snapshot.children) {
                    val noti = item.getValue(NotificationModel::class.java)!!
                    notificationlist.add(noti)
                }
                notificationlist.sortByDescending { model -> model.date }

                val adapter =
                    NotificationAdapter(notificationlist, this@ManageNotificationsActivity)
                binding.manageRc.adapter = adapter
                val itemTouchHelper = ItemTouchHelper(adapter.SwipeToDeleteCallback(adapter))
                itemTouchHelper.attachToRecyclerView(binding.manageRc)
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    @SuppressLint("InflateParams")
    private fun startLoadingScreen() {
        val builder = AlertDialog.Builder(this)
        builder.setView(layoutInflater.inflate(R.layout.loading_screen, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }
}