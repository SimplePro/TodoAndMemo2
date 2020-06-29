package com.simplepro.secondtodoandmemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.simplepro.secondtodoandmemo.instance.PageInstance
import com.simplepro.secondtodoandmemo.R
import com.simplepro.secondtodoandmemo.adapter.IntroduceDeveloperPagerRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_introduce_developer.*

class IntroduceDeveloperActivity : AppCompatActivity(), IntroduceDeveloperPagerRecyclerViewAdapter.setOnClickLottieLayout {

    private var pageItemList = ArrayList<PageInstance>()
    private lateinit var introducePagerRecyclerViewAdapter : IntroduceDeveloperPagerRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduce_developer)


        pageItemList.add(PageInstance(R.color.colorViewPagerOne, R.drawable.question_mark, "1. 개발자는 누구인가?", "저는 2년 전에 C언어라는 언어를 " +
                "\n공부하기 시작했고, 올해 2월에 Kotlin\n언어를 공부하기 시작했습니다. 아이디어를 제 스스로 실현시키고 싶었기에\n프로그래밍이라는 것을 배웠고, \n아이디어를 실현시켜 나가는 중입니다."))
        pageItemList.add(PageInstance(R.color.colorViewPagerTwo, R.drawable.innovation, "2. 이 앱을 만든 이유는?", "많은 사람들이 해야할 일들을 하고 뿌듯한 마음으로 자신이 " +
                "했었던 일을 메모할 수\n있는 앱을 만들고 싶어 만들게 되었습니다! 여러분들이 이 앱으로 할일을\n정리하고, 뿌듯한 마음을 메모로 \n담으셨으면 좋겠습니다!"))
        pageItemList.add(PageInstance(R.color.colorViewPagerThree, R.drawable.future, "3. 다음으로 만들 앱은?", "3번째 프로젝트로 TMI 를 할 수 있는 앱을 만드려고 합니다. " +
                "많은 사람들이 참고\n있었던 TMI 를 모두 내뿜을 수 있는\n앱입니다. 많은 기대 부탁드립니다!\n저의 앱을 다운 받아주신\n여러분들 모두 감사드립니다."))

        introducePagerRecyclerViewAdapter = IntroduceDeveloperPagerRecyclerViewAdapter(pageItemList, this)
        my_intro_view_pager.apply {
            adapter = introducePagerRecyclerViewAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
        dots_indicator.setViewPager2(my_intro_view_pager)
    }

    override fun setOnClickLottie(view: View, position: Int) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}