package com.jacquessmuts.overengineered

import android.app.Application
import com.jacquessmuts.overengineered.api.DeckApi
import com.jacquessmuts.overengineered.db.DeckDb
import com.jacquessmuts.overengineered.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber
import timber.log.Timber.DebugTree


@ExperimentalCoroutinesApi
@FlowPreview
class OEApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        //TODO: else Timber.plant(CrashReportingTree())

        startKoin {
            androidContext(this@OEApplication)
            modules(module {
                single { DeckApi() }
                single { CardsRepository(get(), get()) }
                single { DeckDb(this@OEApplication) }
                viewModel { MainViewModel(get()) }

            })
        }

    }

}
