package com.simplepro.secondtodoandmemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.simplepro.secondtodoandmemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_change_id_chapter_one.*

class ChangeIdChapterOneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_id_chapter_one)

        backIntentImageViewChapterOneId.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        var password : String? = null

        changeIdEnterButtonChapterOne.setOnClickListener {
            if(FirebaseAuth.getInstance().currentUser != null)
            {
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                password = changeIdCheckEditTextChapterOne.text.toString()
                userDocRef.get().addOnSuccessListener {documentSnapshot ->
                    val userEmail = documentSnapshot.getString("email")
                    FirebaseAuth.getInstance().signOut()
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail.toString(), password.toString())
                        .addOnSuccessListener { documentSnapshot ->
                            val intent = Intent(this, ChangeIdChapterTwoActivity::class.java)
                            intent.putExtra("password", password)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(applicationContext, "비밀번호가 같지 않습니다.", Toast.LENGTH_LONG).show()
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