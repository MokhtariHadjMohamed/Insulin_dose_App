package com.example.insulin_dose_app

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.*

class Midical_info : AppCompatActivity() {

    private lateinit var tvDate: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private var selectedGender: String? = null
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_midical_info)
        FirebaseApp.initializeApp(applicationContext)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Bind date EditText
        tvDate = findViewById(R.id.editTextDate2)

        // Bind gender RadioGroup
        genderRadioGroup = findViewById(R.id.genderRadioGroup)

        // Set today's date as the default date
        updateDateInView()

        // Set event listener for clicking the date EditText to open DatePickerDialog
        tvDate.setOnClickListener {
            showDatePickerDialog()
        }

        findViewById<RadioButton>(R.id.one).isChecked = true
        selectedGender = "1"
        updateGenderBackground()

        // Set event listener for RadioGroup to save the selected gender to Firestore
        genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.one -> {
                    selectedGender = "1"
                    updateGenderBackground()
                }
                R.id.tow -> {
                    selectedGender = "2"
                    updateGenderBackground()
                }
            }
        }

        findViewById<View>(R.id.logInbtn).setOnClickListener {
            saveDataToFirestore()
            // Create an Intent to start the new activity
            val intent = Intent(this@Midical_info, HomeActivity::class.java)
            startActivity(intent)
        }

        // Assume you have a method to get the current user ID (replace "getUserId()" with your actual method)
        userId = getUserId()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tvDate.setText(sdf.format(calendar.time))
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateGenderBackground() {
        val maleRadioButton: RadioButton = findViewById(R.id.one)
        val femaleRadioButton: RadioButton = findViewById(R.id.tow)

        if (selectedGender == "1") {
            maleRadioButton.setBackgroundResource(R.drawable.gender_checked_background_1)
            femaleRadioButton.setBackgroundResource(R.drawable.gender_unchecked_background_2)
        } else if (selectedGender == "2") {
            maleRadioButton.setBackgroundResource(R.drawable.gender_unchecked_background_1)
            femaleRadioButton.setBackgroundResource(R.drawable.gender_checked_background_2)
        } else {
            // Handle the default case here
            maleRadioButton.setBackgroundResource(R.drawable.gender_checked_background_1)
            femaleRadioButton.setBackgroundResource(R.drawable.gender_checked_background_2)
        }
    }

    private fun saveDataToFirestore() {
        val userId = getUserId()

        if (userId.isNotEmpty()) {
            val dateOfDiagnosis = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(calendar.time)
            val userRef = firestore.collection("Users").document(userId)

            val data = hashMapOf(
                "dateOfDiagnosis" to dateOfDiagnosis,
                "diabet" to selectedGender
            )

            userRef.set(data, SetOptions.merge())
                .addOnSuccessListener {
                    println("Data saved successfully to Firestore")
                }
                .addOnFailureListener { e ->
                    println("Error saving data to Firestore: $e")
                }
        }
    }


    private fun getUserId(): String {
        /* val currentUser = FirebaseAuth.getInstance().currentUser
         return currentUser?.uid ?: ""*/


        return "m0oXDIkgRvhl2zo1Hvtfuw8gSP72" // Replace this with your actual logic to get the user ID
    }
}
