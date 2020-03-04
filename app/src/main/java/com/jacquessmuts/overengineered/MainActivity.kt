package com.jacquessmuts.overengineered

import android.os.Bundle
import com.jacquessmuts.overengineered.ui.BaseActivity
import com.jacquessmuts.overengineered.ui.main.MainFragment
import com.jacquessmuts.overengineered.ui.main.MainState
import com.jacquessmuts.overengineered.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : BaseActivity<MainState, MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commitNow()
        }
    }

    override val viewModel by viewModel<MainViewModel>()

    override fun onStateUpdated(nuState: MainState) {
    }
}
