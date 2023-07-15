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


const val TRACK = "Track"

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var searchEditText: EditText
    private lateinit var viewModel: TracksSearchViewModel
    private lateinit var textWatcher: TextWatcher
    private lateinit var binding: ActivitySearchBinding
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val adapter = TrackAdapter(
        object : ItemClickListener {
            override fun onTrackClick(track: Track) {
                if (clickDebounce()) {
                    viewModel.addTrackToHistory(track)
                    val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
                    intent.putExtra(TRACK, track)
                    startActivity(intent)
                }
            }
        }
    )

    private val historyAdapter = TrackHistoryAdapter(
        object : ItemClickListener {
            override fun onTrackClick(track: Track) {
                if (clickDebounce()) {
                    viewModel.addTrackToHistory(track)
                    val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
                    intent.putExtra(TRACK, track)
                    startActivity(intent)
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, TracksSearchViewModel.getViewModelFactory())[TracksSearchViewModel::class.java]
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.historyVisibility.observe(this) { isVisible ->
            binding.historyClearButton.visibility = if (isVisible) View.VISIBLE else View.GONE
            binding.trackHistoryRecyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
            binding.historyText.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        setupOnLickListeners()
        searchEditText = binding.searchEditText
        binding.trackHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.trackHistoryRecyclerView.adapter = historyAdapter
        binding.trackRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.trackRecyclerView.adapter = adapter
        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchEditText.text.isEmpty()) {
                setHistoryRecyclerView()
            }
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (searchEditText.hasFocus() && s?.isEmpty() == true) {
                        setHistoryRecyclerView()
                } else {
                    viewModel.searchDebounce(changedText = s?.toString() ?: "")
                }

                if (s.isNullOrEmpty()) {
                    binding.clearSearchImage.visibility = View.GONE
                } else {
                    binding.clearSearchImage.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        textWatcher.let { searchEditText.addTextChangedListener(it) }

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
        if(viewModel.getSearchHistoryStorageList().toList().isNotEmpty()){
            binding.trackHistoryRecyclerView.visibility = View.VISIBLE
            binding.historyText.visibility = View.VISIBLE
        historyAdapter.tracks.clear()
        historyAdapter.tracks.addAll(viewModel.getSearchHistoryStorageList().toList())
        historyAdapter.notifyDataSetChanged()
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
    }


    override fun onBackPressed() {
        if (searchEditText.hasFocus()) {
            searchEditText.clearFocus()
        }
        else {
            super.onBackPressed()
        }
    }

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
        binding.historyClearButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.findNothingImg.visibility = View.GONE
        binding.findNothingText.visibility = View.GONE
        binding.netErrorImg.visibility = View.GONE
        binding.netErrorText.visibility = View.GONE
        binding.updateBt.visibility = View.GONE
        binding.trackRecyclerView.visibility = View.GONE
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
    }

    private fun setupOnLickListeners(){
        binding.updateBt.setOnClickListener {viewModel.searchDebounce()}
        binding.historyClearButton.setOnClickListener {viewModel.historyClearClick()}
        binding.buttonBack.setOnClickListener {finish()}
        binding.clearSearchImage.setOnClickListener { searchEditText.setText("")
        setHistoryRecyclerView()}
    }
}