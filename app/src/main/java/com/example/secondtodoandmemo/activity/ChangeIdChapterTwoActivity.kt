package com.example.secondtodoandmemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.secondtodoandmemo.R
import com.example.secondtodoandmemo.instance.UserInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_change_id_chapter_two.*
import kotlinx.android.synthetic.main.activity_change_password_chapter_two.*

class ChangeIdChapterTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_id_chapter_two)

        backIntentImageViewChapterTwoId.setOnClickListener {
            val intent = Intent(this, ChangeIdChapterOneActivity::class.java)
            startActivity(intent)
            finish()
        }

        changeIdEnterButtonChapterTwo.setOnClickListener {
            if(changeIdCheckEditTextChapterTwo.text.toString().trim().length < 5)
            {
                Toast.makeText(applicationContext, "아이디는 5자 이상 15자 이하여야 합니다.", Toast.LENGTH_LONG).show()
            }
            else {
                if(FirebaseAuth.getInstance().currentUser != null)
                {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val id = changeIdCheckEditTextChapterTwo.text.toString()
                    val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                    var userGetPassword : String? = null
                    var userGetEmail : String? = null
                    userDocRef.get()
                        .addOnCompleteListener {task ->
                            userGetPassword = task.result!!.getString("password")
                            userGetEmail = task.result!!.getString("email")
                            val userData = UserInstance(id, userGetPassword.toString(), userGetEmail.toString())
                            userDocRef.set(userData)
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "통신에 실패하였습니다.", Toast.LENGTH_LONG).show()
                        }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}