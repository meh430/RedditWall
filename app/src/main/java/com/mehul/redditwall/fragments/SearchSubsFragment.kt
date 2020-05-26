package com.mehul.redditwall.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mehul.redditwall.AppUtils
import com.mehul.redditwall.R
import com.mehul.redditwall.adapters.SubAdapter
import com.mehul.redditwall.databinding.FragmentSearchSubsBinding
import com.mehul.redditwall.objects.Subreddit
import com.mehul.redditwall.viewmodels.SubViewModel
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject

class SearchSubsFragment : Fragment() {
    private var _binding: FragmentSearchSubsBinding? = null
    private val binding get() = _binding!!

    private val noResMessage = "No subreddits found"
    private val subsList = ArrayList<Subreddit>()
    private lateinit var subAdapter: SubAdapter
    private lateinit var subViewModel: SubViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchSubsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subViewModel = ViewModelProvider(this).get(SubViewModel::class.java)

        binding.searchButton.setOnClickListener {
            findSubreddits(view)
        }
        subAdapter = SubAdapter(getCon(), subViewModel)
        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                findSubreddits(view)
                return@OnEditorActionListener true
            }
            false
        })
        val bottomBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        binding.subsScroll.layoutManager = LinearLayoutManager(getCon())
        binding.subsScroll.adapter = subAdapter
        binding.subsScroll.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 50) {
                    bottomBar?.visibility = View.GONE

                } else {
                    if (dy != 0 && dy < -50) {
                        bottomBar?.visibility = View.VISIBLE
                    }
                }
            }
        })
        subAdapter.setSubs(subsList)
    }

    private fun findSubreddits(v: View) {
        subsList.clear()
        binding.subLoading.visibility = View.VISIBLE
        binding.noResults.visibility = View.GONE
        val inputManager = getCon().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(v.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
        CoroutineScope(Dispatchers.Main).launch {
            getSearchResults(binding.search.text.toString())
        }
    }

    private suspend fun getSearchResults(query: String) {
        val temp = ArrayList<Subreddit>()
        withContext(Dispatchers.Default) {
            val endpoint = "https://www.reddit.com/api/search_reddit_names/.json?query=$query"
            val jsonString = async { AppUtils.getJsonData(endpoint) }
            try {
                val results = JSONObject(jsonString.await())
                val resultsList = results.getJSONArray("names")
                for (i in 0..resultsList.length()) {
                    val curr = resultsList.getString(i)
                    val json = async { AppUtils.getSubInfo(curr) }
                    val result = json.await().getJSONObject("data")
                    val iconUrl = result.getString("icon_img")
                    val title = result.getString("display_name_prefixed")
                    val desc = result.getString("public_description")
                    val subs = result.getInt("subscribers")
                    val sub = Subreddit((0..999999999).random(), title, desc, subs, iconUrl, null)
                    temp.add(sub)
                    Log.e("LOADED", "loaded $title")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        subsList.addAll(temp)
        if (temp.isEmpty()) {
            binding.noResults.text = noResMessage
            binding.noResults.visibility = View.VISIBLE
        } else {
            subAdapter.notifyDataSetChanged()
        }
        binding.subLoading.visibility = View.GONE
        binding.subsScroll.visibility = View.VISIBLE
        subAdapter.setSubs(subsList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCon(): Context {
        return activity as Context
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchSubsFragment()
    }
}
