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
import kotlinx.android.synthetic.main.activity_change_id_chapter_two.*

class ChangeIdChapterTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_id_chapter_two)

        backIntentImageViewChapterTwoId.setOnClickListener {
            val intent = Intent(this, ChangeIdChapterOneActivity::class.java)
            startActivity(intent)
            finish()
        }

        var userPassword : String? = null

        if(intent.hasExtra("password"))
        {
            userPassword = intent.getStringExtra("password")
        }
        else {
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
                    var userGetEmail : String? = null
                    userDocRef.get()
                        .addOnSuccessListener {documentSnapshot ->
                            userGetEmail = documentSnapshot.getString("email")
                            val userData = UserInstance(id, userGetEmail.toString())
                            userDocRef.set(userData)
                                .addOnCompleteListener { task ->
                                    if(task.isSuccessful)
                                    {
                                        Log.d("TAG", "유저 데이터 set 성공")
                                    }
                                }
                        }
                        .addOnFailureListener {
                            FirebaseAuth.getInstance().signOut()
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(userGetEmail.toString(), userPassword.toString())
                            Toast.makeText(applicationContext, "통신에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                        }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, ChangeIdChapterOneActivity::class.java)
        startActivity(intent)
        finish()
    }
}