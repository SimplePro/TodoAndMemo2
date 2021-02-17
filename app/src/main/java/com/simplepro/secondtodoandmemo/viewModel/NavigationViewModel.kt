package com.simplepro.secondtodoandmemo.viewModel

import androidx.databinding.ObservableField
import com.simplepro.secondtodoandmemo.instance.UserInstance

class NavigationViewModel(user: UserInstance) {
    val userEmail = ObservableField<String>(user.email)
    val userId = ObservableField<String>(user.id)
}
