package com.example.secondtodoandmemo.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.secondtodoandmemo.instance.PageInstance
import com.example.secondtodoandmemo.R
import com.example.secondtodoandmemo.activity.MainActivity
import kotlinx.android.synthetic.main.view_pager_item.view.*


class IntroduceDeveloperPagerRecyclerViewAdapter(private val pageList: ArrayList<PageInstance>, private val clickListener : setOnClickLottieLayout) :
    RecyclerView.Adapter<IntroduceDeveloperPagerRecyclerViewAdapter.CustomViewHolder>() {

    interface setOnClickLottieLayout {
        fun setOnClickLottie(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(LayoutInflater.from(parent.context).inflate(
                                R.layout.view_pager_item, parent, false))
    }

    override fun getItemCount(): Int {
        return pageList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindWithView(pageList[position])
    }

    inner class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private val itemBg: ConstraintLayout = itemView.pager_item_bg
        private val itemImage: ImageView = itemView.pager_item_image
        private val itemTitle: TextView = itemView.pager_item_title
        private val itemContent: TextView = itemView.pager_item_text
        private val itemLottieLayout: ConstraintLayout = itemView.onClickGoToMainActivityLayout
        private val itemLottieAnimation: LottieAnimationView = itemView.onClickGotoMainActivityLottieAnimationView

        fun bindWithView(pageItem: PageInstance) {
            itemBg.setBackgroundResource(pageItem.bgColor)
            itemImage.setImageResource(pageItem.imageSrc)
            itemTitle.text = pageItem.title
            itemContent.text = pageItem.content

            if (pageItem.bgColor == R.color.colorViewPagerThree) {
                itemLottieLayout.visibility = View.VISIBLE
            }

            itemLottieAnimation.setOnClickListener {
                clickListener.setOnClickLottie(it, position)
            }
        }
    }
}
