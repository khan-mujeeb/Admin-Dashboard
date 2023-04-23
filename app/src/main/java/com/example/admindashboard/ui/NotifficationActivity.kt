package com.example.admindashboard.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.admindashboard.MainActivity
import com.example.admindashboard.R
import com.example.admindashboard.data.NotificationModel
import com.example.admindashboard.databinding.ActivityNotifficationBinding
import com.example.admindashboard.utils.FirebaseUtils.notificationRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class NotifficationActivity : AppCompatActivity() {

    private lateinit var dialog: AlertDialog
    private lateinit var notification: NotificationModel
    private lateinit var database: FirebaseDatabase
    lateinit var binding: ActivityNotifficationBinding
    private lateinit var auth: FirebaseAuth
    private val pickImage = 100
    private var imageUri: Uri? = null
    var email = ""
    var name = ""
    var imgUrl = ""
    lateinit var list: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifficationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        variableInit()
        subscribeOnClickEvent()


    }

    private fun variableInit() {
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        email = auth.currentUser!!.email!!
        list = email.split("_", "@").map { it.trim() }
        name = "${list[0]} ${list[1]}"
    }

    private fun subscribeOnClickEvent() {

        binding.push.setOnClickListener {

            val notificationTitle = binding.title.text.toString().let {
                it.ifEmpty {
                    Toast.makeText(this, "enter notification title", Toast.LENGTH_SHORT).show()
                    ""
                }
            }

            val notificationDescription = binding.notification.text.toString().let {
                it.ifEmpty {
                    Toast.makeText(this, "enter notification", Toast.LENGTH_SHORT).show()
                    ""
                }
            }


            if (notificationDescription!!.isNotEmpty() && notificationTitle!!.isNotEmpty()) {
                startLoadingScreen()
                setNotificationToFirebase(name, notificationTitle, notificationDescription)
            }
        }

        binding.floatingBtn.setOnClickListener {
            startActivity(Intent(this, ManageNotificationsActivity::class.java))
        }

        binding.headerImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }

    private fun setNotificationToFirebase(
        name: String,
        notificationTitle: String,
        notificationDescription: String
    ) {

        val calendar = Calendar.getInstance()
        val currentDate =
            "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
                calendar.get(Calendar.YEAR)
            }"
        val currentTime = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}:${
            calendar.get(Calendar.SECOND)
        }"


        val randomkey = database.getReference("notification").push().key


        if (imageUri == null) {
            notification = NotificationModel(
                id = randomkey!!,
                notification = notificationDescription,
                senderName = name,
                date = currentDate,
                time = currentTime,
                title = notificationTitle,
                img = imgUrl
            )

            notificationRef
                .child(randomkey!!)
                .setValue(notification)
                .addOnCompleteListener {
                    Toast.makeText(this, "Posted", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }

        }
        else {
            uploadPdfToFirebaseStorage(imageUri) { downloadUri ->
                if (downloadUri != null) {
                    imgUrl = downloadUri
                    notification = NotificationModel(
                        id = randomkey!!,
                        notification = notificationDescription,
                        senderName = name,
                        date = currentDate,
                        time = currentTime,
                        title = notificationTitle,
                        img = imgUrl
                    )

                    notificationRef
                        .child(randomkey!!)
                        .setValue(notification)
                        .addOnCompleteListener {
                            Toast.makeText(this, "Posted", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                        }


                } else {
                    // Handle the error
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.headerImage.setImageURI(imageUri)
        }
    }


    private fun uploadPdfToFirebaseStorage(uriValue: Uri?, callback: (String?) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val pdfRef = storageRef.child("books/${uriValue!!.lastPathSegment}")
        val uploadTask = pdfRef.putFile(uriValue)

        uploadTask.addOnSuccessListener {
            pdfRef.downloadUrl.addOnSuccessListener { uri ->
                // Call the callback function with the download URL as a parameter
                callback(uri.toString())
            }
        }.addOnFailureListener {
            // Call the callback function with a null parameter to indicate failure
            callback(null)
        }
    }


    private fun startLoadingScreen() {
        val builder = AlertDialog.Builder(this)
        builder.setView(layoutInflater.inflate(R.layout.loading_screen, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }
}