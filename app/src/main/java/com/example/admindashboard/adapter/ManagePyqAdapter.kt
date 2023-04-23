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
import com.example.admindashboard.data.PYQ
import com.example.admindashboard.ui.ControlPanelActivity
import com.example.admindashboard.utils.FirebaseUtils
import com.example.admindashboard.utils.FirebaseUtils.pyqRef

class ManagePyqAdapter(
    val pyqList: MutableList<PYQ>,
    val context: Context,
    val department: String,
    val year: String,
    val semester: String,
    val subject: String
) : RecyclerView.Adapter<ManagePyqAdapter.PYQViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PYQViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pyq_itemview, parent, false)
        return PYQViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pyqList.size
    }

    override fun onBindViewHolder(holder: PYQViewHolder, position: Int) {
        val pyq = pyqList[position]
        holder.name.text = "${pyq.exam} ${pyq.year}"
        holder.senderName.text = "Uploaded by: ${pyq.senderName}"
        holder.dateTime.text = "${pyq.date} ${pyq.time}"
        holder.delete.setOnClickListener {
            pyqRef.child(department)
                .child(year)
                .child(semester + "_sem")
                .child(subject)
                .child(pyq.id)
                .removeValue()

            notifyItemRemoved(position)
            Toast.makeText(holder.itemView.context, "deleted", Toast.LENGTH_SHORT).show()
        }
    }

    inner class SwipeToDeleteCallback(private val adapter: ManagePyqAdapter) :
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
            adapter.deleteItem(position)
        }
    }

    fun deleteItem(position: Int) {
        // implement the delete logic here
        pyqRef.child(department)
            .child(year)
            .child(semester + "_sem")
            .child(subject)
            .child(pyqList[position].id)
            .removeValue()

        notifyItemRemoved(position)
        Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show()

    }

    class PYQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val senderName: TextView = itemView.findViewById(R.id.senderName)
        val dateTime: TextView = itemView.findViewById(R.id.dateTime)
        val delete: ImageView = itemView.findViewById(R.id.delete)
    }
}
