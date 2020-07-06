package com.simplepro.secondtodoandmemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.simplepro.secondtodoandmemo.R
import com.simplepro.secondtodoandmemo.instance.TodoInstance

class TrophyRecyclerViewAdapter(val trophyList: ArrayList<TodoInstance>)
    : RecyclerView.Adapter<TrophyRecyclerViewAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.memo_todo_list_item, parent, false)
        return CustomViewHolder(view)
            .apply {
                removeButton.setOnClickListener {
                    trophyList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    notifyItemChanged(adapterPosition, trophyList.size)
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