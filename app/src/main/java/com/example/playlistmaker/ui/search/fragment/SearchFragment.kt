package com.example.playlistmaker.ui.search.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.ItemClickListener
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.ui.search.TrackHistoryAdapter
import com.example.playlistmaker.ui.search.TracksState
import com.example.playlistmaker.ui.search.view_model.TracksSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


const val TRACK = "Track"

class SearchFragment : Fragment() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var searchEditText: EditText
    private val viewModel by viewModel<TracksSearchViewModel>()
    private lateinit var binding: FragmentSearchBinding
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val adapter = TrackAdapter(
        object : ItemClickListener {
            override fun onTrackClick(track: Track) {
                if (clickDebounce()) {
                    viewModel.addTrackToHistory(track)
                    val intent = Intent(requireContext(), PlayerActivity::class.java)
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
                    val intent = Intent(requireContext(), PlayerActivity::class.java)
                    intent.putExtra(TRACK, track)
                    startActivity(intent)
                }
            }
        }
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.historyVisibility.observe(viewLifecycleOwner) { isVisible ->
            binding.historyClearButton.isVisible = isVisible
            binding.trackHistoryRecyclerView.isVisible = isVisible
            binding.historyText.isVisible = isVisible
        }

        setupOnLickListeners()
        searchEditText = binding.searchEditText
        binding.trackHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.trackHistoryRecyclerView.adapter = historyAdapter
        binding.trackRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.trackRecyclerView.adapter = adapter
        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchEditText.text.isEmpty()) {
                setHistoryRecyclerView()
            }
        }


        searchEditText.doAfterTextChanged {
            if (searchEditText.hasFocus() && searchEditText.text.toString()?.isEmpty() == true) {
            setHistoryRecyclerView()
                binding.clearSearchImage.visibility = View.GONE

            } else {
            viewModel.searchDebounce(searchEditText.text.toString())
                binding.clearSearchImage.visibility = View.VISIBLE
            }
        }


        if (savedInstanceState != null) {
            val text = savedInstanceState.getString(SEARCH_TEXT)
            searchEditText.setText(text)
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(searchEditText.text.toString())
            }
            false
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchEditText.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val text = savedInstanceState?.getString(SEARCH_TEXT)
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

/*
    override fun onBackPressed() {
        if (searchEditText.hasFocus()) {
            searchEditText.clearFocus()
        }
        else {
            super.onBackPressed()
        }
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
        binding.updateBt.setOnClickListener {viewModel.searchDebounce(searchEditText.text.toString())}
        binding.historyClearButton.setOnClickListener {viewModel.historyClearClick()}
        binding.clearSearchImage.setOnClickListener { searchEditText.setText("")
        setHistoryRecyclerView()}
    }
}