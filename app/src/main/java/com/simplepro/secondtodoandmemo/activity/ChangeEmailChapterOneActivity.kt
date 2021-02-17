package com.simplepro.secondtodoandmemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.simplepro.secondtodoandmemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_change_email_chapter_one.*

class ChangeEmailChapterOneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email_chapter_one)

        backIntentImageViewChapterOneEmail.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        var email : String? = null

        if(FirebaseAuth.getInstance().currentUser != null)
        {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userId)
            userDocRef.get()
                .addOnSuccessListener {documentSnapshot ->
                    email = documentSnapshot.getString("email")
                    Log.d("TAG", "가져오기 성공 success $email")
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "통신에 실패하였습니다.", Toast.LENGTH_LONG).show()
                }

        changeEmailEnterButtonChapterOne.setOnClickListener {
            val password = changeEmailCheckEditTextChapterOne.text.toString()

                FirebaseAuth.getInstance().signOut()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.toString(), password)
                    .addOnSuccessListener { authResult ->
                        val intent = Intent(this, ChangeEmailChapterTwoActivity::class.java)
                        intent.putExtra("password", password)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(applicationContext, "비밀번호가 같지 않습니다.", Toast.LENGTH_LONG).show()
                    }
//                FirebaseAuth.getInstance().signOut()
//                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.toString(), password)
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}