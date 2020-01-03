package com.georgcantor.counter.di

import com.georgcantor.counter.model.DaysDatabase
import com.georgcantor.counter.util.PreferenceManager
import com.georgcantor.counter.viewmodel.*
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
    viewModel {
        EditViewModel(get())
    }
}

val appModule = module {
    single { DaysDatabase.buildDefault(get()).dao() }
}