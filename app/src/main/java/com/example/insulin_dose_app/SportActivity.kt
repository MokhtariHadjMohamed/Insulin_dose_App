package com.example.insulin_dose_app

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SportActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    // Calendar et formatters pour la gestion des dates et heures
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("d,MMMM,yyyy", Locale("ar"))
    private val timeFormatter = SimpleDateFormat("HH:mma", Locale("ar"))

    // Références aux éléments de l'interface utilisateur
    private lateinit var sport: EditText
    private lateinit var tvdate: EditText
    private lateinit var tvtime: EditText

    // Firebase
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport)

        // Initialisation des références aux éléments de l'interface utilisateur
        sport = findViewById(R.id.sport)
        tvdate = findViewById(R.id.tvdate)
        tvtime = findViewById(R.id.tvtime)

        // Bouton "Ajouter" et dialogue
        val add = findViewById<TextView>(R.id.add)
        add.setOnClickListener {
            showCustomDialog()
        }

        // Bouton "Annuler"
        val button_cancel2: TextView = findViewById(R.id.cnnl2)
        button_cancel2.setOnClickListener {
            sport.text.clear()
            tvdate.text.clear()
            tvtime.text.clear()
        }

        // Sélection de la date
        tvdate.setOnClickListener {
            showDatePicker()
        }

        // Sélection de l'heure
        tvtime.setOnClickListener {
            showTimePicker()
        }

        findViewById<View>(R.id.imageView3).setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this@SportActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    // Dialogue pour ajouter des données de sport
    private fun showCustomDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.dialogue_add, null)
        val myDialog = Dialog(this)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        // Bouton "Oui" dans le dialogue
        val yesbtn = dialogBinding.findViewById<TextView>(R.id.merci)
        yesbtn.setOnClickListener {
            // Appeler la fonction pour ajouter les données de sport à Firestore
            addSportDataToFirestore()

            myDialog.dismiss()
        }

        // Bouton "Annuler" dans le dialogue
        val canncelbtn = dialogBinding.findViewById<ImageView>(R.id.cancel)
        canncelbtn.setOnClickListener {
            myDialog.dismiss()
        }
    }

    // Gestion de la sélection de la date
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        displayFormattedDate(calendar.timeInMillis)
    }

    // Affichage de la date formatée
    private fun displayFormattedDate(timestamp: Long) {
        val date = Date(timestamp)
        tvdate.setText(dateFormatter.format(date))
    }

    // Gestion de la sélection de l'heure
    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            tvtime.setText(timeFormatter.format(cal.time))
        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    // Gestion de la sélection de la date avec DatePickerDialog
    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    // Fonction pour ajouter les données de sport à Firestore
    private fun addSportDataToFirestore() {
        val sportText = sport.text.toString()
        val dateText = tvdate.text.toString()
        val timeText = tvtime.text.toString()

        val user = auth.currentUser
        val uid = user?.uid

        val Sport = hashMapOf(
            "sport" to sportText,
            "date" to dateText,
            "time" to timeText,
            "uid" to uid
        )

        firestore.collection("Sport")
            .add(Sport)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Sport data added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding sport data", e)
            }
    }

    companion object {
        private const val TAG = "SportActivity"
    }
}
