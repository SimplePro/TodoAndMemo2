package com.simplepro.onlinetodoandmemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.simplepro.onlinetodoandmemo.R
import com.simplepro.onlinetodoandmemo.instance.TodoInstance

class TrophyRecyclerViewAdapter(val trophyList: ArrayList<TodoInstance>, val itemRemoveOnClick : itemRemoveOnClickListener)
    : RecyclerView.Adapter<TrophyRecyclerViewAdapter.CustomViewHolder>() {

    interface itemRemoveOnClickListener {
        fun itemRemove(view : View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.memo_todo_list_item, parent, false)
        return CustomViewHolder(view)
            .apply {
                removeButton.setOnClickListener {
                    val trophyId = trophyList[adapterPosition].todoId

                    trophyList.removeAt(adapterPosition)
                    if(FirebaseAuth.getInstance().currentUser != null)
                    {
                        val trophyDocRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                        trophyDocRef.collection("DoneTodo").document(trophyId).delete().addOnSuccessListener {
                            Toast.makeText(parent.context.applicationContext, "데이터가 삭제되었습니다.", Toast.LENGTH_LONG).show()
                        }
                    }
                    notifyItemRemoved(adapterPosition)
                    notifyItemChanged(adapterPosition, trophyList.size)
                    itemRemoveOnClick.itemRemove(it, adapterPosition)
                }
            }
    }

    override fun getItemCount(): Int {
        return trophyList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.todoTitleText.text = trophyList[position].todo
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoTitleText = itemView.findViewById<TextView>(R.id.memoTodoListTextView)
        val removeButton = itemView.findViewById<ImageView>(R.id.memoTodoListRemoveButton)
    }
}