package com.example.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.api.TrackApiService
import com.example.playlistmaker.data.search.TrackResponse
import com.example.playlistmaker.data.search.TrackPreferences
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.ui.PlayerActivity
import com.example.playlistmaker.ui.search.ItemClickListener
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.ui.search.TrackHistoryAdapter
import com.example.playlistmaker.ui.search.TracksState
import com.example.playlistmaker.ui.search.view_model.TracksSearchViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SEARCH_HISTORY = "search_history"

const val TRACK = "Track"

class SearchActivity : ComponentActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var searchEditText: EditText
    private lateinit var searchIcon: Drawable
    private lateinit var clearIcon: Drawable
    private lateinit var viewModel: TracksSearchViewModel
    private lateinit var textWatcher: TextWatcher
    private lateinit var share: SharedPreferences
    private lateinit var sharedPreferences: TrackPreferences
    private lateinit var binding: ActivitySearchBinding
    private val historyList: ArrayList<Track> = ArrayList()
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val adapter = TrackAdapter(
        object : ItemClickListener {
            override fun onTrackClick(track: Track) {
                if (clickDebounce()) {
                    historyList.clear()
                    historyList.addAll(sharedPreferences.read(share).toList())
                    historyList.remove(track)
                    historyList.add(0, track)
                    if (historyList.size > 10) {
                        historyList.removeAt(10)
                    }
                    sharedPreferences.write(share, historyList.toTypedArray())
                    val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
                    intent.putExtra(TRACK, track)
                    startActivity(intent)
                }
            }
        }
    )

    private val historyAdapter = TrackAdapter(
        object : ItemClickListener {
            override fun onTrackClick(track: Track) {
                if (clickDebounce()) {
                    historyList.clear()
                    historyList.addAll(sharedPreferences.read(share).toList())
                    historyList.remove(track)
                    historyList.add(0, track)
                    if (historyList.size > 10) {
                        historyList.removeAt(10)
                    }
                    share.edit().clear().apply()
                    sharedPreferences.write(share, historyList.toTypedArray())
                    val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
                    intent.putExtra(TRACK, track)
                    startActivity(intent)
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this, TracksSearchViewModel.getViewModelFactory())[TracksSearchViewModel::class.java]
        share = getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
        sharedPreferences = TrackPreferences()
        historyList.clear()
        historyList.addAll(sharedPreferences.read(share).toList())
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOnLickListeners()


        searchEditText = findViewById<EditText>(R.id.search_edit_text)
        searchIcon = resources.getDrawable(R.drawable.search, null)
        clearIcon = resources.getDrawable(R.drawable.clear, null)

        binding.trackHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.trackHistoryRecyclerView.adapter = historyAdapter
        binding.trackRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.trackRecyclerView.adapter = adapter





        if (searchEditText.text.isNullOrEmpty()) {
            searchEditText.setCompoundDrawablesWithIntrinsicBounds(searchIcon, null, null, null)
        }

        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchEditText.text.isEmpty() && historyList.isNotEmpty()) {
                setHistoryRecyclerView()
            } else {
                setRecyclerView()
            }
        }
//
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (searchEditText.hasFocus() && s?.isEmpty() == true && historyList.isNotEmpty()) {
                        setHistoryRecyclerView()
                } else {
                    viewModel.searchDebounce(
                        changedText = s?.toString() ?: ""
                    )
                    binding.historyText.visibility = View.GONE
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
            }
        }

        textWatcher.let { searchEditText.addTextChangedListener(it) }



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
                viewModel.searchDebounce()
            }
            false
        }
//
        viewModel.observeState().observe(this) {
            render(it)
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


    fun setHistoryRecyclerView() {
        binding.trackRecyclerView.visibility = View.GONE
        binding.trackHistoryRecyclerView.visibility = View.VISIBLE
        binding.historyText.visibility = View.VISIBLE
        historyList.clear()
        historyList.addAll(sharedPreferences.read(share).toList())
        historyAdapter.tracks.clear()
        historyAdapter.tracks.addAll(historyList)
        historyAdapter.notifyDataSetChanged()
        //val historyList: List<Track> = sharedPreferences.read(share).toList()
        //historyTrackAdapter.updateData(historyList)
        val marginTop = resources.getDimensionPixelSize(R.dimen.DP85)
        val clearHistoryLayoutParams = binding.historyClearButton.layoutParams as ViewGroup.MarginLayoutParams
        val lastItemPosition = binding.trackHistoryRecyclerView.adapter?.itemCount?.minus(1) ?: 0
        if (lastItemPosition >= 0) {
            binding.historyClearButton.visibility = View.VISIBLE
            binding.trackHistoryRecyclerView.post {                                                                 //Changing clearButton position
                val lastItemView = binding.trackHistoryRecyclerView.layoutManager?.findViewByPosition(lastItemPosition)
                val layoutParams = binding.historyClearButton.layoutParams as FrameLayout.LayoutParams
                if(lastItemView != null){
                    layoutParams.gravity = Gravity.CENTER or Gravity.TOP
                    clearHistoryLayoutParams.topMargin = lastItemView.bottom + marginTop
                    binding.historyClearButton.layoutParams = layoutParams
                }else{
                    layoutParams.gravity = Gravity.CENTER or Gravity.BOTTOM
                    binding.historyClearButton.layoutParams = layoutParams
                }
            }
        }
    }



    fun setRecyclerView() {
        //trackAdapter.tracks.clear()
        //trackAdapter.notifyDataSetChanged()
        //tracks.clear()
        //trackAdapter.updateData(tracks)
        binding.historyClearButton.visibility = View.GONE
        binding.trackHistoryRecyclerView.visibility = View.GONE

        //binding.trackRecyclerView.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (searchEditText.hasFocus()) {
            searchEditText.clearFocus()
        }
        else {
            super.onBackPressed()
        }
    }

    /*
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

     */

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Content -> showContent(state.movies)
            is TracksState.Empty -> showEmpty(state.message)
            is TracksState.Error -> showError(state.errorMessage)
            is TracksState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.findNothingImg.visibility = View.GONE
        binding.findNothingText.visibility = View.GONE
        binding.netErrorImg.visibility = View.GONE
        binding.netErrorText.visibility = View.GONE
        binding.updateBt.visibility = View.GONE
        //binding.trackRecyclerView.visibility = View.GONE
        binding.trackHistoryRecyclerView.visibility = View.GONE
        binding.historyText.visibility = View.GONE
    }

    private fun showError(errorMessage: String) {
        binding.progressBar.visibility = View.GONE
        binding.netErrorImg.visibility = View.VISIBLE
        binding.netErrorText.visibility = View.VISIBLE
        binding.updateBt.visibility = View.VISIBLE
    }

    private fun showEmpty(emptyMessage: String) {
        binding.progressBar.visibility = View.GONE
        binding.findNothingImg.visibility = View.VISIBLE
        binding.findNothingText.visibility = View.VISIBLE
    }

    private fun showContent(contentTracks: List<Track>) {
        binding.progressBar.visibility = View.GONE
        adapter.tracks.clear()
        adapter.tracks.addAll(contentTracks)
        adapter.notifyDataSetChanged()
        binding.trackRecyclerView.visibility = View.VISIBLE
        //trackAdapter.updateData(tracks)
    }

    private fun setupOnLickListeners(){
        binding.updateBt.setOnClickListener {viewModel.searchDebounce()}
        binding.historyClearButton.setOnClickListener {
            share.edit().clear().apply()
            binding.historyClearButton.visibility = View.GONE
            binding.trackHistoryRecyclerView.visibility = View.GONE
            binding.historyText.visibility = View.GONE
        }
        binding.buttonBack.visibility = View.VISIBLE
        binding.buttonBack.setOnClickListener {
            finish()
        }

    }
}