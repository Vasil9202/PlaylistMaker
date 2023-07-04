package com.example.playlistmaker.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.DARK_THEME
import com.example.playlistmaker.R
import com.example.playlistmaker.THEME_MODE


class SettingsActivity : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var shareButton: TextView
    private lateinit var writeSupportButton: TextView
    private lateinit var userAgreementButton: TextView
    private lateinit var themeSwitcher: SwitchCompat


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        themeSwitcher = findViewById<SwitchCompat>(R.id.themeSwitcher)
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
        val sharedPrefs = getSharedPreferences(THEME_MODE, MODE_PRIVATE)
        if(sharedPrefs.getBoolean(THEME_MODE, DARK_THEME)){
            themeSwitcher.setChecked(true)
        }


        backButton = findViewById<Button>(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }

        shareButton = findViewById<TextView>(R.id.share_app)
        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.android_course))
                intent.putExtra(
                    Intent.EXTRA_TEXT,getString(R.string.android_dev_page))
                startActivity(Intent.createChooser(intent, getString(R.string.send_link_via)))

        }

        writeSupportButton = findViewById<TextView>(R.id.write_support)
        writeSupportButton.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.sup_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sup_email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.sup_email_def_message))
                startActivity(this)
            }

        }

        userAgreementButton = findViewById<TextView>(R.id.user_agreement)
        userAgreementButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.offer_page)))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }



    }
}