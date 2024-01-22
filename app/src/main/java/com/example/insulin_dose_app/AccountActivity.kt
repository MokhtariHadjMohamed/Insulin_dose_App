package com.example.insulin_dose_app

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Integer.parseInt

class AccountActivity : AppCompatActivity(), OnClickListener {

    //element
    lateinit var full_name:EditText
    lateinit var email:EditText
    lateinit var phone:EditText
    lateinit var genderRadioGroup: RadioGroup
    var selectedGender: String? = null
    lateinit var submit:TextView
    lateinit var cancel:TextView
    lateinit var weight:EditText
    lateinit var height:EditText
    lateinit var progressDialog: ProgressDialog

    //firebase
    val firestor = Firebase.firestore
    val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        progressDialog = ProgressDialog(this@AccountActivity, ProgressDialog.THEME_HOLO_DARK)
        progressDialog.setMessage("تحميل ...")
        progressDialog.show()

        full_name = findViewById(R.id.nameAccountActivity)
        email = findViewById(R.id.emailAccountActivity)
        phone = findViewById(R.id.phoneAccountActivity)

        submit = findViewById(R.id.submitAccountActivity)
        submit.setOnClickListener(this)
        cancel = findViewById(R.id.cancelAccountActivity)
        cancel.setOnClickListener(this)

        genderRadioGroup = findViewById(R.id.genderRadioGroupAccountActivity)

        genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_maleAccountActivity -> {
                    selectedGender = "Male"
                    updateGenderBackground()
                }
                R.id.radio_femaleAccountActivity -> {
                    selectedGender = "Female"
                    updateGenderBackground()
                }
            }
        }

        findViewById<View>(R.id.imageView39).setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this@AccountActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        weight = findViewById(R.id.weightAccountActivity)
        height = findViewById(R.id.heightAccountActivity)

        getUserInfo()
    }

    fun updateGenderBackground() {
        val maleRadioButton: RadioButton = findViewById(R.id.radio_maleAccountActivity)
        val femaleRadioButton: RadioButton = findViewById(R.id.radio_femaleAccountActivity)

        if (selectedGender == "Male") {
            maleRadioButton.setBackgroundResource(R.drawable.gender_checked_background_male)
            femaleRadioButton.setBackgroundResource(R.drawable.gender_unchecked_background_female)
        } else if (selectedGender == "Female") {
            maleRadioButton.setBackgroundResource(R.drawable.gender_unchecked_background_male)
            femaleRadioButton.setBackgroundResource(R.drawable.gender_checked_background_female)
        } else {
            maleRadioButton.setBackgroundResource(R.drawable.gender_unchecked_background_male)
            femaleRadioButton.setBackgroundResource(R.drawable.gender_unchecked_background_female)
        }
    }

    fun getUserInfo(){
        firestor.collection("Users")
            .document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {document ->
                val user = document.toObject(User::class.java)
                if (user != null) {
                    full_name.setText(user.full_name)
                    email.setText(user.email)
                    phone.setText(user.phone.toString())
                    full_name.setText(user.full_name)
                    if (user.gender == "Male")
                        genderRadioGroup.check(R.id.radio_maleAccountActivity)
                    else if (user.gender == "Female")
                        genderRadioGroup.check(R.id.radio_femaleAccountActivity)
                    weight.setText(user.weight)
                    height.setText(user.height)
                }
                progressDialog.dismiss()
            }.addOnFailureListener{e ->
                Log.e(TAG, e.message.toString())
            }
    }

    fun updateUser(){
        firestor.collection("Users")
            .document(auth.currentUser!!.uid)
            .update("full_name", full_name.text.toString(),
                "email", email.text.toString(),
                "phone", parseInt(phone.text.toString()),
                "gender", selectedGender,
                "height", height.text.toString(),
                "weight", weight.text.toString())
            .addOnSuccessListener {
                startActivity(Intent(this@AccountActivity,HomeActivity::class.java))
                finish()
            }
            .addOnFailureListener{e ->
                Log.e(TAG, e.message.toString())
            }
    }

    override fun onClick(v: View?) {
        when(v){
            submit -> updateUser()
            cancel -> {
                startActivity(Intent(this@AccountActivity,HomeActivity::class.java))
                finish()
            }
        }
    }
}