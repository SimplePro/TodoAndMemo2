package com.example.secondtodoandmemo.adapter

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.secondtodoandmemo.instance.TodoInstance
import com.example.secondtodoandmemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*
import kotlin.collections.ArrayList

class TodoRecyclerViewAdapter(val todoList: ArrayList<TodoInstance>, val DoneTodoList: ArrayList<TodoInstance>, private val DoneListener: todoItemClickListener, var todoSearchList : ArrayList<TodoInstance>)
    : RecyclerView.Adapter<TodoRecyclerViewAdapter.CustomViewHolder>(), Filterable {

//    var todoSearchList : ArrayList<TodoForm>

    lateinit var context : Context

    lateinit var DoneTodoDocRef : DocumentReference

    init {
        todoSearchList = todoList
        notifyItemChanged(itemCount)
    }

    //todoItem 의 Done 버튼이 클릭 되었을 때 호출되는 콜백 함수.
    interface todoItemClickListener {
        fun todoOnItemClick(view: View, position: Int)
        fun todoOnItemReplaceClick(view: View, position: Int)
    }

    //역할 : 아이템이 생성되었을 때 실행됨.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context

        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)
        return CustomViewHolder(
            view
        ).apply {

            //todoItem 의 replace 버튼이 클릭 되었을 때
            replaceButton.setOnClickListener {
                saveTodoTitleAndContentData(todoSearchList[adapterPosition].todo, todoSearchList[adapterPosition].content)
                saveTodoIdData(todoSearchList[adapterPosition].todoId)
                //변수 선언
                DoneListener.todoOnItemReplaceClick(it, adapterPosition)
            }

            //todoItem 의 Done(replace) 버튼이 클릭 되었을 때
            DoneButton.setOnClickListener {
                saveTodoIdData(todoSearchList[adapterPosition].todoId)
                for(i in 0 .. todoList.size - 1)
                {
                    if(todoList[i].todoId == todoSearchList[adapterPosition].todoId)
                    {
                        DoneTodoList.add(todoList[i])
                        if(FirebaseAuth.getInstance().currentUser != null)
                        {
                            DoneTodoDocRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                            DoneTodoDocRef.collection("DoneTodo").document(todoList[i].todoId).set(todoList[i], SetOptions.merge()).addOnCompleteListener {
                                Log.d("TAG", "DoneTodoDocRef success")
                            }
                        }
                        Log.d("TAG", "DoneTodoList[0] = ${DoneTodoList[0].todo} ${DoneTodoList[0].content} ${DoneTodoList[0].todoId}")
                    }
                }
                //todoList 에 해당 position 의 값을 삭제함.
//                todoList.removeAt(adapterPosition)
//                todoSearchList = todoList
                //notify 로 recyclerView 에 반영함.
//                notifyItemRemoved(adapterPosition)
//                notifyItemChanged(adapterPosition, todoList.size)
                //Done(remove) 버튼이 클릭 되었을 때 해당 콜백 함수를 호출함.
                DoneListener.todoOnItemClick(it, adapterPosition)
            }
        }
    }

    //역할 : recyclerView 에 들어갈 item 의 개수를 반환하는 것.
    override fun getItemCount(): Int {
        return todoSearchList.size
    }

    //데이터를 할당함. (꾸며주는 것. text = string)
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.todoText.text = todoSearchList[position].todo
    }

    //데이터를 BindViewHolder 에 넘겨주는 것
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoText = itemView.findViewById<TextView>(R.id.todoListTextView)
        val DoneButton = itemView.findViewById<ImageView>(R.id.todoListDoneButton)
        val replaceButton = itemView.findViewById<ImageView>(R.id.todoListReplaceButton)
    }

    //역할 : filter 를 이용하여 리사이클러뷰에 보여줄 리스트를 조절하는 것.
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if(charSearch.isEmpty()) {
                    todoSearchList = todoList
                } else {
                    val resultList = ArrayList<TodoInstance>()
                    for(row in todoList)
                    {
                        if(row.todo.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    todoSearchList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = todoSearchList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                todoSearchList = results?.values as ArrayList<TodoInstance>
                notifyDataSetChanged()
            }
        }
    }

    private fun saveTodoTitleAndContentData(todoTitleText : String, todoContentText: String) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = pref.edit()

        editor
            .putString("todoTitleText", todoTitleText)
            .putString("todoContentText", todoContentText)
            .apply()
    }

    private fun saveTodoIdData(todoId: String)
    {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = pref.edit()

        editor
            .putString("todoId", todoId)
            .apply()
    }

}