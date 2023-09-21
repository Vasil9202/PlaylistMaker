package com.example.playlistmaker.ui.media_lib.fragment

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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class NewPlaylistFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentNewPlaylistBinding
    private lateinit var bottomNavigationView: BottomNavigationView
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
        bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createButton.isEnabled = false
        binding.buttonBack.setOnClickListener{
            if(binding.editDescription.text.toString().isNotEmpty() || binding.editName.text.toString().isNotEmpty()
                || binding.addLabel.drawable != null){
                showDialog()
            }
            else{
                findNavController().navigateUp()
            }
        }
        binding.createButton.setOnClickListener{
            Toast.makeText(requireContext(),"Плейлист ${binding.editName.text} создан",Toast.LENGTH_LONG).show()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if(binding.editDescription.text.toString().isNotEmpty() || binding.editName.text.toString().isNotEmpty()
                || binding.addLabel.drawable != null){
                showDialog()
            }
        }


        binding.editDescription.doAfterTextChanged {
            if(binding.editDescription.text.isNotEmpty()){
                binding.editDescription.setBackgroundResource(R.drawable.editted_rectangle)
                binding.textDescription.visibility = View.VISIBLE
            }else{
                binding.editDescription.setBackgroundResource(R.drawable.edittext_rectangle)
                binding.textDescription.visibility = View.GONE
            }
        }

        binding.editName.doAfterTextChanged {
            binding.createButton.isEnabled = binding.editName.text.toString().isNotEmpty()
            if(binding.editName.text.isNotEmpty()){
                binding.editName.setBackgroundResource(R.drawable.editted_rectangle)
                binding.textName.visibility = View.VISIBLE
            }else{
                binding.editName.setBackgroundResource(R.drawable.edittext_rectangle)
                binding.textName.visibility = View.GONE
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
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
        bottomNavigationView.visibility = View.VISIBLE


    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "first_cover.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun showDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?") // Заголовок диалога
            .setMessage("Все несохраненные данные будут потеряны") // Описание диалога
            .setNegativeButton("Отмена") { dialog, which ->
            }
            .setPositiveButton("Завершить") { dialog, which -> // Добавляет кнопку «Да»
                findNavController().navigateUp()
            }
            .show()
    }

}