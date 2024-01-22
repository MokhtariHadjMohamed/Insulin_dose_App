package com.example.insulin_dose_app

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insulin_dose_app.RecViews.RecycleViewAdapterTreatment
import com.example.insulin_dose_app.RecViews.OnItemClickListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MedicinesPillsActivity : AppCompatActivity(), OnItemClickListener {

    lateinit var rec: RecyclerView
    lateinit var treatmentList: ArrayList<PillsAndInsulin>
    lateinit var adapter: RecycleViewAdapterTreatment
    lateinit var progressDialog: ProgressDialog
    val firestore = Firebase.firestore
    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines_pills)

        progressDialog = ProgressDialog(this@MedicinesPillsActivity)
        val imageView19: ImageView = findViewById(R.id.imageView19)
        imageView19.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        rec = findViewById(R.id.recyclerViewMedicinesPilles)
        rec.layoutManager = LinearLayoutManager(this)
        treatmentList = ArrayList()
        adapter = RecycleViewAdapterTreatment(treatmentList, this)
        getAllTreatment()
    }

    private fun getAllTreatment() {
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
                    treatmentList.add(
                        PillsAndInsulin(
                            Integer.parseInt(document.data["cereal"].toString()),
                            document.data["date"].toString(),
                            document.data["time"].toString(),
                            document.data["uid"].toString(),
                            "Pill"
                        )
                    )
                }
                getAllInsulin()
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
                progressDialog.dismiss()
            }
    }

    private fun getAllInsulin() {
        firestore.collection("Insulin")
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    treatmentList.add(
                        PillsAndInsulin(
                            Integer.parseInt(document.data["insulin"].toString()),
                            document.data["date"].toString(),
                            document.data["time"].toString(),
                            document.data["uid"].toString(),
                            "Insulin"
                        )
                    )
                }
                adapter.notifyDataSetChanged()
                progressDialog.dismiss()
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
                progressDialog.dismiss()
            }
        rec.adapter = adapter
    }

    // Implement the click listener function
    override fun onItemClick(position: Int) {
        deleteItem(position)
    }

    // Function to delete item
    private fun deleteItem(position: Int) {
        if (position in 0 until treatmentList.size) {
            val itemToDelete = treatmentList[position]

            // Remove item from the local list
            treatmentList.removeAt(position)
            adapter.notifyItemRemoved(position)

            // Update Firebase Firestore to remove the corresponding document
            val collectionName = if (itemToDelete.type == "Pill") "Pills" else "Insulin"

            firestore.collection(collectionName)
                .whereEqualTo("uid", auth.currentUser!!.uid)
                .whereEqualTo("date", itemToDelete.date)
                .whereEqualTo("time", itemToDelete.time)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        // Delete the document from Firestore
                        firestore.collection(collectionName).document(document.id).delete()
                            .addOnSuccessListener {
                                Log.d("TAG", "Document successfully deleted from Firestore.")
                            }
                            .addOnFailureListener { exception ->
                                Log.d("TAG", "Error deleting document from Firestore: ", exception)
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "Error getting documents from Firestore: ", exception)
                }
        } else {
            Log.d("TAG", "Invalid position: $position")
        }
    }
}
