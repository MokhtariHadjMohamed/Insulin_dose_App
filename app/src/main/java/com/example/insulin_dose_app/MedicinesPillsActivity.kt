package com.example.insulin_dose_app

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insulin_dose_app.RecViews.RecycleViewAdapterTreatment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.util.Date

class MedicinesPillsActivity : AppCompatActivity() {

    // treatment rec
    lateinit var rec: RecyclerView
    lateinit var treatmentList: ArrayList<Pills>
    lateinit var adapter: RecycleViewAdapterTreatment

    //firebase
    val firestore = Firebase.firestore
    val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines_pills)

        rec = findViewById(R.id.recyclerViewMedicinesPilles)
        rec.layoutManager = LinearLayoutManager(this)
        treatmentList = ArrayList()
        adapter = RecycleViewAdapterTreatment(treatmentList)
        getAllTreatment()

    }

    private fun getAllTreatment() {
        firestore.collection("Pills")
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { result ->
            for (document in result) {
                treatmentList.apply {
                    add(Pills(
                        Integer.parseInt(document.data["cereal"].toString()), document.data["date"].toString()
                        , document.data["time"].toString(), document.data["uid"].toString(), "Pill"))
                }
            }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
        rec.adapter = adapter
    }
}