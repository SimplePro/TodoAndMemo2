package com.simplepro.secondtodoandmemo.viewModel

import androidx.databinding.ObservableField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.simplepro.secondtodoandmemo.instance.UserInstance

class NavigationViewModel(user: UserInstance) {
//    val uid = FirebaseAuth.getInstance().currentUser!!.uid
//    val docRef = FirebaseFirestore.getInstance().collection("users").document(uid)
//    val runnable = Runnable {
//        docRef.get()
//            .addOnCompleteListener { task ->
//                if(task.isSuccessful)
//                {
                    val userEmail = ObservableField<String>(user.email)
                    val userId = ObservableField<String>(user.id)
//                }
//            }
//    }
//    val handler = android.os.Handler()?.run {
//        postDelayed(runnable, 500)
//    }

}
