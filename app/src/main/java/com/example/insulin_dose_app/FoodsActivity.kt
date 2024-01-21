package com.example.insulin_dose_app

import android.app.Dialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class FoodsActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("d,MMMM,yyyy", Locale("en"))
    private val timeFormatter = SimpleDateFormat("HH:mma", Locale("en"))

    lateinit var food: EditText
    lateinit var date2: EditText
    lateinit var time2: EditText

    private lateinit var breakfastButton: TextView
    private lateinit var lunchButton: TextView
    private lateinit var dinnerButton: TextView

    private lateinit var back_btn: ImageView;

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Variable pour stocker le type de repas sélectionné
    private var selectedMealType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foods)

        food = findViewById(R.id.food)
        date2 = findViewById(R.id.date2)
        time2 = findViewById(R.id.time2)

        val add1 = findViewById<TextView>(R.id.add1)
        add1.setOnClickListener {
            showCustomDialog()
        }

        date2.setOnClickListener {
            showDatePicker()
        }

        time2.setOnClickListener {
            showTimePicker()
        }

        breakfastButton = findViewById(R.id.textView13)
        lunchButton = findViewById(R.id.textView14)
        dinnerButton = findViewById(R.id.textView15)

        breakfastButton.setOnClickListener { changeButtonColor(it as TextView, "الافطار") }
        lunchButton.setOnClickListener { changeButtonColor(it as TextView, " الغداء") }
        dinnerButton.setOnClickListener { changeButtonColor(it as TextView, "العشاء") }

        val button_cancel1: TextView = findViewById(R.id.cnnl1)
        button_cancel1.setOnClickListener {
            food.text.clear()
            date2.text.clear()
            time2.text.clear()
        }

        back_btn = findViewById(R.id.back_btn)
        back_btn.setOnClickListener{
            startActivity(Intent(this@FoodsActivity, HomeActivity::class.java))
            finish()
        }
    }

    private fun showCustomDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.dialogue_add, null)
        val myDialog = Dialog(this)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val yesBtn = dialogBinding.findViewById<TextView>(R.id.merci)
        yesBtn.setOnClickListener {
            // Appeler la fonction pour ajouter les données alimentaires à Firestore
            addFoodDataToFirestore()

            myDialog.dismiss()
        }

        val cancelBtn = dialogBinding.findViewById<ImageView>(R.id.cancel)
        cancelBtn.setOnClickListener {
            myDialog.dismiss()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        displayFormattedDate(calendar.timeInMillis)
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            time2.setText(timeFormatter.format(cal.time))
        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun displayFormattedDate(timestamp: Long) {
        val date = Date(timestamp)
        date2.setText(dateFormatter.format(date))
    }

    private fun addFoodDataToFirestore() {
        val foodText = food.text.toString()
        val dateText = date2.text.toString()
        val timeText = time2.text.toString()

        val user = auth.currentUser
        val uid = user?.uid

        if (selectedMealType != null) {
            val foodData = hashMapOf(
                "food" to foodText,
                "date" to dateText,
                "time" to timeText,
                "uid" to uid,
                "mealType" to selectedMealType
            )

            firestore.collection("Meals")
                .add(foodData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Food data added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding food data", e)
                }
        } else {
            Log.e(TAG, "Meal type not selected")
        }
    }

    private fun changeButtonColor(button: TextView, mealType: String) {
        val selectedBackground = ContextCompat.getDrawable(this, R.drawable.rectangle_8)
        val defaultBackground = ContextCompat.getDrawable(this, R.drawable.rectangle_9)

        when (button.id) {
            R.id.textView13 -> {
                breakfastButton.background = selectedBackground
                lunchButton.background = defaultBackground
                dinnerButton.background = defaultBackground
                resetClickListeners(lunchButton, dinnerButton)
            }
            R.id.textView14 -> {
                lunchButton.background = selectedBackground
                breakfastButton.background = defaultBackground
                dinnerButton.background = defaultBackground
                resetClickListeners(breakfastButton, dinnerButton)
            }
            R.id.textView15 -> {
                dinnerButton.background = selectedBackground
                breakfastButton.background = defaultBackground
                lunchButton.background = defaultBackground
                resetClickListeners(breakfastButton, lunchButton)
            }
        }

        selectedMealType = mealType
    }

    private fun resetClickListeners(vararg buttons: TextView) {
        buttons.forEach { button ->
            button.setOnClickListener { changeButtonColor(button, "") }
        }
    }

    companion object {
        private const val TAG = "FoodsActivity"
    }
}
