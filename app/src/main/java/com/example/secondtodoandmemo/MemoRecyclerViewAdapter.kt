package com.example.secondtodoandmemo

import android.content.Context
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

//todoList까지 받는 이유 : todoList를 Dialog 안에 있는 RecyclerView의 아이템으로 쓰기 위해서이다. 나중에 서버쪽을 작업하게 됬을 때 todoList를 다른 ArrayList형 변수로 바꿔줘야 한다.
class MemoRecyclerViewAdapter (private var memoList: ArrayList<MemoForm>,
                               var memoSearchList: ArrayList<MemoForm>,
                               private var clickListener : memoItemClickListener): RecyclerView.Adapter<MemoRecyclerViewAdapter.CustomViewHolder>(), Filterable{

//    var memoSearchList : ArrayList<MemoForm>

    //역할 : 처음에 memoSearchList 에 memoList 의 값을 넣어줘서 리사이클러뷰에 표시하는 것.
    init {
        memoSearchList = memoList
        notifyItemChanged(itemCount)
    }

    lateinit var context : Context


    //메모의 Remove 버튼이 클릭되었을 때 호출되는 콜백 함수
    interface memoItemClickListener {
        fun memoOnItemClick(view: View, position: Int)

        fun memoItemReplaceClick(view: View, position: Int)
    }

    //역할 : recyclerView 가 생성되었을 때 실행하는 것.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.memo_list_item, parent, false)
        return CustomViewHolder(view).apply {

                //memoItem 의 Replace 버튼이 클릭 되었을 때
            memoReplaceButton.setOnClickListener {
                saveMemoTitleAndContentData(memoSearchList[adapterPosition].memoTitle, memoSearchList[adapterPosition].memoContent)
                saveMemoIdData(memoSearchList[adapterPosition].memoId)
                //Replace 콜백 함수를 호출한다.
                clickListener.memoItemReplaceClick(it, adapterPosition)
            }

            //memoItem 의 Remove 버튼이 클릭 되었을 때
            memoRemoveButton.setOnClickListener {
                saveMemoIdData(memoSearchList[adapterPosition].memoId)
                //해당 position 의 값을 삭제한다.
//                memoList.removeAt(adapterPosition)
//                memoSearchList = memoList
                //notify 로 recyclerView 에 반영한다.
//                notifyItemRemoved(adapterPosition)
//                notifyItemChanged(adapterPosition, memoList.size)
                //Remove 콜백 함수를 호출한다.
                clickListener.memoOnItemClick(it, adapterPosition)
            }
            }
        }

    //역할 : recyclerView 에 들어갈 item 의 개수를 반환하는 것.
    override fun getItemCount(): Int {
        return memoSearchList.size
    }


    //역할 : recyclerView 에 데이터를 할당하는 것.
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.memoTitleText.text = memoSearchList.get(position).memoTitle
        holder.memoContentText.text = memoSearchList.get(position).memoContent
        holder.memoCalendarText.text = memoSearchList.get(position).memoCalendar
        memoPlanText(holder, position)
    }

    //역할 : 변수에 findViewById 를 하여 대입하는 것.
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memoTitleText = itemView.findViewById<TextView>(R.id.memoListTitleTextView)
        val memoContentText = itemView.findViewById<TextView>(R.id.memoListContentTextView)
        val memoCalendarText = itemView.findViewById<TextView>(R.id.memoListCalendarTextView)
        val memoPlanText = itemView.findViewById<TextView>(R.id.memoListPlanTextView)
        val memoReplaceButton = itemView.findViewById<ImageView>(R.id.memoListReplaceButton)
        val memoRemoveButton = itemView.findViewById<ImageView>(R.id.memoListRemoveButton)
    }

    //역할 : memoPlanText 의 text 형식을 정해주는 것.
    private fun memoPlanText (holder: CustomViewHolder, position : Int) {
        if(memoSearchList.get(position).memoPlan == "") {
            holder.memoPlanText.text = ""
        }
        else if(memoSearchList.get(position).memoPlan != "") {
            holder.memoPlanText.text = "(${memoSearchList.get(position).memoPlan} 후)"
        }
    }

    //역할 : filter 를 이용하여 리사이클러뷰에 보여줄 리스트를 조절하는 것.
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if(charSearch.isEmpty()) {
                    memoSearchList = memoList
                } else {
                    val resultList = ArrayList<MemoForm>()
                    for(row in memoList)
                    {
                        if(row.memoContent.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))
                            || row.memoTitle.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))
                            || row.memoPlan.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    memoSearchList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = memoSearchList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                memoSearchList = results?.values as ArrayList<MemoForm>
                notifyDataSetChanged()
            }
        }
    }

    private fun saveMemoTitleAndContentData(memoTitleText : String, memoContentText: String) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = pref.edit()

        editor
            .putString("memoTitleText", memoTitleText)
            .putString("memoContentText", memoContentText)
            .apply()
    }

    private fun saveMemoIdData(memoId: String){
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = pref.edit()

        editor
            .putString("memoId", memoId)
            .apply()
    }
}