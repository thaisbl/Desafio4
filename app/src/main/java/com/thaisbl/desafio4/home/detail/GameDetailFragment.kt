package com.thaisbl.desafio4.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.thaisbl.desafio4.R
import com.thaisbl.desafio4.databinding.FragmentGameDetailBinding
import com.thaisbl.desafio4.home.Game

class GameDetailFragment : Fragment() {

    private lateinit var binding: FragmentGameDetailBinding
    private lateinit var gameDetailViewModel: GameDetailViewModel
    private val args: GameDetailFragmentArgs by navArgs()

    private fun setupGameDetailsComponents(game: Game) {
        binding.fabEditGame.setOnClickListener {
            val action =
                GameDetailFragmentDirections.actionGameDetailFragmentToEditGameFragment(game)
            findNavController().navigate(action)
        }

        binding.tvGameTitle.text = game.title
        binding.tvGameTitleAppbar.text = game.title

        Glide.with(this)
            .load(game.mImageStoragePath)
            .centerCrop()
            .into(binding.ivGameCover)

        binding.tvGameDescription.text = game.description
        binding.tvGameReleaseYear.text = getString(R.string.fmt_release_year, game.releaseYear)

    }

    private fun setUpObservables() {
        gameDetailViewModel.onGameDataResultSuccess.observe(viewLifecycleOwner, {
            setupGameDetailsComponents(it)
        })
        gameDetailViewModel.onGameDataResultFailure.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
        })
        binding.ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        gameDetailViewModel = ViewModelProvider(this).get(GameDetailViewModel::class.java)

        args.gameUid.let {
            gameDetailViewModel.getGameData(it)
        }


        setUpObservables()
    }
}