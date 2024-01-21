package com.example.insulin_dose_app

import android.app.Dialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InsulinActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val TAG = "InsulinActivity"

    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("d,MMMM,yyyy", Locale("en"))
    private val timeFormatter = SimpleDateFormat("HH:mma", Locale("en"))

    lateinit var insulin: EditText
    lateinit var date3: EditText
    lateinit var time3: EditText

    private lateinit var LittleEffectiveness: TextView
    private lateinit var LongEffective: TextView

    private val firestore = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    private var selectedInsulinType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insulin)

        val go2ImageView: ImageView = findViewById(R.id.go2)
        go2ImageView.setOnClickListener {
            startActivity(Intent(this, pharmaceuticalActivity::class.java))
        }

        insulin = findViewById(R.id.insulin)
        date3 = findViewById(R.id.date3)
        time3 = findViewById(R.id.time3)

        date3.setOnClickListener {
            DatePickerDialog(
                this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        time3.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                time3.setText(timeFormatter.format(cal.time))
            }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        LittleEffectiveness = findViewById(R.id.textView29)
        LongEffective = findViewById(R.id.textView30)

        LittleEffectiveness.setOnClickListener { changeButtonColor(it as TextView, "قليل الفعالية") }
        LongEffective.setOnClickListener { changeButtonColor(it as TextView, "طويل الفعالية") }
        selectedInsulinType = "طويل الفعالية"

        val button_cancel: TextView = findViewById(R.id.cnnl)
        button_cancel.setOnClickListener {
            insulin.text.clear()
            date3.text.clear()
            time3.text.clear()
        }

        val add3 = findViewById<TextView>(R.id.add3)
        add3.setOnClickListener {
            showCustomDialog()
        }
    }

    private fun showCustomDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.dialogue_insulin, null)
        val myDialog = Dialog(this)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val textView45 = dialogBinding.findViewById<TextView>(R.id.textView45)
        val timeText = time3.text.toString()
        textView45.text = getFormattedTime(timeText)

        val cancelBtn = dialogBinding.findViewById<ImageView>(R.id.cancel1)
        cancelBtn.setOnClickListener {
            addInsulinDataToFirestore()
            myDialog.dismiss()
        }

        val btnmerci: TextView = dialogBinding.findViewById(R.id.merci1)
        btnmerci.setOnClickListener {
            addInsulinDataToFirestore()
            startActivity(Intent(this, MedicinesPillsActivity::class.java))
            myDialog.dismiss()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.e(TAG, "$year -- $month -- $dayOfMonth")
        calendar.set(year, month, dayOfMonth)
        displayFormattedDate(calendar.timeInMillis)
    }

    private fun getFormattedTime(timeText: String): String {
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(timeText)
        return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(time)
    }

    private fun displayFormattedDate(timestamp: Long) {
        val date = Date(timestamp)
        date3.setText(dateFormatter.format(date))
        Log.i(TAG, timestamp.toString())
    }

    private fun changeButtonColor(button: TextView, insulinType: String) {
        val selectedBackground = ContextCompat.getDrawable(this, R.drawable.edit_text_shap_rec)
        val defaultBackground = ContextCompat.getDrawable(this, R.drawable.edit_text_shap_reccc)

        when (button.id) {
            R.id.textView29 -> {
                LongEffective.background = selectedBackground
                LittleEffectiveness.background = defaultBackground
                resetClickListeners(LittleEffectiveness)
            }
            R.id.textView30 -> {
                LittleEffectiveness.background = selectedBackground
                LongEffective.background = defaultBackground
                resetClickListeners(LongEffective)
            }
        }

        selectedInsulinType = insulinType
    }

    private fun resetClickListeners(vararg buttons: TextView) {
        buttons.forEach { button ->
            button.setOnClickListener { changeButtonColor(button, "") }
        }
    }

    private fun addInsulinDataToFirestore() {
        val insulinText = insulin.text.toString()
        val dateText = date3.text.toString()
        val timeText = time3.text.toString()

        Log.d(TAG, "insulinText: $insulinText, dateText: $dateText, timeText: $timeText, selectedInsulinType: $selectedInsulinType")

        val user = auth.currentUser
        val uid = user?.uid

        if (selectedInsulinType != null) {
            val insulinData = hashMapOf(
                "insulin" to insulinText,
                "date" to dateText,
                "time" to timeText,
                "uid" to uid,
                "type" to selectedInsulinType
            )

            firestore.collection("Insulin")
                .add(insulinData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Insulin data added with ID: ${documentReference.id}")
                    startNotificationService(insulinText, dateText, timeText)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding insulin data", e)
                }
        } else {
            Log.e(TAG, "Insulin type not selected")
        }
    }

    private fun startNotificationService(insulinText: String, dateText: String, timeText: String) {
        val serviceIntent = Intent(this, MyNotificationService::class.java).apply {
            putExtra("insulin", insulinText)
            putExtra("date", dateText)
            putExtra("time", timeText)
        }
        startService(serviceIntent)
    }

    companion object {
        private const val TAG = "InsulinActivity"
    }
}
