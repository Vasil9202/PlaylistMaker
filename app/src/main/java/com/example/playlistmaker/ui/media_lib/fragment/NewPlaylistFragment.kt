package com.example.playlistmaker.ui.media_lib.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import com.example.playlistmaker.R
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


class NewPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentNewPlaylistBinding
    private val viewModel by activityViewModel<PlaylistViewModel>()
    private lateinit var label: String
    private var fragment: Fragment? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var communicator: FragmentCommunicator? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        fragment = requireActivity().supportFragmentManager.findFragmentByTag("NewPlaylistFrag")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createButton.isEnabled = false
        label = ""
        setOnClickListeners()

        binding.editName.doAfterTextChanged {
            binding.createButton.isEnabled = binding.editName.text.toString().isNotBlank()
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

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView?.visibility = View.VISIBLE


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentCommunicator) {
            communicator = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        communicator?.fragmentDetached()
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, uri.path?.substringAfterLast("/") ?: "labels.jpg")
        label = file.absolutePath
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.isFinish) // Заголовок диалога
            .setMessage(R.string.allDataLose) // Описание диалога
            .setNegativeButton(R.string.cancel) { dialog, which ->
            }
            .setPositiveButton(R.string.finish) { dialog, which -> // Добавляет кнопку «Да»
                if (fragment != null) {
                    requireActivity().supportFragmentManager.beginTransaction().remove(fragment!!)
                        .commit()
                } else {
                    findNavController().navigateUp()
                }
            }
            .show()
    }

    private fun setOnClickListeners() {
        binding.buttonBack.setOnClickListener {
            if (binding.editDescription.text.toString()
                    .isNotEmpty() || binding.editName.text.toString().isNotEmpty()
                || binding.addLabel.drawable != null
            ) {
                showDialog()
            } else {
                if (fragment != null) {
                    requireActivity().supportFragmentManager.beginTransaction().remove(fragment!!)
                        .commit()
                } else {
                    findNavController().navigateUp()
                }
            }
        }

        binding.createButton.setOnClickListener {
            val newPlaylist = Playlist(
                name = binding.editName.text.toString(),
                description = binding.editDescription.text.toString(),
                placeholderPath = label
            )
            viewModel.savePlaylist(newPlaylist)
            viewModel.getAllPlaylists()
            Toast.makeText(
                requireContext(),
                "Плейлист ${binding.editName.text} создан",
                Toast.LENGTH_LONG
            ).show()

            if (fragment != null) {
                requireActivity().supportFragmentManager.beginTransaction().remove(fragment!!)
                    .commit()
            } else {
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.editDescription.text.toString()
                    .isNotEmpty() || binding.editName.text.toString().isNotEmpty()
                || binding.addLabel.drawable != null
            ) {
                showDialog()
            } else if (fragment != null) {
                requireActivity().supportFragmentManager.beginTransaction().remove(fragment!!)
                    .commit()
            } else {
                findNavController().navigateUp()
            }
        }
    }
}

public interface FragmentCommunicator {
    public fun fragmentDetached();
}