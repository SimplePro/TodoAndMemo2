package com.example.secondtodoandmemo.adapter

import android.content.Context
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.secondtodoandmemo.Instance.TodoForm
import com.example.secondtodoandmemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//새로운 다른 itemLayout을 만들었다. (MemoRecyclerView 안에서 Dialog 쪽에서 사용됨. Dialog 안에 있는 RecyclerView의 아이디는 memoPlanRecyclerViewDialog이다.)
class MemoTodoRecyclerViewAdapter(val todoList: ArrayList<TodoForm>, val context: Context, private val DoneTodoListListener: memoItemViewOnClickListener)
    : RecyclerView.Adapter<MemoTodoRecyclerViewAdapter.CustomViewHolder>() {

    //itemView 가 클릭 되었을 때 호출되는 콜백 함수.
    interface memoItemViewOnClickListener {
        fun memoItemViewOnClick(view: View, position: Int)
    }

    //역할 : recyclerView 가 생성되었을 때 실행하는 것.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.memo_todo_list_item, parent, false)
        return CustomViewHolder(
            view
        ).apply {
            //itemView 가 클릭 되었을 때
            itemView.setOnClickListener {
                //쉐어드로 planText 의 값을 보내주어 메모에 반영함.
                saveData(todoList.get(adapterPosition).todo)
                //해당 콜백 함수를 호출함.
                DoneTodoListListener.memoItemViewOnClick(it, adapterPosition)
            }
            //DoneTodoList 의 remove 버튼이 클릭 되었을 때
            DoneTodoListRemoveButton.setOnClickListener {
                val doneTodoId = todoList[adapterPosition].todoId
                //해당 position 의 값을 삭제함.
                todoList.removeAt(adapterPosition)
                if(FirebaseAuth.getInstance().currentUser != null)
                {
                    val doneTodoDocRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                    doneTodoDocRef.collection("DoneTodo").document(doneTodoId).delete().addOnCompleteListener {
                        Toast.makeText(parent.context.applicationContext, "데이터가 삭제되었습니다.", Toast.LENGTH_LONG).show()
                    }
                }
                //notify 로 recyclerView 에 반영함.
                notifyItemRemoved(adapterPosition)
                notifyItemChanged(adapterPosition, todoList.size)
            }
        }
    }

    //역할 : recyclerView 에 들어갈 아이템의 개수를 반환하는 것.
    override fun getItemCount(): Int {
        return todoList.size
    }

    //역할 : 해당 position 의 item 의 데이터를 할당하는 것.
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.todoTitleText.text = todoList.get(position).todo
    }

    //역할 : 변수에 findViewById 를 사용하여 값을 대입하는 것.
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoTitleText = itemView.findViewById<TextView>(R.id.memoTodoListTextView)
        val DoneTodoListRemoveButton = itemView.findViewById<ImageView>(R.id.memoTodoListRemoveButton)
    }

    //쉐어드를 이용하여 planText 를 저장하고, memo 에 반영하기 위한 함수.
    private fun saveData(memoPlanText: String){
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = pref.edit()

        editor.putString("memoPlanText", memoPlanText)
            .apply()

    }


}