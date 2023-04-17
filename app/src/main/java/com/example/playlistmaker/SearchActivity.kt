package com.example.playlistmaker

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchEditText: EditText
    private lateinit var backButton: Button
    private lateinit var searchIcon: Drawable
    private lateinit var clearIcon: Drawable
    private lateinit var findNothingImg: ImageView
    private lateinit var findNothingText: TextView
    private lateinit var netErrorImg: ImageView
    private lateinit var netErrorText: TextView
    private lateinit var updateButton: ImageButton
    private val iTunes = "https://itunes.apple.com"
    private val tracks = ArrayList<Track>()




    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunes)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(TrackApiService::class.java)



    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables",
        "NotifyDataSetChanged", "MissingInflatedId"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        backButton = findViewById<Button>(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }

        searchEditText = findViewById<EditText>(R.id.search_edit_text)

        findNothingImg = findViewById<ImageView>(R.id.find_nothing_img)

        findNothingText = findViewById<TextView>(R.id.find_nothing_text)

        netErrorImg = findViewById<ImageView>(R.id.net_error_img)

        netErrorText = findViewById<TextView>(R.id.net_error_text)

        updateButton = findViewById<ImageButton>(R.id.update_bt)

        searchIcon = resources.getDrawable(R.drawable.search, null)

        clearIcon = resources.getDrawable(R.drawable.clear, null)

        findNothingImg.visibility = View.GONE

        findNothingText.visibility = View.GONE

        if (searchEditText.text.isNullOrEmpty()) {
            searchEditText.setCompoundDrawablesWithIntrinsicBounds(searchIcon, null, null, null)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(
                        searchIcon,
                        null,
                        null,
                        null
                    )
                } else {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(
                        searchIcon,
                        null,
                        clearIcon,
                        null
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })

        updateButton.setOnClickListener {
            search()
        }

        searchEditText.setOnTouchListener { view, event ->
            val drawable =
                searchEditText.compoundDrawables[0]
            if (!searchEditText.text.isNullOrEmpty() && event.x >= searchEditText.width - searchEditText.paddingRight - drawable.intrinsicWidth) {
                searchEditText.setText("")
                return@setOnTouchListener true
            }
            false
        }
        if (savedInstanceState != null) {
            val text = savedInstanceState.getString(SEARCH_TEXT)
            searchEditText.setText(text)
        }

        recyclerView  = findViewById<RecyclerView>(R.id.trackList)
        trackAdapter = TrackAdapter(tracks)
        recyclerView.adapter = trackAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val text = savedInstanceState.getString(SEARCH_TEXT)
        searchEditText.setText(text)
    }

    fun search(){
        findNothingImg.visibility = View.GONE
        findNothingText.visibility = View.GONE
        netErrorImg.visibility = View.GONE
        netErrorText.visibility = View.GONE
        updateButton.visibility = View.GONE
        iTunesService.search(searchEditText.text.toString()).enqueue(object : Callback<TrackResponse>{
            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                if (response.code() == 200) {
                    tracks.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        tracks.addAll(response.body()?.results!!)
                        trackAdapter.updateData(tracks)
                    }
                    else{
                        tracks.clear()
                        trackAdapter.updateData(tracks)
                        findNothingImg.visibility = View.VISIBLE
                        findNothingText.visibility = View.VISIBLE
                    }
                }
                else {
                    tracks.clear()
                    trackAdapter.updateData(tracks)
                    netErrorImg.visibility = View.VISIBLE
                    netErrorText.visibility = View.VISIBLE
                    updateButton.visibility = View.VISIBLE

                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                tracks.clear()
                trackAdapter.updateData(tracks)
                netErrorImg.visibility = View.VISIBLE
                netErrorText.visibility = View.VISIBLE
                updateButton.visibility = View.VISIBLE}
        })
    }
}