package com.example.secondtodoandmemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.widget.Toast
import com.example.secondtodoandmemo.instance.UserInstance
import com.example.secondtodoandmemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.Locale.filter
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    lateinit var authUid : String

    lateinit var docRef : DocumentReference

    //false 면 안 보여주는 것 true 면 보여주는 것
    var preViewPasswordBoolean : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

//        previewPasswordTextView.setOnClickListener {
//            Log.d("TAG", "setOnClickListener previewPasswordTextView")
//            if(preViewPasswordBoolean == false)
//            {
//                passwordEditTextSignUp.setRawInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
//                oneMorePasswordEditTextSignUp.setRawInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
//                preViewPasswordBoolean = true
//            }
//            else if(preViewPasswordBoolean == true)
//            {
//                passwordEditTextSignUp.setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
//                oneMorePasswordEditTextSignUp.setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
//                preViewPasswordBoolean = false
//            }
//        }

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
        val id = idEditTextSignUp.text.toString()
        val email = emailEditTextSignUp.text.toString()
        val password = passwordEditTextSignUp.text.toString()
        val oneMorePassword = oneMorePasswordEditTextSignUp.text.toString()


        if(id.trim().length < 5 || password.trim().length < 7 || oneMorePassword.trim().length < 7)
        {
            if(id.trim().length < 5 ) Toast.makeText(applicationContext, "아이디은 5자 이상 15자 이하여야 합니다.", Toast.LENGTH_LONG).show()
            else if(password.trim().length < 7 || oneMorePassword.trim().length < 7) Toast.makeText(applicationContext, "비밀번호는 7자 이상 15자 이하여야 합니다.", Toast.LENGTH_LONG).show()
        }
        else if(password != oneMorePassword)
        {
            Toast.makeText(applicationContext, "비밀번호가 같지 않습니다.", Toast.LENGTH_LONG).show()
        }
        else {
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

//        val id = idEditTextSignUp.text.toString()

//        if(FirebaseAuth.getInstance().currentUser != null)
//        {
////            val idData = hashMapOf("id" to id)
//
//            val idData = mapOf("id" to id)
//
//            authUid = FirebaseAuth.getInstance().currentUser!!.uid
//            docRef = FirebaseFirestore.getInstance().collection("users").document(authUid)
//            docRef.set(idData, SetOptions.merge())
//        }

    }

    private fun moveNextPage() {
        val email = emailEditTextSignUp.text.toString()
        val password = passwordEditTextSignUp.text.toString()
        val id = idEditTextSignUp.text.toString()

        if(FirebaseAuth.getInstance().currentUser != null)
        {

            val userData = UserInstance(id.trim(), password.trim(), email.trim())

            authUid = FirebaseAuth.getInstance().currentUser!!.uid
            docRef = FirebaseFirestore.getInstance().collection("users").document(authUid)
            docRef.set(userData, SetOptions.merge())

            docRef.collection("todo").document()
//            docRef.set(idData, SetOptions.merge())
        }

        var currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null)
        {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
//    filter : InputFilter = InputFilter() {
//        public CharSequence filter(CharSequence source, int start, int end,
//            Spanned dest, int dstart, int dend)
//        {
//            for (int i = start; i < end; i++) {
//            if (!isEnglishOrHebrew(source.charAt(i))) {
//                return "";
//            }
//        }
//            return null;
//        }
//
//        private boolean isEnglishOrHebrew(char c) {
//            . . .
//        }
//    };
//
//    edit.setFilters(new InputFilter[]{filter});

}