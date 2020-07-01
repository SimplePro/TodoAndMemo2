package com.simplepro.secondtodoandmemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.simplepro.secondtodoandmemo.R
import com.simplepro.secondtodoandmemo.instance.UserInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_change_email_chapter_two.*

class ChangeEmailChapterTwoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email_chapter_two)
        backIntentImageViewChapterTwoEmail.setOnClickListener {
            val intent = Intent(this, ChangeEmailChapterOneActivity::class.java)
            startActivity(intent)
            finish()
        }

        changeEmailEnterButtonChapterTwo.setOnClickListener {
            if(changeEmailCheckEditTextChapterTwo.text.toString().isEmpty())
            {
                Toast.makeText(applicationContext, "이메일을 작성하여야 합니다.", Toast.LENGTH_LONG).show()
            }
            else {
                if(FirebaseAuth.getInstance().currentUser != null)
                {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val email = changeEmailCheckEditTextChapterTwo.text.toString()
                    var userDocRef =  FirebaseFirestore.getInstance().collection("users").document(userId)
                    var getEmail : String? = null
                    var getPassword : String? = null
                    userDocRef.get()
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful)
                            {
                                getEmail = task.result!!.getString("email")
                                getPassword = task.result!!.getString("password")
                            }
                        }
                    FirebaseAuth.getInstance().currentUser!!.updateEmail(email)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful)
                            {
                                val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                                var userGetPassword : String? = null
                                var userGetId : String? = null
                                userDocRef.get()
                                    .addOnCompleteListener {task ->
                                        userGetPassword = task.result!!.getString("password")
                                        userGetId = task.result!!.getString("id")
                                        val userData = UserInstance(userGetId.toString(), userGetPassword.toString(), email)
                                        userDocRef.set(userData)
                                            .addOnCompleteListener { task ->
                                                if(task.isSuccessful)
                                                {
                                                    Log.d("TAG", "유저 데이터 set 성공")
                                                    val intent = Intent(this, MainActivity::class.java)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                            }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(applicationContext, "통신에 실패하였거나 이메일 형식이 잘못 됬습니다. \n 다시시도해주세요.", Toast.LENGTH_LONG).show()
                                    }
                            }
                        }
                        .addOnFailureListener { exception ->
                            FirebaseAuth.getInstance().signOut()
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(getEmail.toString(), getPassword.toString())
                            Toast.makeText(applicationContext, "통신에 실패하였거나 이메일 형식이 잘못 됬습니다. \n 다시시도해주세요.", Toast.LENGTH_LONG).show()
                            Log.d("TAG", "이메일 변경 실패 $exception")
                        }

                }
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, ChangeEmailChapterOneActivity::class.java)
        startActivity(intent)
        finish()
    }
}