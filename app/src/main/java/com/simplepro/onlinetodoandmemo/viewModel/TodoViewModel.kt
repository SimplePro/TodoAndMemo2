package com.simplepro.onlinetodoandmemo.viewModel

import androidx.databinding.ObservableField
import com.simplepro.onlinetodoandmemo.instance.TodoInstance

class TodoViewModel(todo: TodoInstance) {
    val todo = ObservableField<String>(todo.todo)
}