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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InsulinActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    // Calendar et formatters pour la gestion des dates et heures
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("d,MMMM,yyyy", Locale("ar"))
    private val timeFormatter = SimpleDateFormat("HH:mma", Locale("ar"))

    // Références aux éléments de l'interface utilisateur
    lateinit var insulin: EditText
    lateinit var date3: EditText
    lateinit var time3: EditText

    private lateinit var LittleEffectiveness: TextView
    private lateinit var LongEffective: TextView

    // Firebase
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insulin)

        // Retour à la page pharmaceutical
        val go2ImageView: ImageView = findViewById(R.id.go2)
        go2ImageView.setOnClickListener {
            startActivity(Intent(this, pharmaceuticalActivity::class.java))
        }

        // Initialisation des références aux éléments de l'interface utilisateur
        insulin = findViewById(R.id.insulin)
        date3 = findViewById(R.id.date3)
        time3 = findViewById(R.id.time3)

        // Sélection de la date
        date3.setOnClickListener {
            DatePickerDialog(
                this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Sélection de l'heure
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

        // Boutons LittleEffectiveness et LongEffective
        LittleEffectiveness = findViewById(R.id.textView29)
        LongEffective = findViewById(R.id.textView30)

        LittleEffectiveness.setOnClickListener { changeButtonColor(it as TextView) }
        LongEffective.setOnClickListener { changeButtonColor(it as TextView) }

        // Bouton "Annuler"
        val button_cancel: TextView = findViewById(R.id.cnnl)
        button_cancel.setOnClickListener {
            insulin.text.clear()
            date3.text.clear()
            time3.text.clear()
        }

        // Bouton "Ajouter" et dialogue
        val add3 = findViewById<TextView>(R.id.add3)
        add3.setOnClickListener {
            showCustomDialog()
        }
    }

    // Dialogue pour ajouter des données d'insuline
    private fun showCustomDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.dialogue_insulin, null)
        val myDialog = Dialog(this)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        // Bouton "Annuler" dans le dialogue
        val cancelBtn = dialogBinding.findViewById<ImageView>(R.id.cancel1)
        cancelBtn.setOnClickListener {
            // Appeler la fonction pour ajouter les données d'insuline à Firestore
            addInsulinDataToFirestore()

            myDialog.dismiss()
        }

        // Bouton "ajout" dans le dialogue
        val btnmerci: TextView = dialogBinding.findViewById(R.id.merci1)
        btnmerci.setOnClickListener {
            startActivity(Intent(this, MedicinesPillsActivity::class.java))
            myDialog.dismiss() // Dismiss the dialog after navigating to the new activity
        }

    }

    // Gestion de la sélection de la date
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.e("Calendar", "$year -- $month -- $dayOfMonth")
        calendar.set(year, month, dayOfMonth)
        displayFormattedDate(calendar.timeInMillis)
    }

    // Affichage de la date formatée
    private fun displayFormattedDate(timestamp: Long) {
        val date = Date(timestamp)
        date3.setText(dateFormatter.format(date))
        Log.i("Formatting", timestamp.toString())
    }

    // Changement de couleur des boutons LittleEffectiveness et LongEffective
    private fun changeButtonColor(button: TextView) {
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
    }

    // Réinitialisation des écouteurs de clic pour les boutons
    private fun resetClickListeners(vararg buttons: TextView) {
        buttons.forEach { button ->
            button.setOnClickListener { changeButtonColor(button) }
        }
    }

    // Fonction pour ajouter les données d'insuline à Firestore
    private fun addInsulinDataToFirestore() {
        val insulinText = insulin.text.toString()
        val dateText = date3.text.toString()
        val timeText = time3.text.toString()

        val user = auth.currentUser
        val uid = user?.uid

        val insulinData = hashMapOf(
            "insulin" to insulinText,
            "date" to dateText,
            "time" to timeText,
            "uid" to uid
        )

        firestore.collection("Insulin")
            .add(insulinData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Insulin data added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding insulin data", e)
            }
    }

    companion object {
        private const val TAG = "InsulinActivity"
    }
}
