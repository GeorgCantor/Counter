package com.georgcantor.counter.di

import com.georgcantor.counter.viewmodel.TimerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        TimerViewModel()
    }
}
//
//val appModule = module {
//    single { Database.buildDefault(get()).dao() }
//}