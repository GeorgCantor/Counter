package com.georgcantor.counter.di

import com.georgcantor.counter.model.DaysDatabase
import com.georgcantor.counter.util.PreferenceManager
import com.georgcantor.counter.viewmodel.GraphViewModel
import com.georgcantor.counter.viewmodel.HistoryViewModel
import com.georgcantor.counter.viewmodel.SettingsViewModel
import com.georgcantor.counter.viewmodel.TimerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (manager: PreferenceManager) ->
        TimerViewModel(manager, get())
    }
    viewModel {
        HistoryViewModel(get())
    }
    viewModel {
        GraphViewModel(get())
    }
    viewModel { (manager: PreferenceManager) ->
        SettingsViewModel(manager)
    }
}

val appModule = module {
    single { DaysDatabase.buildDefault(get()).dao() }
}