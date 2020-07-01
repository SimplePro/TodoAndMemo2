package com.simplepro.secondtodoandmemo.viewModel

import androidx.databinding.ObservableField
import com.simplepro.secondtodoandmemo.instance.TodoInstance

class TodoViewModel(todo: TodoInstance) {
    val todo = ObservableField<String>(todo.todo)
}