package com.example.playlistmaker.ui.settings.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isDarkModeEnable().observe(this) { isEnable ->
            binding.themeSwitcher.isChecked = isEnable
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.themeChange(checked)
        }


        binding.buttonBack.setOnClickListener {
            finish()
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