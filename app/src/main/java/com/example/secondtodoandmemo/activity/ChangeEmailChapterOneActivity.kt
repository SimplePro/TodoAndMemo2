package com.example.secondtodoandmemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.secondtodoandmemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_change_email_chapter_one.*
import kotlinx.android.synthetic.main.activity_change_id_chapter_one.*

class ChangeEmailChapterOneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email_chapter_one)

        backIntentImageViewChapterOneEmail.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        changeEmailEnterButtonChapterOne.setOnClickListener {
            if(FirebaseAuth.getInstance().currentUser != null)
            {
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                userDocRef.get().addOnCompleteListener {task ->
                    if(task.isSuccessful)
                    {
                        val getPassword = task.result!!.getString("password")
                        val password = changeEmailCheckEditTextChapterOne.text.toString()
                        if(getPassword == password)
                        {
                            val intent = Intent(this, ChangeEmailChapterTwoActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else {
                            Toast.makeText(applicationContext, "비밀번호가 같지 않습니다.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, "통신에 실패하였습니다.", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}