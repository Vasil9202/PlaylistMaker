package com.example.playlistmaker.ui.settings.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isDarkModeEnable().observe(viewLifecycleOwner) { isEnable ->
            binding.themeSwitcher.isChecked = isEnable
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.themeChange(checked)
        }


        binding.shareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.android_course))
                intent.putExtra(
                    Intent.EXTRA_TEXT,getString(R.string.android_dev_page))
                startActivity(Intent.createChooser(intent, getString(R.string.send_link_via)))
        }

        binding.writeSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.sup_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sup_email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.sup_email_def_message))
                startActivity(this)
            }
        }

        binding.userAgreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.offer_page)))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}