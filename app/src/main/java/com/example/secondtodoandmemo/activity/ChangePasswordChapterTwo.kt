package com.example.secondtodoandmemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.secondtodoandmemo.Instance.UserForm
import com.example.secondtodoandmemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_change_password_chapter_two.*

class ChangePasswordChapterTwo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password_chapter_two)

        backIntentImageViewChapterTwo.setOnClickListener {
            val intent = Intent(this, ChangePasswordChapterOne::class.java)
            startActivity(intent)
            finish()
        }

        changePasswordEnterButtonChapterTwo.setOnClickListener {
            if(changePasswordCheckEditTextChapterTwo.text.toString().trim().length < 5)
            {
                Toast.makeText(applicationContext, "비밀번호는 5자 이상 15자 이하여야 합니다.", Toast.LENGTH_LONG).show()
            }
            else {
                if(FirebaseAuth.getInstance().currentUser != null)
                {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val password = changePasswordCheckEditTextChapterTwo.text.toString()
                    FirebaseAuth.getInstance().currentUser!!.updatePassword(password)
                    val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                    var userGetId : String? = null
                    var userGetEmail : String? = null
                    userDocRef.get()
                        .addOnCompleteListener {task ->
                            userGetId = task.result!!.getString("id")
                            userGetEmail = task.result!!.getString("email")
                            val userData = UserForm(userGetId.toString(), password, userGetEmail.toString())
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