package com.example.admindashboard.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admindashboard.MainActivity
import com.example.admindashboard.databinding.ActivityAddBooksBinding
import java.util.*

class AddBooks : AppCompatActivity() {

    var year: String = ""
    var department: String = "IT"
    var semester: String = ""
    var subject: String = ""
    var from: String = ""
    var binding: ActivityAddBooksBinding? = null


    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityAddBooksBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        variableInit()
        subscribeClickListner()
        setYearViewGroup()
        setSemViewGroup()

    }

    private fun subscribeClickListner() {

        binding!!.back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding!!.next.setOnClickListener {
            goToNextActivity()
        }

    }


    private fun goToNextActivity() {
        if (year.isNotEmpty() && department.isNotEmpty() && semester.isNotEmpty()) {
            val intent = Intent(this, UploadActivity::class.java)
            intent.putExtra("year", year)
            intent.putExtra("department", department)
            intent.putExtra("sem", semester)
            intent.putExtra("heading", "Upload")
            intent.putExtra("from", from)

            if (from != "pyq") {
                if (subject.isNotEmpty()) {
                    intent.putExtra("subject", subject)
                }else if (subject.isEmpty()) {
                    Toast.makeText(this, "select subject", Toast.LENGTH_SHORT).show()
                }

            }
            startActivity(intent)
        } else {
            if (year.isEmpty()) {
                Toast.makeText(this, "select year", Toast.LENGTH_SHORT).show()
            } else if (department.isEmpty()) {
                Toast.makeText(this, "select department", Toast.LENGTH_SHORT).show()
            } else if (semester.isEmpty()) {
                Toast.makeText(this, "select sem", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun variableInit() {
        val heading = intent.getStringExtra("heading")
        binding!!.appbarText.text = heading
        from = intent.getStringExtra("from")!!
    }

    private fun setSemViewGroup() {

        binding!!.semesterRadioGrp.setOnCheckedChangeListener { _, checkedId ->
            if (
                checkedId == binding!!.three.id || checkedId == binding!!.four.id ||
                checkedId == binding!!.five.id || checkedId == binding!!.six.id ||
                checkedId == binding!!.seven.id || checkedId == binding!!.eight.id
            ) {
                checkSem(binding!!.three, binding!!.four, 3, 4)
                checkSem(binding!!.five, binding!!.six, 5, 6)
                checkSem(binding!!.seven, binding!!.eight, 7, 8)
            }
        }
    }

    private fun setYearViewGroup() {
        binding!!.yearRadioGrp.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {

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


    private fun setContent(i: Int) {
        binding!!.sem.visibility = View.VISIBLE
        binding!!.three.visibility = View.GONE
        binding!!.four.visibility = View.GONE
        binding!!.five.visibility = View.GONE
        binding!!.six.visibility = View.GONE
        binding!!.seven.visibility = View.GONE
        binding!!.eight.visibility = View.GONE

        when (i) {

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
                    semSixSubject()
                    binding!!.subjectLayout.setOnCheckedChangeListener { _, _ ->
                        semSixSubject()
                    }
                }

            }
            7 -> {
                word = "seventh"
                if (from != "pyq") {
                    semSeventhSubject()
                    binding!!.subjectLayout.setOnCheckedChangeListener { _, _ ->
                        semSeventhSubject()
                    }
                }
            }
            8 -> {
                word = "eight"
                if (from != "pyq") {
                    semEightSubject()
                    binding!!.subjectLayout.setOnCheckedChangeListener { _, _ ->
                        semEightSubject()
                    }
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

