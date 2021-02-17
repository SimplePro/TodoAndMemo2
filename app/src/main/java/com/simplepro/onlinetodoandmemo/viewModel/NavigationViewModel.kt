package com.simplepro.onlinetodoandmemo.viewModel

import androidx.databinding.ObservableField
import com.simplepro.onlinetodoandmemo.instance.UserInstance

class NavigationViewModel(user: UserInstance) {
    val userEmail = ObservableField<String>(user.email)
    val userId = ObservableField<String>(user.id)
}
