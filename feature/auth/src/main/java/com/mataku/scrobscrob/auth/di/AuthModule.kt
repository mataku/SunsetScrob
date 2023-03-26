package com.mataku.scrobscrob.auth.di

import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.data.repository.di.repositoryModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
  includes(repositoryModule)
  viewModel {
    LoginViewModel(
      get()
    )
  }
}
