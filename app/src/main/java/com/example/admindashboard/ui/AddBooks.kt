package com.example.admindashboard.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.admindashboard.data.Pdf
import com.example.admindashboard.databinding.ActivityAddBooksBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddBooks : AppCompatActivity() {

     var year: String = ""
     var department: String = "IT"
     var semester: String = ""
     var subject: String = ""

    private var uriValue: Uri? = null
    var binding: ActivityAddBooksBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBooksBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setYearViewGroup()
        setSemViewGroup()
        onUploadClick()



        binding!!.submit.setOnClickListener {
//            subject = binding!!.subject.text.toString()
            uploadPdfToFirebaseStorage(uriValue)
        }




    }

    private fun uploadPdfToFirebaseStorage(uriValue: Uri?) {
        val storageRef = FirebaseStorage.getInstance().reference
        val pdfRef = storageRef.child("books/${uriValue!!.lastPathSegment}")
        val uploadTask = pdfRef.putFile(uriValue)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Get the download URL of the PDF file and save it to Firebase Realtime Database
            pdfRef.downloadUrl.addOnSuccessListener { downloadUri ->
                savePdfDownloadUrlToFirebaseRealtimeDatabase(downloadUri.toString())
            }
        }.addOnFailureListener { exception ->
            // Handle the error
        }
    }

    private fun savePdfDownloadUrlToFirebaseRealtimeDatabase(downloadUrl: String) {
        println("mujeeb $subject")
        val databaseRef = FirebaseDatabase.getInstance().getReference("books")
            .child(department)
            .child(year)
            .child("${semester}_sem")
            .child(subject)
        val pdfId = subject
        val pdf = Pdf(pdfId, downloadUrl)
        pdfId?.let {
            databaseRef.setValue(pdf)
        }
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
            if (
                checkedId == binding!!.one.id || checkedId == binding!!.two.id ||
                checkedId == binding!!.three.id || checkedId == binding!!.four.id ||
                checkedId == binding!!.five.id || checkedId == binding!!.six.id ||
                checkedId == binding!!.seven.id || checkedId == binding!!.eight.id
            ) {
                checkSem(binding!!.one, binding!!.two, 1, 2 )
                checkSem(binding!!.three, binding!!.four, 3, 4 )
                checkSem(binding!!.five, binding!!.six, 5, 6)
                checkSem(binding!!.seven, binding!!.eight, 7, 8 )
//                binding!!.subject.visibility = View.VISIBLE
            }
        }
    }

    private fun setYearViewGroup() {
        binding!!.yearRadioGrp.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                binding!!.fe.id -> {
                    Toast.makeText(this, "1st year", Toast.LENGTH_SHORT).show()
                    setContent(1)
                    year = "first_year"
                }

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
        binding!!.one.visibility = View.GONE
        binding!!.two.visibility = View.GONE
        binding!!.three.visibility = View.GONE
        binding!!.four.visibility = View.GONE
        binding!!.five.visibility = View.GONE
        binding!!.six.visibility = View.GONE
        binding!!.seven.visibility = View.GONE
        binding!!.eight.visibility = View.GONE

        when (i) {
            1 -> {
                binding!!.one.visibility = View.VISIBLE
                binding!!.two.visibility = View.VISIBLE
            }
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
        if(two.isChecked)
            semester = semNumberToWord(i1)
    }

    private fun semNumberToWord(i: Int): String {
        var word = ""
        when(i) {
            1 -> {
                word = "first"

            }
            2 -> {
                word = "second"

            }

            3 -> {
                word = "third"


            }
            4 -> {
                word = "fourth"

            }
            5 -> {
                word = "fifth"

                semFiveSubject()
                binding!!.subjectLayout.setOnCheckedChangeListener { _, _ ->
                    semFiveSubject()
                }

            }
            6 -> {
                word = "six"
                semSixSubject()
                binding!!.subjectLayout.setOnCheckedChangeListener { _, _ ->
                    semSixSubject()
                }

            }
            7 -> {
                word = "seventh"
            }
            8 -> {
                word = "eight"
            }
        }
        return word
    }

    private fun semSixSubject() {
        binding!!.subjectLayout.visibility = View.VISIBLE
        binding!!.subj5.visibility = View.INVISIBLE

        setSubjectData(binding!!.subj1, "Computer Network and Security")
        setSubjectData(binding!!.subj2, "Data Science and Big Data Analytics")
        setSubjectData(binding!!.subj3, "Web Application Development")
        setSubjectData(binding!!.subj4, "Elective-II")
    }

    private fun semFiveSubject() {
        binding!!.subjectLayout.visibility = View.VISIBLE
        binding!!.subj5.visibility = View.VISIBLE
        setSubjectData(binding!!.subj1, "Theory of Computation")
        setSubjectData(binding!!.subj2, "Operating System")
        setSubjectData(binding!!.subj3, "Machine Learning")
        setSubjectData(binding!!.subj4, "Human Computer Interaction")
        setSubjectData(binding!!.subj5, "Elective–I")
    }

    private fun setSubjectData(subjBtn: RadioButton, sub: String) {
        subjBtn.text = sub
        println("khan njsncsnc")
        if (subjBtn.isChecked) {
            println("khan")
            subject = sub
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

