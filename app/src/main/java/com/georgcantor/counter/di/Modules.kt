package com.georgcantor.counter.di

import com.georgcantor.counter.model.DaysDatabase
import com.georgcantor.counter.viewmodel.StatisticsViewModel
import com.georgcantor.counter.viewmodel.TimerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        TimerViewModel(get())
    }
    viewModel {
        StatisticsViewModel(get())
    }
}

val appModule = module {
    single { DaysDatabase.buildDefault(get()).dao() }
}