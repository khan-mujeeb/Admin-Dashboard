package com.example.admindashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.admindashboard.R
import com.example.admindashboard.data.NotificationModel
import com.example.admindashboard.utils.FirebaseUtils
import com.example.admindashboard.utils.FirebaseUtils.notificationRef

class NotificationAdapter(
    private val notificationList: MutableList<NotificationModel>,
    private val context: Context
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.mange_itemview, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notificationModel = notificationList[position]
        holder.nameTextView.text = notificationModel.title
        holder.publicationTextView.text = notificationModel.notification
        holder.senderNameTextView.text = notificationModel.senderName
        holder.dateTimeTextView.text = "${notificationModel.date} ${notificationModel.time}"
        holder.deleteImageView.setOnClickListener {
            notificationRef.child(notificationModel.id).removeValue()
            notificationList.removeAt(position)
            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show()
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount() = notificationList.size
    inner class SwipeToDeleteCallback(private val adapter: NotificationAdapter) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            deleteItem(position)
        }
    }

    fun deleteItem(position: Int) {
        // implement the delete logic here
        notificationRef.child(notificationList[position].id).removeValue()
        notifyItemRemoved(position)
        Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show()

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val publicationTextView: TextView = itemView.findViewById(R.id.publication)
        val senderNameTextView: TextView = itemView.findViewById(R.id.senderName)
        val dateTimeTextView: TextView = itemView.findViewById(R.id.dateTime)
        val deleteImageView: ImageView = itemView.findViewById(R.id.delete)
    }
}

