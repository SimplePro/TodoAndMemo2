package com.example.todoandmemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        SignUpButton.setOnClickListener {
            createEmailId()
        }

        GoLoginActivityTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createEmailId() {
        val email = emailEditTextSignUp.text.toString()
        val password = passwordEditTextSignUp.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful)
            {
                moveNextPage()
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "계정을 만들지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                Log.d("TAG", it.toString())
            }
    }

    private fun moveNextPage() {
        var currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null)
        {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}