package com.example.playlistmaker

import android.content.Intent
import android.content.IntentSender.OnFinished
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }

        val shareButton = findViewById<TextView>(R.id.share_app)
        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Курс Android-разработчик от Яндекс Практикума")
                intent.putExtra(
                    Intent.EXTRA_TEXT,"https://practicum.yandex.ru/profile/android-developer/")
                startActivity(Intent.createChooser(intent, "Отправить ссылку через"))

        }

        val writeSupportButton = findViewById<TextView>(R.id.write_support)
        writeSupportButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("vasil9202@yandex.ru"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
            startActivity(shareIntent)
        }

        val userAgreementButton = findViewById<TextView>(R.id.user_agreement)
        userAgreementButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }



    }
}