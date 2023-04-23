package com.example.admindashboard.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.admindashboard.utils.FirebaseUtils
import com.example.admindashboard.MainActivity
import com.example.admindashboard.R
import com.example.admindashboard.data.PYQ
import com.example.admindashboard.data.Pdf
import com.example.admindashboard.databinding.ActivityUploadBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UploadActivity : AppCompatActivity() {
    var binding: ActivityUploadBinding? = null
    lateinit var from: String
    private var uriValue: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: AlertDialog
    var exam: String = ""
    var yearOfExam: String = ""
    var year: String = ""
    var department: String = ""
    var semester: String = ""
    var subject: String = ""
    var publication: String = ""
    var displayName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        variableInit()
        subscribeUi()
        subscribeClickEvents()

    }

    private fun subscribeUi() {
        binding!!.subjectName.text = subject

        when (from) {
            "pyq" -> {
                binding!!.publication.hint = "Enter Examination"
                binding!!.publication.visibility = View.GONE
                binding!!.yearOfExam.visibility = View.VISIBLE
                binding!!.exam.visibility = View.VISIBLE
            }
            else -> {
                binding!!.publication.hint = getString(R.string.enter_publications_name)
                binding!!.yearOfExam.visibility = View.GONE
                binding!!.exam.visibility = View.GONE
            }
        }
    }

    private fun subscribeClickEvents() {
        binding!!.back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // reading pdf file
        binding!!.browseFile.setOnClickListener {
            onBrowseClick()
        }

        binding!!.submit.setOnClickListener {
            publication = binding!!.publication.editText!!.text.toString()
            exam = binding!!.exam.text.toString()
            yearOfExam = binding!!.yearOfExam.text.toString()

            if (publication.isEmpty() && from != "pyq") {
                Toast.makeText(this, "Enter Publication", Toast.LENGTH_SHORT).show()
            } else if (displayName.isEmpty()) {
                Toast.makeText(this, "select file to upload", Toast.LENGTH_SHORT).show()
            } else if (exam.isEmpty() && yearOfExam.isEmpty() && from == "pyq") {
                Toast.makeText(this, "enter all pyq details", Toast.LENGTH_SHORT).show()
            } else {
                uploadPdfToFirebaseStorage(uriValue)
            }
        }
    }

    private fun variableInit() {
        from = intent.getStringExtra("from")!!
        year = intent.getStringExtra("year")!!
        department = intent.getStringExtra("department")!!
        semester = intent.getStringExtra("sem")!!
        subject = intent.getStringExtra("subject")!!
        auth = Firebase.auth
        val heading = intent.getStringExtra("heading")
        binding!!.appbarText.text = heading
    }


    private fun onBrowseClick() {

        if (Build.VERSION.RELEASE.toInt() >= 13) {
            pickPdfFile()
        }
        // permission
        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            // when permission not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            // when permission granted
            pickPdfFile()
        }


    }

    // pick pdf file form internal storage
    private fun pickPdfFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(
            intent,
            1,
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (
            requestCode == 1 && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // when permission is granted call method
            pickPdfFile()
        } else {
            // when permission is denied
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            data!!.data!!.let { uri ->
                uriValue = uri
                val cursor = contentResolver.query(uri, null, null, null, null)
                cursor?.use {
                    it.moveToFirst()
                    displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    binding?.pdfPath?.text = displayName
                }
            }
        }
    }

    private fun uploadPdfToFirebaseStorage(uriValue: Uri?) {

        startLoadingScreen()
        val storageRef = FirebaseStorage.getInstance().reference
        val pdfRef = storageRef.child("books/${uriValue!!.lastPathSegment}")
        val uploadTask = pdfRef.putFile(uriValue)
        uploadTask.addOnSuccessListener {
            // Get the download URL of the PDF file and save it to Firebase Realtime Database
            pdfRef.downloadUrl.addOnSuccessListener { downloadUri ->
                savePdfDownloadUrlToFirebaseRealtimeDatabase(downloadUri.toString())
            }
        }.addOnFailureListener {
            // Handle the error
        }
    }

    @SuppressLint("InflateParams")
    private fun startLoadingScreen() {
        val builder = AlertDialog.Builder(this)
        builder.setView(layoutInflater.inflate(R.layout.loading_screen, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    private fun savePdfDownloadUrlToFirebaseRealtimeDatabase(downloadUrl: String) {

        val calendar = Calendar.getInstance()
        val currentDate =
            "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
                calendar.get(
                    Calendar.YEAR
                )
            }"

        val currentTime = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}:${
            calendar.get(
                Calendar.SECOND
            )
        }"

        val email = auth.currentUser!!.email
        val list = email!!.split("_", "@").map { it.trim() }
        val name = "${list[0]} ${list[1]}"


        when (from) {
            "pyq" -> {
                uploadPYQ(name, currentDate, currentTime, downloadUrl, publication)
            }
            "add_books" -> {
                uploadBooks(publication, currentDate, currentTime, downloadUrl, name)
            }
        }

    }

    private fun uploadBooks(
        publication: String,
        currentDate: String,
        currentTime: String,
        downloadUrl: String,
        name: String
    ) {
        val database = FirebaseUtils.bookRef
        val key = database.push().key.toString()

        val databaseRef = database
            .child(department)
            .child(year)
            .child("${semester}_sem")
            .child(subject)
            .child(key)

        val pdfId = subject

        val pdf = Pdf(
            key!!,
            pdfId,
            downloadUrl,
            name,
            currentDate,
            currentTime,
            publication,
            displayName
        )

        pdfId.let {
            databaseRef.setValue(pdf)
        }
        onuploadSuccessful()
    }

    private fun uploadPYQ(
        name: String,
        currentDate: String,
        currentTime: String,
        downloadUrl: String,
        publication: String
    ) {
        val database = FirebaseUtils.pyqRef
        val key = database.push().key.toString()

        val databaseRef = database
            .child(department)
            .child(year)
            .child("${semester}_sem")
            .child(subject)
            .child(key)

        val pdfId = subject

        val pdf = PYQ(
            key!!,
            downloadUrl,
            name,
            currentDate,
            currentTime,
            yearOfExam,
            exam,
            subject
        )

        pdfId.let {
            databaseRef.setValue(pdf)
        }

        onuploadSuccessful()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (binding != null) {
            binding = null
        }
    }

    fun onuploadSuccessful() {
        dialog.dismiss()
        Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}// end of class