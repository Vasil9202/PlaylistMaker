package com.example.playlistmaker.ui.media_lib.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentEditPlaylistBinding
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.media_lib.view_model.PlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.File
import java.io.FileOutputStream


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class EditPlaylistFragment : Fragment() {
    private lateinit var playlist: Playlist
    private val args: ActivePlaylistFragmentArgs by navArgs()
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentEditPlaylistBinding
    private val viewModel by activityViewModel<PlaylistViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = args.playlistArg ?: Playlist(name = "")
        setFields()
        setOnClickListeners()
        binding.editDescription.doAfterTextChanged {text ->
            if (text != null) {
                if (text.isNotEmpty()) {
                    binding.editDescription.setBackgroundResource(R.drawable.editted_rectangle)
                } else {
                    binding.editDescription.setBackgroundResource(R.drawable.edittext_rectangle)
                }
            }
        }

        binding.editName.doAfterTextChanged {text ->
            binding.saveButton.isEnabled = binding.editName.text.toString().isNotEmpty()
            if (text != null) {
                if (text.isNotEmpty()) {
                    binding.editName.setBackgroundResource(R.drawable.editted_rectangle)
                } else {
                    binding.editName.setBackgroundResource(R.drawable.edittext_rectangle)
                }
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.addLabel.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        //по нажатию на кнопку pickImage запускаем photo picker
        binding.addLabel.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, uri.path?.substringAfterLast("/") ?: "labels.jpg")
        playlist.placeholderPath = file.absolutePath
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun setOnClickListeners() {
        binding.buttonBack.setOnClickListener {
                    findNavController().navigateUp()
        }

        binding.saveButton.setOnClickListener {
            playlist.name = binding.editName.text.toString()
            playlist.description = binding.editDescription.text.toString()
            viewModel.editPlaylist(playlist)
                findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                findNavController().navigateUp()
        }
    }
    private fun setFields(){
        binding.addLabel.setImageURI(Uri.parse(playlist.placeholderPath))
        binding.editName.setText(playlist.name)
        binding.editDescription.setText(playlist.description)
    }
}