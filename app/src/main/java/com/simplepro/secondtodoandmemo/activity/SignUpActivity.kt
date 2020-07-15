package com.simplepro.secondtodoandmemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.simplepro.secondtodoandmemo.instance.UserInstance
import com.simplepro.secondtodoandmemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    lateinit var authUid : String

    lateinit var docRef : DocumentReference

    lateinit var termsDialog: AlertDialog.Builder
    lateinit var termsEdialog: LayoutInflater
    lateinit var termsMView: View
    lateinit var termsBuilder: AlertDialog

    lateinit var termsCheckBox : CheckBox
    lateinit var termsCancelButton : Button

    //이용약관을 동의하면 true, 아니면 false
    var termsBoolean = false

    //false 면 안 보여주는 것 true 면 보여주는 것
    var preViewPasswordBoolean : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        termsDialog = AlertDialog.Builder(this)
        termsEdialog = LayoutInflater.from(this)
        termsMView = termsEdialog.inflate(R.layout.terms_dialog, null)
        termsBuilder = termsDialog.create()

        termsCheckBox = termsMView.findViewById(R.id.termsCheckBox)
        termsCancelButton = termsMView.findViewById(R.id.termsDialogCancelButton)

        termsCancelButton.setOnClickListener {
            termsBoolean = termsCheckBox.isChecked
            termsBuilder.dismiss()
        }

        showTermsDialogTextView.setOnClickListener {
            termsBuilder.setView(termsMView)
            termsBuilder.show()
        }

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


        if(termsBoolean == false)
        {
            Toast.makeText(applicationContext, "이용약관에 동의해주세요.", Toast.LENGTH_LONG).show()
        }
        else if(id.trim().length < 5 || password.trim().length < 7 || oneMorePassword.trim().length < 7 || email.isEmpty())
        {
            if(id.trim().length < 5 ) Toast.makeText(applicationContext, "아이디은 5자 이상 15자 이하여야 합니다.", Toast.LENGTH_LONG).show()
            else if(password.trim().length < 7 || oneMorePassword.trim().length < 7) Toast.makeText(applicationContext, "비밀번호는 7자 이상 15자 이하여야 합니다.", Toast.LENGTH_LONG).show()
            else if(email.isEmpty()) Toast.makeText(applicationContext, "이메일을 작성하여야 합니다.", Toast.LENGTH_LONG).show()
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
    }

    private fun moveNextPage() {
        val email = emailEditTextSignUp.text.toString()
        val password = passwordEditTextSignUp.text.toString()
        val id = idEditTextSignUp.text.toString()

        if(FirebaseAuth.getInstance().currentUser != null)
        {

            val userData = UserInstance(id.trim(), email.trim())

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

}