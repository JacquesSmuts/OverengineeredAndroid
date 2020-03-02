package com.jacquessmuts.overengineered.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.jacquessmuts.overengineered.R
import com.jacquessmuts.overengineered.ui.BaseFragment
import com.jacquessmuts.overengineered.ui.adapter.CardAdapter
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.sharedViewModel
import reactivecircus.flowbinding.android.view.clicks

@FlowPreview
@ExperimentalCoroutinesApi
class MainFragment : BaseFragment<MainState, MainViewModel>() {

    override val viewModel by sharedViewModel<MainViewModel>()
    private var cardAdapter = CardAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
        setListeners()
    }

    private fun setupRecyclerView() {

        recyclerView.adapter = cardAdapter
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        layoutManager.spanCount = 3
    }

    override fun onStateUpdated(nuState: MainState) {
        message.setText(getString(R.string.cards_remaining, nuState.cardsRemaining))

        if (nuState.shouldShowCards) {
            mainLayout.transitionToEnd()
        } else {
            mainLayout.transitionToStart()
        }

        cardAdapter.cards = nuState.cards
    }

    private fun setListeners() {
        button.clicks()
            .onEach { viewModel.buttonClicked() }
            .launchIn(this)

        imageDeck.clicks()
            .onEach { viewModel.deckClicked() }
            .launchIn(this)

        mainLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            }

        })
    }
}
