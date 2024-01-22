package com.example.insulin_dose_app

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Integer.parseInt

class ProfileFragment : Fragment() {

    // Initialize references to UI elements
    lateinit var full_name: TextView
    lateinit var email: TextView
    lateinit var dateOfBirth: TextView
    lateinit var gender: TextView
    lateinit var height: TextView
    lateinit var weight: TextView
    lateinit var diabet: TextView
    lateinit var dateOfDiagnosis: TextView

    // Firebase
    val auth = Firebase.auth
    val firestore = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    companion object{
        fun  newIntance(): ProfileFragment{
            return ProfileFragment()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements with correct resource IDs
        full_name = view.findViewById(R.id.some_id)
        email = view.findViewById(R.id.david_jones)
        dateOfBirth = view.findViewById(R.id.textView37)
        gender = view.findViewById(R.id.textView43)
        height = view.findViewById(R.id.textView68)
        weight = view.findViewById(R.id.textView69)
        dateOfDiagnosis = view.findViewById(R.id.textView48)
        diabet = view.findViewById(R.id.textView67)

        // Call the function to get user data
        getUser(auth.currentUser?.uid.orEmpty())
    }

    private fun getUser(uid: String) {
        firestore.collection("Users")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { result ->
                if (result != null && !result.isEmpty) {
                    for (document in result) {
                        val data = document.data

                        // Check for null values before accessing data
                        val fullName = data["full_name"] as? String ?: ""
                        val email = data["email"] as? String ?: ""
                        val phone = data["phone"] as? Int ?: 0
                        val height = data["height"] as? String ?: ""
                        val weight = data["weight"] as? String
                        val dateOfBirth = data["dateOfBirth"] as? String
                        val gender = data["gender"] as? String
                        val dateOfDiagnosis = data["dateOfDiagnosis"] as? String
                        val diabet = data["diabet"] as? String

                        // Create a User object with the retrieved data
                        val user = User(
                            uid,
                            fullName,
                            email,
                            phone,
                            dateOfBirth,
                            gender,
                            height,
                            weight,
                            dateOfDiagnosis,
                            diabet
                        )

                        this.full_name.text = user.full_name
                        this.email.text = user.email
                        this.dateOfBirth.text = user.dateOfBirth
                        this.gender.text = user.gender
                        this.height.text = user.height.toString()
                        this.weight.text = user.weight.toString()
                        this.diabet.text = user.diabet
                        this.dateOfDiagnosis.text = user.dateOfDiagnosis

                        // Do something with the user object if needed
                        Log.d(TAG, "DocumentSnapshot data: $fullName")
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                // Log more detailed information about the failure
                Log.e(TAG, "Error getting user data", exception)
            }
    }
}
