package com.thaisbl.desafio4.home.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thaisbl.desafio4.R
import com.thaisbl.desafio4.databinding.ItemGameBinding
import com.thaisbl.desafio4.home.Game

class HomeGamesListAdapter(
    private val gamesList: MutableList<Game>,
    private val onGameClicked: (Game) -> Unit
) : RecyclerView.Adapter<HomeGamesListAdapter.HomeGamesListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGamesListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGameBinding.inflate(layoutInflater, parent, false)
        return HomeGamesListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeGamesListViewHolder, position: Int) {
        holder.bind(gamesList[position], onGameClicked)
    }

    override fun getItemCount(): Int {
        return gamesList.size
    }

    class HomeGamesListViewHolder(
        private val binding: ItemGameBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, onGameClicked: (Game) -> Unit): Unit = with(binding) {
            tvGameTitle.text = game.title
            tvGameReleaseYear.text = game.releaseYear.toString()

            Glide.with(itemView.context)
                .load(game.mImageStoragePath)
                .centerCrop()
                .error(R.drawable.layer_ic_broken)
                .into(ivGameImage)

            itemView.setOnClickListener {
                onGameClicked(game)
            }
        }

    }


}