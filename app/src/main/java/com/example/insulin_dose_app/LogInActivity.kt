package com.example.insulin_dose_app

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity(), OnClickListener {

    lateinit var registerBtnLogIn: TextView
    lateinit var emailLogIn:EditText
    lateinit var passwordLogIn:EditText
    lateinit var logInbtn:CardView

    //Firebase
    val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        registerBtnLogIn = findViewById(R.id.registerBtnLogIn)
        registerBtnLogIn.setOnClickListener(this)

        emailLogIn = findViewById(R.id.emailLogIn)
        passwordLogIn = findViewById(R.id.passwordLogIn)

        logInbtn = findViewById(R.id.logInbtn)
        logInbtn.setOnClickListener(this)
    }

    fun signInWithEmailAndPassword(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    startActivity(Intent(this@LogInActivity, HomeActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    override fun onClick(v: View?) {
        when(v){
            registerBtnLogIn -> {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }
            logInbtn->{
                signInWithEmailAndPassword(emailLogIn.text.toString(), passwordLogIn.text.toString())
            }
        }
    }
}