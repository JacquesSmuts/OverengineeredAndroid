package com.jacquessmuts.overengineered.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jacquessmuts.overengineered.R
import com.jacquessmuts.overengineered.model.Card
import kotlinx.android.synthetic.main.viewholder_card.view.*

class CardAdapter : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    var cards: List<Card> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class CardViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_card, parent, false) as CardView
        return CardViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        val imageView = holder.cardView.imageCard
        Glide.with(imageView)
            .load(card.image)
            .placeholder(imageView.drawable)
            .fallback(R.drawable.card_back)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = cards.size
}
