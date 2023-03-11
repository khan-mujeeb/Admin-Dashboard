package com.example.admindashboard.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.admindashboard.R
import com.example.admindashboard.data.Pdf
import com.example.admindashboard.databinding.ActivityAddBooksBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddBooks : AppCompatActivity() {

    var year: String = ""
    var department: String = "IT"
    var semester: String = ""
    var subject: String = ""

    var from: String = ""
    private var uriValue: Uri? = null
    private lateinit var auth: FirebaseAuth
    var binding: ActivityAddBooksBinding? = null

    private lateinit var dialog: AlertDialog

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityAddBooksBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val heading = intent.getStringExtra("heading")
        binding!!.appbarText.text = heading

        from = intent.getStringExtra("from")!!



        setYearViewGroup()
        setSemViewGroup()
        onUploadClick()

        auth = Firebase.auth

        binding!!.submit.setOnClickListener {


            uploadPdfToFirebaseStorage(uriValue)
        }


    }

    // upload books / pyq
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



        if (from == "pyq") {

            val database = FirebaseDatabase.getInstance().getReference("pyq")
            val key = database.push().key.toString()

            val databaseRef = database
                .child(department)
                .child(year)
                .child("${semester}_sem")
                .child(key)

            val pdfId = subject

            val pdf = Pdf(
                key!!,
                pdfId,
                downloadUrl,
                name,
                currentDate,
                currentTime
            )

            pdfId.let {
                databaseRef.setValue(pdf)
            }

        } else {

            val database = FirebaseDatabase.getInstance().getReference("books")
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
                currentTime
            )

            pdfId.let {
                databaseRef.setValue(pdf)
            }
        }


        dialog.dismiss()
        Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
    }


    private fun onUploadClick() {
        binding!!.upload.setOnClickListener {
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
    }

    private fun setSemViewGroup() {
        binding!!.semesterRadioGrp.setOnCheckedChangeListener { _, checkedId ->
//            checkedId == binding!!.one.id || checkedId == binding!!.two.id ||
            if (
                checkedId == binding!!.three.id || checkedId == binding!!.four.id ||
                checkedId == binding!!.five.id || checkedId == binding!!.six.id ||
                checkedId == binding!!.seven.id || checkedId == binding!!.eight.id
            ) {
//                checkSem(binding!!.one, binding!!.two, 1, 2)
                checkSem(binding!!.three, binding!!.four, 3, 4)
                checkSem(binding!!.five, binding!!.six, 5, 6)
                checkSem(binding!!.seven, binding!!.eight, 7, 8)
//                binding!!.subject.visibility = View.VISIBLE
            }
        }
    }

    private fun setYearViewGroup() {
        binding!!.yearRadioGrp.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
//                binding!!.fe.id -> {
//                    Toast.makeText(this, "1st year", Toast.LENGTH_SHORT).show()
//                    setContent(1)
//                    year = "first_year"
//                }

                binding!!.se.id -> {
                    Toast.makeText(this, "2nd year", Toast.LENGTH_SHORT).show()
                    setContent(2)
                    year = "second_year"
                }

                binding!!.te.id -> {
                    Toast.makeText(this, "3rd year", Toast.LENGTH_SHORT).show()
                    setContent(3)
                    year = "third_year"
                }

                binding!!.be.id -> {
                    Toast.makeText(this, "4th year", Toast.LENGTH_SHORT).show()
                    year = "fourth_year"
                    setContent(4)

                }
            }
        }
    }

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
                    val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    binding?.pdfPath?.text = displayName
                }
            }
        }
    }


    private fun setContent(i: Int) {
        binding!!.sem.visibility = View.VISIBLE
//        binding!!.one.visibility = View.GONE
//        binding!!.two.visibility = View.GONE
        binding!!.three.visibility = View.GONE
        binding!!.four.visibility = View.GONE
        binding!!.five.visibility = View.GONE
        binding!!.six.visibility = View.GONE
        binding!!.seven.visibility = View.GONE
        binding!!.eight.visibility = View.GONE

        when (i) {
//            1 -> {
//                binding!!.one.visibility = View.VISIBLE
//                binding!!.two.visibility = View.VISIBLE
//            }
            2 -> {

                binding!!.three.visibility = View.VISIBLE
                binding!!.four.visibility = View.VISIBLE
            }
            3 -> {
                binding!!.five.visibility = View.VISIBLE
                binding!!.six.visibility = View.VISIBLE
            }

            4 -> {
                binding!!.seven.visibility = View.VISIBLE
                binding!!.eight.visibility = View.VISIBLE
            }
        }
    }

    private fun checkSem(one: RadioButton, two: RadioButton, i: Int, i1: Int) {
        if (one.isChecked)

            semester = semNumberToWord(i)
        if (two.isChecked)
            semester = semNumberToWord(i1)
    }

    private fun semNumberToWord(i: Int): String {
        var word = ""
        when (i) {

            3 -> {
                word = "third"


                if (from != "pyq") {
                    semThreeSubject()
                    binding!!.subjectLayout.setOnCheckedChangeListener { _, _ ->
                        semThreeSubject()
                    }
                }


            }
            4 -> {
                word = "fourth"

                if (from != "pyq") {
                    semFourthSubject()
                    binding!!.subjectLayout.setOnCheckedChangeListener { _, _ ->
                        semFourthSubject()
                    }
                }


            }
            5 -> {
                word = "fifth"

                if (from != "pyq") {
                    semFiveSubject()
                    binding!!.subjectLayout.setOnCheckedChangeListener { _, _ ->
                        semFiveSubject()
                    }
                }

            }
            6 -> {
                word = "six"
                if (from != "pyq") {
                    //                semSixSubject()
                    setSemesterData(
                        "Computer Network and Security",
                        "Data Science and Big Data Analytics",
                        "Web Application Development",
                        "Elective-II",
                        "",
                        0, 0, 0, 0, 1

                    )
                    binding!!.subjectLayout.setOnCheckedChangeListener { _, _ ->
//                    semSixSubject()
                        setSemesterData(
                            "Computer Network and Security",
                            "Data Science and Big Data Analytics",
                            "Web Application Development",
                            "Elective-II",
                            "",
                            0, 0, 0, 0, 1

                        )
                    }
                }

            }
            7 -> {
                word = "seventh"
                if (from != "pyq") {
                    semSeventhSubject()
                }
            }
            8 -> {
                word = "eight"
                if (from != "pyq") {
                    semEightSubject()
                }
            }
        }
        return word
    }

    private fun setSemesterData(
        s: String,
        s1: String,
        s2: String,
        s3: String,
        s4: String,
        i: Int,
        i1: Int,
        i2: Int,
        i3: Int,
        i4: Int
    ) {
        binding!!.subjectLayoutView.visibility = View.VISIBLE
        setSubjectVisiblity(binding!!.subj1, i)
        setSubjectVisiblity(binding!!.subj2, i1)
        setSubjectVisiblity(binding!!.subj3, i2)
        setSubjectVisiblity(binding!!.subj4, i3)
        setSubjectVisiblity(binding!!.subj5, i4)

        setSubjectData(binding!!.subj1, s)
        setSubjectData(binding!!.subj2, s1)
        setSubjectData(binding!!.subj3, s2)
        setSubjectData(binding!!.subj4, s3)
        setSubjectData(binding!!.subj5, s4)
    }

    private fun setSubjectVisiblity(subj: RadioButton, i: Int) {
        if (i == 1) {
            subj.visibility = View.INVISIBLE
        } else {
            subj.visibility = View.VISIBLE
        }
    }

    private fun semSixSubject() {
        binding!!.subjectLayoutView.visibility = View.VISIBLE
        binding!!.subj5.visibility = View.INVISIBLE

        setSubjectData(binding!!.subj1, "Computer Network and Security")
        setSubjectData(binding!!.subj2, "Data Science and Big Data Analytics")
        setSubjectData(binding!!.subj3, "Web Application Development")
        setSubjectData(binding!!.subj4, "Elective-II")
    }

    private fun semSeventhSubject() {
        binding!!.subjectLayoutView.visibility = View.VISIBLE
        binding!!.subj5.visibility = View.VISIBLE

        setSubjectData(binding!!.subj1, "Information and Storage Retrieval")
        setSubjectData(binding!!.subj2, "Software Project Management")
        setSubjectData(binding!!.subj3, "Deep Learning")
        setSubjectData(binding!!.subj4, "Elective–III")
        setSubjectData(binding!!.subj5, "Elective–IV")
    }

    private fun semEightSubject() {
        binding!!.subjectLayoutView.visibility = View.VISIBLE
        binding!!.subj5.visibility = View.INVISIBLE

        setSubjectData(binding!!.subj1, "Distributed Systems")
        setSubjectData(binding!!.subj2, "Start up and Ecosystem")
        setSubjectData(binding!!.subj3, "Elective–III")
        setSubjectData(binding!!.subj4, "Elective–IV")
    }

    private fun semThreeSubject() {
        binding!!.subjectLayoutView.visibility = View.VISIBLE
        binding!!.subj5.visibility = View.VISIBLE

        setSubjectData(binding!!.subj1, "Discrete Mathematics ")
        setSubjectData(binding!!.subj2, "Logic Design and Computer Organization ")
        setSubjectData(binding!!.subj3, "Data Structures and Algorithms ")
        setSubjectData(binding!!.subj4, "Object Oriented Programming")
        setSubjectData(binding!!.subj5, "Basics of Computer Network")
    }

    private fun semFourthSubject() {
        binding!!.subjectLayoutView.visibility = View.VISIBLE
        binding!!.subj5.visibility = View.VISIBLE

        setSubjectData(binding!!.subj1, "Engineering Mathematics- III ")
        setSubjectData(binding!!.subj2, "Processor Architecture")
        setSubjectData(binding!!.subj3, "Database Management System")
        setSubjectData(binding!!.subj4, "Computer Graphics")
        setSubjectData(binding!!.subj5, "Software Engineering")
    }

    private fun semFiveSubject() {
        binding!!.subjectLayoutView.visibility = View.VISIBLE
        binding!!.subj5.visibility = View.VISIBLE
        setSubjectData(binding!!.subj1, "Theory of Computation")
        setSubjectData(binding!!.subj2, "Operating System")
        setSubjectData(binding!!.subj3, "Machine Learning")
        setSubjectData(binding!!.subj4, "Human Computer Interaction")
        setSubjectData(binding!!.subj5, "Elective–I")
    }

    // get subject
    private fun setSubjectData(subjBtn: RadioButton, sub: String) {
        subjBtn.text = sub
        if (subjBtn.isChecked) {
            subject = sub
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

