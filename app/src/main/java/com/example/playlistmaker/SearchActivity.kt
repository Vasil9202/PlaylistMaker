package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.data.TrackPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SEARCH_HISTORY = "search_history"

const val TRACK = "Track"

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L


    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyTrackAdapter: TrackHistoryAdapter
    private lateinit var searchEditText: EditText
    private lateinit var backButton: Button
    private lateinit var searchIcon: Drawable
    private lateinit var clearIcon: Drawable
    private lateinit var findNothingImg: ImageView
    private lateinit var findNothingText: TextView
    private lateinit var netErrorImg: ImageView
    private lateinit var netErrorText: TextView
    private lateinit var updateButton: ImageButton
    private lateinit var clearHistoryButton: ImageButton
    private lateinit var historyText: TextView
    private lateinit var progressBar: ProgressBar

    private val iTunes = "https://itunes.apple.com"
    private val tracks = ArrayList<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { if(!searchEditText.text.toString().isNullOrEmpty())search()}
    private var isClickAllowed = true




    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunes)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(TrackApiService::class.java)


    @SuppressLint(
        "ClickableViewAccessibility", "UseCompatLoadingForDrawables",
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

        clearHistoryButton = findViewById(R.id.search_history_clear)

        historyText = findViewById(R.id.search_history)

        recyclerView = findViewById<RecyclerView>(R.id.trackList)

        historyRecyclerView = findViewById<RecyclerView>(R.id.historyTrackList)

        progressBar = findViewById(R.id.progressBar)


        historyTrackAdapter = TrackHistoryAdapter(emptyList())
        historyRecyclerView.adapter = historyTrackAdapter
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyTrackAdapter.onItemClick = {track ->
            if(clickDebounce()){
            val share = getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
            val sharedPreferences: TrackPreferences = TrackPreferences()
            val historyTrackList: ArrayList<Track> = ArrayList(sharedPreferences.read(share).toList())
            historyTrackList.remove(track)
            historyTrackList.add(0, track)
            if (historyTrackList.size > 10) {
                historyTrackList.removeAt(10)
            }
            sharedPreferences.write(share, historyTrackList.toTypedArray())
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK,track)
            startActivity(intent)
        }}

        trackAdapter = TrackAdapter(emptyList())
        recyclerView.adapter = trackAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter.onItemClick = {track ->
            if(clickDebounce()){
            val share = getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
            val sharedPreferences: TrackPreferences = TrackPreferences()
            val historyTrackList: ArrayList<Track> = ArrayList(sharedPreferences.read(share).toList())
            historyTrackList.remove(track)
            historyTrackList.add(0, track)
            if (historyTrackList.size > 10) {
                historyTrackList.removeAt(10)
            }
            sharedPreferences.write(share, historyTrackList.toTypedArray())
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("Track",track)
            startActivity(intent)
        }}

        clearHistoryButton.setOnClickListener {
            clearHistoryButton.visibility = View.GONE
            historyRecyclerView.visibility = View.GONE
            historyText.visibility = View.GONE
            val share = getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
            share.edit().clear().apply()
        }


        if (searchEditText.text.isNullOrEmpty()) {
            searchEditText.setCompoundDrawablesWithIntrinsicBounds(searchIcon, null, null, null)
        }

        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchEditText.text.isEmpty()) {
                historyText.visibility = View.VISIBLE
                setHistoryRecyclerView()
            } else {
                historyText.visibility = View.GONE
                setRecyclerView()
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (searchEditText.hasFocus() && s?.isEmpty() == true) {
                    val share = getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)                         //Get list from sharedPreference
                    val sharedPreferences: TrackPreferences = TrackPreferences()
                    val historyList: List<Track> = sharedPreferences.read(share).toList()
                    if(historyList.isNotEmpty()){
                    historyText.visibility = View.VISIBLE
                    setHistoryRecyclerView()}
                } else {
                    searchDebounce()
                    historyText.visibility = View.GONE
                    setRecyclerView()
                }

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

    fun search() {
        progressBar.visibility = View.VISIBLE
        findNothingImg.visibility = View.GONE
        findNothingText.visibility = View.GONE
        netErrorImg.visibility = View.GONE
        netErrorText.visibility = View.GONE
        updateButton.visibility = View.GONE
        iTunesService.search(searchEditText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            trackAdapter.updateData(tracks)
                        } else {
                            tracks.clear()
                            trackAdapter.updateData(tracks)
                            findNothingImg.visibility = View.VISIBLE
                            findNothingText.visibility = View.VISIBLE
                        }
                    } else {
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
                    updateButton.visibility = View.VISIBLE
                }
            })
    }



    fun setHistoryRecyclerView() {
        val share = getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)                         //Get list from sharedPreference
        val sharedPreferences: TrackPreferences = TrackPreferences()
        val historyList: List<Track> = sharedPreferences.read(share).toList()
        historyTrackAdapter.updateData(historyList)
        if(historyList.isEmpty())
            historyText.visibility = View.GONE
        val marginTop = resources.getDimensionPixelSize(R.dimen.DP85)
        val clearHistoryLayoutParams = clearHistoryButton.layoutParams as ViewGroup.MarginLayoutParams
        val lastItemPosition = historyRecyclerView.adapter?.itemCount?.minus(1) ?: 0
        if (lastItemPosition >= 0) {                                                            //Check find track list
            clearHistoryButton.visibility = View.VISIBLE
            historyRecyclerView.post {                                                                 //Changing clearButton position
                val lastItemView = historyRecyclerView.layoutManager?.findViewByPosition(lastItemPosition)
                val layoutParams = clearHistoryButton.layoutParams as FrameLayout.LayoutParams
                if(lastItemView != null){
                    layoutParams.gravity = Gravity.CENTER or Gravity.TOP
                    clearHistoryLayoutParams.topMargin = lastItemView.bottom + marginTop
                    clearHistoryButton.layoutParams = layoutParams
                }else{
                    layoutParams.gravity = Gravity.CENTER or Gravity.BOTTOM
                    clearHistoryButton.layoutParams = layoutParams
                }
            }
        }
        recyclerView.visibility = View.GONE
        historyRecyclerView.visibility = View.VISIBLE
    }

    fun setRecyclerView() {
        tracks.clear()
        trackAdapter.updateData(tracks)
        clearHistoryButton.visibility = View.GONE
        historyRecyclerView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (searchEditText.hasFocus()) {
            searchEditText.clearFocus()
        }
        else {
            super.onBackPressed()
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


}