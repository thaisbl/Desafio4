package com.thaisbl.desafio4.home.edit

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.thaisbl.desafio4.R
import com.thaisbl.desafio4.databinding.FragmentEditGameBinding
import com.thaisbl.desafio4.home.Game
import com.thaisbl.desafio4.shared.utils.isReadStoragePermissionAlreadyGranted

class EditGameFragment : Fragment() {

    private lateinit var binding: FragmentEditGameBinding
    private lateinit var editGameViewModel: EditGameViewModel

    private val args: EditGameFragmentArgs by navArgs()

    private var gameImageBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        editGameViewModel = ViewModelProvider(this).get(EditGameViewModel::class.java)

        args.game?.let {
            loadGameInformation(it)
        }

        initComponents()
        setUpObservables()
    }


    private fun callIntentOpenCameraActivity() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            startActivityForResult(it, REQUEST_CAPTURE_IMAGE)
        }
    }

    private fun callIntentChooseImageFromGallery() {
        context?.let { context ->
            if (isReadStoragePermissionAlreadyGranted(context)) {
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
                    startActivityForResult(it, REQUEST_CHOOSE_IMAGE)
                }
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_EXTERNAL_STORAGE
                )
            }
        }
    }

    private fun openChooseImageDialog() {
        val options = arrayOf(
            getString(R.string.action_take_photo),
            getString(R.string.action_choose_gallery),
        )

        context?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.title_select_image)
                .setItems(options) { _, item ->
                    when (options[item]) {
                        getString(R.string.action_take_photo) -> {
                            callIntentOpenCameraActivity()
                        }
                        getString(R.string.action_choose_gallery) -> {
                            callIntentChooseImageFromGallery()
                        }
                    }
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun setUpObservables() {
        editGameViewModel.onStoreGameResultSuccess.observe(viewLifecycleOwner, {
            args.game?.let {
                Toast.makeText(context, "Game edited successfully!", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(context, "Game saved successfully!", Toast.LENGTH_LONG).show()
            }

            findNavController().popBackStack()
        })
        editGameViewModel.onStoreGameResultFailure.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
        })
    }

    private fun initComponents() {
        args.game?.let {
            binding.btnSaveGame.text = getString(R.string.edit_game)
        } ?: run {
            binding.btnSaveGame.text = getString(R.string.save_game)
        }

        binding.ivGameImage.setOnClickListener {
            openChooseImageDialog()
        }

        binding.btnSaveGame.setOnClickListener {
            val name = binding.tieName.text.toString()
            val releaseYear = binding.tieReleaseYear.text.toString()
            val description = binding.tieDescription.text.toString()

            args.game?.let {
                editGameViewModel.editGame(it.uid, name, releaseYear, description, gameImageBitmap)
            } ?: run {
                editGameViewModel.saveGame(name, releaseYear, description, gameImageBitmap)
            }
        }
    }

    private fun loadGameInformation(game: Game) {
        binding.tieName.setText(game.title)
        binding.tieReleaseYear.setText(game.releaseYear.toString())
        binding.tieDescription.setText(game.description)

        Glide.with(this)
            .load(game.mImageStoragePath)
            .centerCrop()
            .into(binding.ivGameImage)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_EXTERNAL_STORAGE -> {
                val isStoragePermissionGranted = grantResults.isNotEmpty()
                        && grantResults.first() == PackageManager.PERMISSION_GRANTED

                if (isStoragePermissionGranted) {
                    callIntentChooseImageFromGallery()
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CAPTURE_IMAGE -> {
                    data?.extras?.get("data").let { imageBitmap ->
                        gameImageBitmap = imageBitmap as Bitmap
                        binding.ivGameImage.setImageBitmap(gameImageBitmap)
                    }
                }
                REQUEST_CHOOSE_IMAGE -> {
                    data?.data?.let { imageUri ->
                        binding.ivGameImage.setImageURI(imageUri)
                        gameImageBitmap = binding.ivGameImage.drawable.toBitmap()
                    }
                }
            }
        }
    }

    companion object {
        const val PERMISSION_EXTERNAL_STORAGE = 512
        const val REQUEST_CAPTURE_IMAGE = 1024
        const val REQUEST_CHOOSE_IMAGE = 2048
    }

}