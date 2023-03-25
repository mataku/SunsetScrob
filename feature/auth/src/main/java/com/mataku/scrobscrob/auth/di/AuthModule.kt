package com.mataku.scrobscrob.auth.di

import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
  viewModel {
    LoginViewModel(
      get()
    )
  }
}
