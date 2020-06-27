package com.example.secondtodoandmemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.secondtodoandmemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var authUid : String

    lateinit var docRef : DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        loginButton.setOnClickListener {
            loginEmail()
        }

        GoSighUpActivityTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginEmail() {
        val email = emailEditTextLogin.text.toString()
        val password = passwordEditTextLogin.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
            if(task.isSuccessful) {
                moveNextPage()
                return@addOnCompleteListener
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "존재하지 않는 계정입니다.", Toast.LENGTH_LONG).show()
                Log.d("TAG", it.toString())
            }
    }

    private fun moveNextPage() {

        Log.d("TAG", "uid is ${FirebaseAuth.getInstance().uid}")
        if(FirebaseAuth.getInstance().currentUser != null)
        {
            authUid = FirebaseAuth.getInstance().currentUser!!.uid
            docRef = FirebaseFirestore.getInstance().collection("users").document(authUid)
            docRef.get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful)
                    {
                        Log.d("TAG", "exist")
                        val result = task.result?.getString("id")
                        Toast.makeText(applicationContext, result + "님 환영합니다", Toast.LENGTH_LONG).show()
                    }
                    else {
                        Log.d("TAG", "no exist No such document")
                    }
                }
                .addOnFailureListener {Exception ->
                    Log.d("TAG", "error is $Exception")
                }
        }
        var currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null)
        {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if(intent.hasExtra("secession"))
        {
            val secessionBoolean = intent.getBooleanExtra("secession", false)
            if(!secessionBoolean)
            {
                moveNextPage()
            }
        }
        else {
            moveNextPage()
        }
    }
}