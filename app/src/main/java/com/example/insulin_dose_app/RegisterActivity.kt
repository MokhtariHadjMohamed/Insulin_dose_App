package com.example.insulin_dose_app

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Integer.parseInt

class RegisterActivity : AppCompatActivity(), OnClickListener {

    lateinit var registerBtn: CardView
    lateinit var logIn:TextView
    lateinit var name:EditText
    lateinit var email: EditText
    lateinit var password01:EditText
    lateinit var password02:EditText
    lateinit var phone:EditText
    lateinit var progressDialog:ProgressDialog
    // Firebase
    val auth = Firebase.auth
    val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Sign in...")

        name = findViewById(R.id.nameRegisterActivity)
        email = findViewById (R.id.emailRegisterActivity)
        password01 = findViewById(R.id.passwordRegisterActivity)
        password02 = findViewById(R.id.password2RegisterActivity)
        phone = findViewById(R.id.phoneRegisterActivity)

        registerBtn = findViewById(R.id.registerbtn)
        registerBtn.setOnClickListener(this)

        logIn = findViewById(R.id.logInRegisterActivity)
        logIn.setOnClickListener(this)
    }

    private fun addUserToFirestor(user:User){
        firestore.collection("Users").document(user.uid).set(user)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
                opt(user.phone)
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun editTextTest(arr:Array<EditText>): Boolean {
        for (edit in arr){
            if (edit.text.toString() == ""){
                edit.setBackgroundResource(R.drawable.edit_text_shap_error)
                return false
            }
            edit.setBackgroundResource(R.drawable.edit_text_shap)
        }
        return true
    }

    fun opt(phone:Int){
        val intent = Intent(this, PhoneAuthActivity::class.java)
        intent.putExtra("Phone", "+" + "213" + phone)
        startActivity(intent)
        progressDialog.dismiss()

    }

    override fun onClick(v: View?) {
        when(v){
            registerBtn->{
                progressDialog.show()

                var arr = arrayOf(name, email, password01, password02, phone)
                if (editTextTest(arr)) {
                    if (password01.text.toString() == password02.text.toString()) {
                        auth.createUserWithEmailAndPassword(
                            email.text.toString(),
                            password01.text.toString()
                        )
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success")
                                    val user = auth.currentUser
                                    Toast.makeText(
                                        baseContext,
                                        "Authentication success.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                    addUserToFirestor(
                                        User(
                                            user!!.uid,
                                            name.text.toString(),
                                            email.text.toString(),
                                            parseInt(phone.text.toString())
                                        )
                                    )
                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressDialog.dismiss()

                                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                    Toast.makeText(
                                        baseContext,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            }
                    } else {
                        progressDialog.dismiss()

                        password01.setBackgroundResource(R.drawable.edit_text_shap_error);
                    }
                }
            }
            logIn->{
                startActivity(Intent(this, LogInActivity::class.java))
                finish()
            }
        }
    }
}