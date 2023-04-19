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
import com.example.admindashboard.data.Pdf
import com.example.admindashboard.utils.FirebaseUtils.bookRef

class ManageBookAdapter(
    val pdfList: List<Pdf>,
    val context: Context,
    val department: String,
    val year: String,
    val semester: String,
    val subject: String
) : RecyclerView.Adapter<ManageBookAdapter.PdfViewHolder>() {

    class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name)
        private val publicationTextView: TextView = itemView.findViewById(R.id.publication)
        private val senderNameTextView: TextView = itemView.findViewById(R.id.senderName)
        private val dateTimeTextView: TextView = itemView.findViewById(R.id.dateTime)
        val delete: ImageView = itemView.findViewById(R.id.delete)

        fun bind(
            pdf: Pdf
        ) {
            nameTextView.text = pdf.fileName
            publicationTextView.text = pdf.publication
            senderNameTextView.text = "Uploaded by: ${pdf.senderName}"
            dateTimeTextView.text = "${pdf.date} ${pdf.time}"


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.mange_itemview, parent, false)
        return PdfViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val pdf = pdfList[position]
        holder.bind(
            pdf
        )

        holder.delete.setOnClickListener {
            bookRef.child(department)
                .child(year)
                .child(semester + "_sem")
                .child(subject)
                .child(pdf.id)
                .removeValue()

            notifyItemRemoved(position)
            Toast.makeText(holder.itemView.context, "deleted", Toast.LENGTH_SHORT).show()
        }


    }

    inner class SwipeToDeleteCallback(private val adapter: ManageBookAdapter) :
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
        bookRef.child(department)
            .child(year)
            .child(semester + "_sem")
            .child(subject)
            .child(pdfList[position].id)
            .removeValue()

        notifyItemRemoved(position)
        Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show()

    }

    override fun getItemCount(): Int {
        return pdfList.size
    }
}
