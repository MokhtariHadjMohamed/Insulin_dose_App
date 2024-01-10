package com.example.insulin_dose_app

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class Mesurer_la_glycemieActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var tvDate: TextView
    private lateinit var etSugarLevel: EditText
    private lateinit var timeTextViews: List<TextView>
    private var selectedTime: String? = null
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var etTime: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mesurer_la_glycemie)
        FirebaseApp.initializeApp(applicationContext)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Bind date TextView
        tvDate = findViewById(R.id.sport5)

        // Bind sugar level EditText
        etSugarLevel = findViewById(R.id.sport)

        // Bind time TextViews
        timeTextViews = listOf(
            findViewById(R.id.textView50),
            findViewById(R.id.textView51),
            findViewById(R.id.textView52),
            findViewById(R.id.textView53),
            findViewById(R.id.textView54),
            findViewById(R.id.textView55)
        )
        // Set "قبل الفطور" as the default selected time
        findViewById<TextView>(R.id.textView50).isSelected = true
        selectedTime = "قبل الفطور"
        updateBackground()
        // Set today's date as the default date
        updateDateInView()

        // Set event listener for clicking the date TextView to open DatePickerDialog
        tvDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Set event listener for TextViews to handle time selection
        for (timeTextView in timeTextViews) {
            timeTextView.setOnClickListener {
                handleTimeSelection(timeTextView.text.toString())
            }
        }

        // ربط عنصر EditText بالساعة
        etTime = findViewById(R.id.etTime)

        // تعيين حدث النقر على EditText لفتح TimePickerDialog
        etTime.setOnClickListener {
            showTimePickerDialog()
        }

        // Bind the "إضافة" button
        val addTimeButton: TextView = findViewById(R.id.textView20)

        addTimeButton.setOnClickListener {
            val enteredTime = etTime.text.toString().trim()
            val enteredSugarLevel = etSugarLevel.text.toString().trim()
            if (enteredTime.isNotEmpty() && enteredSugarLevel.isNotEmpty()) {
                // Save the measurement to Firestore
                saveMeasurementToFirestore(enteredTime, enteredSugarLevel)

                // Save the date to Firestore
                showDialogo(Integer.parseInt(enteredSugarLevel))

                etTime.text.clear()
                etSugarLevel.text.clear()
            } else {
                // Handle the case where either time or sugar level is empty
                println("Error: Entered time or sugar level is empty")
            }
        }

        val textView19 = findViewById<TextView>(R.id.textView19)
        val etTime = findViewById<EditText>(R.id.etTime)
        val etSugarLevel = findViewById<EditText>(R.id.sport)

        textView19.setOnClickListener {
            etTime.text.clear()
            etSugarLevel.text.clear()
        }

        findViewById<View>(R.id.imageView6).setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this@Mesurer_la_glycemieActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        // Assume you have a method to get the current user ID (replace "getUserId()" with your actual method)
        userId = getUserId()
    }

    private fun getUserId(): String {
        /*val currentUser = FirebaseAuth.getInstance().currentUser
   return currentUser?.uid ?: "" */
        return "m0oXDIkgRvhl2zo1Hvtfuw8gSP72" // Replace this with your actual logic to get the user ID
    }

    private fun showDialogo(sugarLevelValue: Int) {
        val dialogId = when {
            sugarLevelValue > 130 -> R.layout.dialogue_rouge
            sugarLevelValue < 70 -> R.layout.dialogue_orange
            else -> R.layout.dialogue_vert
        }

        val dialogView = layoutInflater.inflate(dialogId, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        alertDialog.show()
    }

    private fun saveMeasurementToFirestore(enteredTime: String, enteredSugarLevel: String) {
        if (userId.isNotEmpty()) {
            val userRef =
                firestore.collection("Measurements").document(userId).collection("userMeasurements")
                    .document()

            // Save measurement data under a unique key
            userRef.set(
                mapOf(
                    "date" to tvDate.text.toString(),
                    "selectedTime" to selectedTime,
                    "etTime" to enteredTime,
                    "sugarLevelValue" to enteredSugarLevel
                )
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("Measurement saved successfully: $enteredSugarLevel")
                    } else {
                        println("Error saving measurement: ${task.exception}")
                    }
                }
        }
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                // When the time is set, update the etTime element and save the value in Firestore
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                updateDateTimeInView()

                // Save the time to Firestore
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )

        timePickerDialog.show()
    }

    private fun updateDateTimeInView() {
        val myFormat = "HH:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        etTime.setText(sdf.format(calendar.time))
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

    private fun handleTimeSelection(time: String) {
        selectedTime = time
        updateBackground()

        // Save the sugar level to Firestore
    }

    private fun updateBackground() {
        for (timeTextView in timeTextViews) {
            if (timeTextView.text == selectedTime) {
                timeTextView.setBackgroundResource(R.drawable.radio_button_unchecked)
            } else {
                timeTextView.setBackgroundResource(R.drawable.radio_button_checked)
            }
        }
    }

    // Add the following function to save the date to Firestore
    // Add the following function to save the date to Firestore
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateDateInView()

        // Save the date to Firestore
    }
}
