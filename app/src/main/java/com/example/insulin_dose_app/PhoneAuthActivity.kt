package com.example.insulin_dose_app


import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.goodiebag.pinview.Pinview
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Objects
import java.util.concurrent.TimeUnit

class PhoneAuthActivity : AppCompatActivity(),OnClickListener {

    lateinit var pinFromUser: Pinview
    lateinit var next:CardView
    lateinit var phoneNumberPhoneAuth:TextView
    lateinit var resend:Button
    lateinit var timingPhoneAuth:TextView
    lateinit var codeBySystem:String
    //firebase
    val auth = Firebase.auth
    val uid = auth.currentUser!!.uid
    val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        auth.signOut()

        pinFromUser = findViewById(R.id.pinEnterPhoneAuth)
        pinFromUser.apply {
            setTextColor(R.color.text03)
        }

        next = findViewById(R.id.logInbtn)
        resend = findViewById(R.id.resendPhoneAuth)
        timingPhoneAuth = findViewById(R.id.timingPhoneAuth)

        resend.setOnClickListener(this)

        next.setOnClickListener(this)

        val phoneNumber = intent.getStringExtra("Phone").toString()

        phoneNumberPhoneAuth = findViewById(R.id.phoneNumberPhoneAuth)
        phoneNumberPhoneAuth.text = phoneNumber

        timer()
        sendVerificationCodeToUser(phoneNumber)
    }

    private fun upDateUser(uid:String){
        firestore.collection("Users").document(uid).update("uid2", auth.currentUser!!.uid)
    }

    private fun timer(){
        val timer = object: CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timingPhoneAuth.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                resend.visibility = View.VISIBLE
            }
        }
        timer.start()
    }
    private fun sendVerificationCodeToUser(phoneNumber:String){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider
                .OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(
                    verificationId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken,
                ) {
                    codeBySystem = verificationId;
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    val code:String? = phoneAuthCredential.smsCode
                    if (code != null){
                        Toast.makeText(this@PhoneAuthActivity, code, Toast.LENGTH_SHORT).show()
                        pinFromUser.value = code

                        verifyCode(code)
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // ...
                    Log.d(TAG, e.message.toString())
                }
            }) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode(code:String){
        val credential:PhoneAuthCredential = PhoneAuthProvider.getCredential(codeBySystem, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    startActivity(Intent(this@PhoneAuthActivity, InfoPersonnelActivity::class.java))
                    upDateUser(uid)

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this@PhoneAuthActivity, "Verification Not Completed! Try again.", Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }

    fun callNextScreenFromOTP(){
        val code:String = pinFromUser.value
        if (!code.isEmpty()){
            verifyCode((code))
        }
    }

    override fun onClick(v: View?) {
        when(v){
            next -> callNextScreenFromOTP()
            resend ->{
                timer()
                sendVerificationCodeToUser(intent.getStringExtra("Phone").toString())
            }
        }
    }
}