package com.example.insulin_dose_app

import android.app.ProgressDialog
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

class MedicinesPillsActivity : AppCompatActivity() {

    // treatment rec
    lateinit var rec: RecyclerView
    lateinit var treatmentList: ArrayList<PillsAndInsulin>
    lateinit var adapter: RecycleViewAdapterTreatment
    lateinit var progressDialog:ProgressDialog

    //firebase
    val firestore = Firebase.firestore
    val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines_pills)

        progressDialog = ProgressDialog(this@MedicinesPillsActivity)

        rec = findViewById(R.id.recyclerViewMedicinesPilles)
        rec.layoutManager = LinearLayoutManager(this)
        treatmentList = ArrayList()
        adapter = RecycleViewAdapterTreatment(treatmentList)
        getAllTreatment()

    }

    private fun getAllTreatment(){
        progressDialog.setTitle("تحميل قائمة الأدوية ")
        progressDialog.setMessage("تحميل...")
        progressDialog.show()
        getAllPills()
    }

    private fun getAllPills() {
        firestore.collection("Pills")
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { result ->
            for (document in result) {
                treatmentList.apply {
                    add(PillsAndInsulin(
                        Integer.parseInt(document.data["cereal"].toString()), document.data["date"].toString()
                        , document.data["time"].toString(), document.data["uid"].toString(), "Pill"))
                }
            }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
                progressDialog.dismiss()
            }
        rec.adapter = adapter
        getAllInsulin()
    }

    private fun getAllInsulin() {
        firestore.collection("Insulin")
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { result ->
            for (document in result) {
                treatmentList.apply {
                    add(PillsAndInsulin(
                        Integer.parseInt(document.data["insulin"].toString()), document.data["date"].toString()
                        , document.data["time"].toString(), document.data["uid"].toString(), "Insulin"))
                }
            }
                adapter.notifyDataSetChanged()
                progressDialog.dismiss()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
                progressDialog.dismiss()
            }
        rec.adapter = adapter
    }
}