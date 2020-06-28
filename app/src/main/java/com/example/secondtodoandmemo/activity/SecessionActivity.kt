package com.example.secondtodoandmemo.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.secondtodoandmemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import io.grpc.internal.SharedResourceHolder
import kotlinx.android.synthetic.main.activity_secession.*
import kotlinx.android.synthetic.main.secession_dialog.view.*

class SecessionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secession)

        backIntentImageViewSecession.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        secessionEnterButton.setOnClickListener {
            if(FirebaseAuth.getInstance().currentUser != null)
            {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val docRef = FirebaseFirestore.getInstance().collection("users").document(userId)
//                val todoDocRef = docRef.collection("tod o") //보류
//                val memoDocRef = docRef.collection("memo") //보류
//                val doneTodoRef = docRef.collection("doneTodo") //보류
                val password = secessionCheckPasswordEditText.text.toString()
                var userPassword : String?
                var userEmail : String?
                docRef.get()
                    .addOnCompleteListener {task ->
                        userPassword = task.result!!.getString("password")
                        userEmail = task.result!!.getString("email")
                        if(userPassword == password)
                        {
                            val secessionDialog = AlertDialog.Builder(this)
                            val secessionEDialog = LayoutInflater.from(this)
                            val secessionMView = secessionEDialog.inflate(R.layout.secession_dialog, null)
                            val secessionBuilder = secessionDialog.create()
//                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                            dialog.setContentView(layout);

                            val secessionAnswerButton = secessionMView.secessionAnswerButtonDialog
                            val secessionCancelButton = secessionMView.secessionCancelButtonDialog


                            secessionBuilder.setView(secessionMView)
                            secessionMView.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            secessionBuilder.show()

                            secessionAnswerButton.setOnClickListener {
                                currentUser?.delete()?.addOnCompleteListener {task ->
                                    if(task.isSuccessful)
                                    {
                                        docRef.delete().addOnCompleteListener {task ->
                                            if (task.isSuccessful)
                                            {
                                                Log.d("TAG", "탈퇴 - 유저 데이터 삭제 성공")
                                                Toast.makeText(applicationContext, "성공적으로 탈퇴하였습니다.", Toast.LENGTH_LONG).show()
                                                secessionBuilder.dismiss()
                                                val intent = Intent(this, LoginActivity::class.java)
                                                intent.putExtra("secession", true)
                                                startActivity(intent)
                                                finish()
                                            }
                                        }
                                            .addOnFailureListener { exception ->
                                                Log.d("TAG", "탈퇴 - 유저 데이터 삭제 실패 $exception")
                                            }

                                    }
                                }
                                    ?.addOnFailureListener {Exception ->
                                        FirebaseAuth.getInstance().signOut()
                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail.toString(), userPassword.toString())
                                        Toast.makeText(applicationContext, "탈퇴를 하지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()

                                        Log.d("TAG", "탈퇴를 하지 못했습니다. $Exception")
                                    }
                            }

                            secessionCancelButton.setOnClickListener {
                                secessionBuilder.dismiss()
                            }


                        }
                        else {
                            Toast.makeText(applicationContext, "비밀번호가 같지 않습니다.", Toast.LENGTH_LONG).show()
                        }
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