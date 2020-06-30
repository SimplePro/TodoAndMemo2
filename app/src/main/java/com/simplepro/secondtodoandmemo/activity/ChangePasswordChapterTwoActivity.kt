package com.simplepro.secondtodoandmemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.simplepro.secondtodoandmemo.instance.UserInstance
import com.simplepro.secondtodoandmemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_change_password_chapter_two.*

class ChangePasswordChapterTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password_chapter_two)

        backIntentImageViewChapterTwoPassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordChapterOneActivity::class.java)
            startActivity(intent)
            finish()
        }

        changePasswordEnterButtonChapterTwo.setOnClickListener {
            if(changePasswordCheckEditTextChapterTwo.text.toString().trim().length < 7)
            {
                Toast.makeText(applicationContext, "비밀번호는 7자 이상 15자 이하여야 합니다.", Toast.LENGTH_LONG).show()
            }
            else {
                if(FirebaseAuth.getInstance().currentUser != null)
                {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                    val password = changePasswordCheckEditTextChapterTwo.text.toString()
                    var userGetId : String? = null
                    var userGetEmail : String? = null
                    var userGetPassword : String? = null
                    userDocRef.get()
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful)
                            {
                                userGetEmail = task.result!!.getString("email")
                                userGetPassword = task.result!!.getString("password")
                            }
                        }
                    FirebaseAuth.getInstance().currentUser!!.updatePassword(password)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful)
                            {
                                userDocRef.get()
                                    .addOnCompleteListener {task ->
                                        userGetId = task.result!!.getString("id")
                                        val userData = UserInstance(userGetId.toString(), password, userGetEmail.toString())
                                        userDocRef.set(userData)
                                            .addOnCompleteListener { task ->
                                                if(task.isSuccessful)
                                                {
                                                    val intent = Intent(this, MainActivity::class.java)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                            }

                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(applicationContext, "통신에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                                    }
                            }
                        }
                        .addOnFailureListener { exception ->
                            FirebaseAuth.getInstance().signOut()
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(userGetEmail.toString(), userGetPassword.toString())
                            Toast.makeText(applicationContext, "통신에 실패하였습니다. 다시 시도해주세요. ", Toast.LENGTH_LONG).show()
                            Log.d("TAG", "업데이트 패스워드 실패 $exception")
                        }
                }
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, ChangePasswordChapterOneActivity::class.java)
        startActivity(intent)
        finish()
    }
}