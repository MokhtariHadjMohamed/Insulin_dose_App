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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CerealsActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    // Calendar et formatters pour la gestion des dates et heures
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("d,MMMM,yyyy", Locale("ar"))
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale("ar"))

    // Références aux éléments de l'interface utilisateur
    lateinit var date4: EditText
    lateinit var time4: EditText
    lateinit var imageView: ImageView
    lateinit var imageView2: ImageView
    lateinit var cereal2: TextView

    var num = 1

    // Firebase
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cereals)

        val go3ImageView: ImageView = findViewById(R.id.go3)
        go3ImageView.setOnClickListener {
            startActivity(Intent(this, pharmaceuticalActivity::class.java))
        }

        // Initialisation des références aux éléments de l'interface utilisateur
        date4 = findViewById(R.id.date4)
        time4 = findViewById(R.id.time4)
        imageView = findViewById(R.id.imagemois)
        imageView2 = findViewById(R.id.imageadd)
        cereal2 = findViewById(R.id.cereal2)

        // Sélection de la date
        date4.setOnClickListener {
            DatePickerDialog(
                this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Sélection de l'heure
        time4.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                time4.setText(timeFormatter.format(cal.time))
            }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        // Modification du nombre de céréales (moins ou plus)
        imageView.setOnClickListener {
            num--
            updateCerealText()
        }
        imageView2.setOnClickListener {
            num++
            updateCerealText()
        }

        // Ajout et dialogue
        val add2 = findViewById<TextView>(R.id.add2)

        add2.setOnClickListener{
            val dialogBinding = layoutInflater.inflate(R.layout.dialogue_insulin,null)

            val myDialog = Dialog(this)
            myDialog.setContentView(dialogBinding)

            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

            val canncelbtn1 = dialogBinding.findViewById<ImageView>(R.id.cancel1)
            canncelbtn1.setOnClickListener {
                // Appeler la fonction pour ajouter les données de céréales à Firestore
                addCerealDataToFirestore()

                myDialog.dismiss()
            }
            // Bouton "ajout" dans le dialogue
            val btnmerci: TextView = dialogBinding.findViewById(R.id.merci1)
            btnmerci.setOnClickListener {
                startActivity(Intent(this, MedicinesPillsActivity::class.java))
                myDialog.dismiss() // Dismiss the dialog after navigating to the new activity
            }
        }

        // Bouton "Annuler"
        val button_cancel3: TextView = findViewById(R.id.cnnl3)
        button_cancel3.setOnClickListener {
            cereal2.text = "حبة"
            date4.text.clear()
            time4.text.clear()
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
        date4.setText(dateFormatter.format(date))
        Log.i("Formatting", timestamp.toString())
    }

    // Mise à jour du texte des céréales
    private fun updateCerealText() {
        cereal2.text = "حبة   $num"
    }

    // Fonction pour ajouter les données de céréales à Firestore
    private fun addCerealDataToFirestore() {
        val dateText = date4.text.toString()
        val timeText = time4.text.toString()

        val user = auth.currentUser
        val uid = user?.uid

        val cerealData = hashMapOf(
            "date" to dateText,
            "time" to timeText,
            "cereal" to num,
            "uid" to uid
        )

        firestore.collection("Pills")
            .add(cerealData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Cereal data added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding cereal data", e)
            }
    }

    companion object {
        private const val TAG = "CerealsActivity"
    }
}
