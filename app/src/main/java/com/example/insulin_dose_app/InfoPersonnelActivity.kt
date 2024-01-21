package com.example.insulin_dose_app

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.*

class InfoPersonnelActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var tvDate: TextView
    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private var selectedGender: String? = null
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_personnel)
        FirebaseApp.initializeApp(applicationContext)

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance()

        // Bind date TextView
        tvDate = findViewById(R.id.editTextDate2)

        // Bind height and weight EditText
        etHeight = findViewById(R.id.textInputLayout1_editText)
        etWeight = findViewById(R.id.textInputEditTextWeight)

        // Bind gender RadioGroup
        genderRadioGroup = findViewById(R.id.genderRadioGroup)

        // Set today's date as the default date
        updateDateInView()

        // Set event listener for clicking the date TextView to open DatePickerDialog
        tvDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Set event listener for RadioGroup to save the selected gender to Firestore
        findViewById<RadioButton>(R.id.radio_male).isChecked = true
        selectedGender = "Male"
        updateGenderBackground()

        genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_male -> {
                    selectedGender = "Male"
                    updateGenderBackground()
                }
                R.id.radio_female -> {
                    selectedGender = "Female"
                    updateGenderBackground()
                }
            }
        }

        findViewById<View>(R.id.logInbtn).setOnClickListener {
            saveDataToFirestore()
            // Create an Intent to start the new activity
            val intent = Intent(this@InfoPersonnelActivity, Midical_info::class.java)
            startActivity(intent)
        }

        // Assume you have a method to get the current user ID (replace "getUserId()" with your actual method)
        userId = getUserId()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tvDate.text = sdf.format(calendar.time)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateGenderBackground() {
        val maleRadioButton: RadioButton = findViewById(R.id.radio_male)
        val femaleRadioButton: RadioButton = findViewById(R.id.radio_female)

        if (selectedGender == "Male") {
            maleRadioButton.setBackgroundResource(R.drawable.gender_checked_background_male)
            femaleRadioButton.setBackgroundResource(R.drawable.gender_unchecked_background_female)
        } else if (selectedGender == "Female") {
            maleRadioButton.setBackgroundResource(R.drawable.gender_unchecked_background_male)
            femaleRadioButton.setBackgroundResource(R.drawable.gender_checked_background_female)
        } else {
            maleRadioButton.setBackgroundResource(R.drawable.gender_unchecked_background_male)
            femaleRadioButton.setBackgroundResource(R.drawable.gender_unchecked_background_female)
        }
    }

    private fun saveDataToFirestore() {

        if (userId.isNotEmpty()) {
            val height = etHeight.text.toString().trim()
            val weight = etWeight.text.toString().trim()
            val dateOfBirth = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(calendar.time)

            val userRef = firestore.collection("Users").document(userId)

            // Use a batched write to update multiple fields
            val batch = firestore.batch()
            batch.update(userRef, "height", height)
            batch.update(userRef, "weight", weight)
            batch.update(userRef, "dateOfBirth", dateOfBirth)
            batch.update(userRef, "gender", selectedGender)

            // Commit the batch
            batch.commit()
                .addOnSuccessListener {
                    println("Data saved successfully to Firestore")
                }
                .addOnFailureListener { e ->
                    println("Error saving data to Firestore: $e")
                }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateDateInView()
    }

    private fun getUserId(): String {
        val currentUser = FirebaseAuth.getInstance()
        return currentUser?.uid ?: ""


    }
}
