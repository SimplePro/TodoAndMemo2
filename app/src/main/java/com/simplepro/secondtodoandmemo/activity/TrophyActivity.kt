package com.simplepro.secondtodoandmemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.simplepro.secondtodoandmemo.R
import com.simplepro.secondtodoandmemo.adapter.TrophyRecyclerViewAdapter
import com.simplepro.secondtodoandmemo.instance.TodoInstance
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_trophy.*

class TrophyActivity : AppCompatActivity(), TrophyRecyclerViewAdapter.itemRemoveOnClickListener {

    lateinit var trophyAdapter : TrophyRecyclerViewAdapter
    var trophyList = arrayListOf<TodoInstance>()
    lateinit var trophyDocRef : DocumentReference
    lateinit var lottieAnimationAlphaAnimation : Animation
    lateinit var startLottieAnimationAlphaAnimation : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trophy)

        lottieAnimationAlphaAnimation = AnimationUtils.loadAnimation(this, R.anim.lottie_animation_alpha_animation)
        startLottieAnimationAlphaAnimation = AnimationUtils.loadAnimation(this, R.anim.lottie_animation_alpha_animation2)

        trophyLeftButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        trophyAdapter = TrophyRecyclerViewAdapter(trophyList, this)

        trophyRecyclerView.apply {
            adapter = trophyAdapter
            layoutManager = LinearLayoutManager(this@TrophyActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        bringDoneTodoDataToFirebase()

    }

    //파이어베이스에서 던투두 데이터를 가져오는 메소드.
    private fun bringDoneTodoDataToFirebase() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        trophyDocRef = FirebaseFirestore.getInstance().collection("users").document(uid)
        trophyDocRef.collection("DoneTodo").get()
            .addOnSuccessListener { documentSnapshot ->
                for(doneTodoData in documentSnapshot)
                {
                    trophyList.add(0, doneTodoData.toObject(TodoInstance::class.java))
                    Log.d("TAG", "doneTodoList is ${trophyList[0].todoId} => ${trophyList[0].todo}, ${trophyList[0].content}}")
                    if(trophyList.isNotEmpty())
                    {
                        trophyAdapter.notifyDataSetChanged()
                        trophyLottieAnimationLayout.startAnimation(lottieAnimationAlphaAnimation)
                        Handler().postDelayed({
                            trophyLottieAnimationLayout.visibility = View.GONE
                        }, 500)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "던투두리스트 데이터 불러오기 실패 $exception")
            }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun itemRemove(view: View, position: Int) {
        if(trophyList.size == 0)
        {
            trophyLottieAnimationLayout.startAnimation(startLottieAnimationAlphaAnimation)
            Handler().postDelayed({
                trophyLottieAnimationLayout.visibility = View.VISIBLE
            }, 500)

        }
    }
}