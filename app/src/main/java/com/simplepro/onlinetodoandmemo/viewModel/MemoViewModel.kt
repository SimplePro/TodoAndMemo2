package com.simplepro.onlinetodoandmemo.viewModel

import androidx.databinding.ObservableField
import com.simplepro.onlinetodoandmemo.instance.MemoInstance

class MemoViewModel(memo : MemoInstance) {
    val title = ObservableField<String>(memo.memoTitle)
    val content = ObservableField<String>(memo.memoContent)
    val calendar = ObservableField<String>(memo.memoCalendar)
    val plan = ObservableField<String>(memo.memoPlan)
}