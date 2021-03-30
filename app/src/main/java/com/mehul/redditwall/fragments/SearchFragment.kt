package com.mehul.redditwall.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mehul.redditwall.AppUtils
import com.mehul.redditwall.R
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.databinding.FragmentSearchBinding
import com.mehul.redditwall.objects.RecyclerListener
import com.mehul.redditwall.rest.RedditApi
import com.mehul.redditwall.rv_adapters.SubAdapter
import com.mehul.redditwall.rv_adapters.WallImageAdapter
import com.mehul.redditwall.viewmodels.SubViewModel
import com.mehul.redditwall.viewmodels.WallImageViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.InternalCoroutinesApi

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var subLoading = false
    private var imageLoading = false
    private var viewingImages = false
    private var searched = false

    private lateinit var prefs: SharedPreferences
    private lateinit var subViewModel: SubViewModel
    private lateinit var wallImageViewModel: WallImageViewModel

    private lateinit var subAdapter: SubAdapter
    private lateinit var wallImageAdapter: WallImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.reddit_sort, menu)
    }

    @InternalCoroutinesApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!viewingImages) {
            return super.onOptionsItemSelected(item)
        }

        when (item.itemId) {
            R.id.hot -> {
                wallImageViewModel.setQAndSort(wallImageViewModel.query, RedditApi.HOT)
            }

            R.id.new_sort -> {
                wallImageViewModel.setQAndSort(wallImageViewModel.query, RedditApi.NEW)
            }

            R.id.top_week -> {
                wallImageViewModel.setQAndSort(wallImageViewModel.query, RedditApi.TOP_WEEK)
            }

            R.id.top_year -> {
                wallImageViewModel.setQAndSort(wallImageViewModel.query, RedditApi.TOP_YEAR)
            }

            R.id.top_all -> {
                wallImageViewModel.setQAndSort(wallImageViewModel.query, RedditApi.TOP_ALL)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @InternalCoroutinesApi
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("RESET", "search init")
        prefs = AppUtils.getPreferences(requireContext())

        subViewModel = ViewModelProvider(this).get(SubViewModel::class.java)
        wallImageViewModel = ViewModelProvider(this).get(WallImageViewModel::class.java)

        val prefs = AppUtils.getPreferences(requireContext())
        val lowRes = prefs.getBoolean(SettingsActivity.PREVIEW_RES, false)
        val dimensions = AppUtils.getDimensions(requireContext())

        subAdapter = SubAdapter()
        wallImageAdapter = WallImageAdapter(dimensions, lowRes)

        binding.subsScroll.adapter = subAdapter
        binding.imageScroll.adapter = wallImageAdapter

        if (!searched) {
            binding.errorInfo.text = "Search for subs"
            Log.e("SUB", "SEARCHED")
            showView(HomeFragment.ERROR_INFO)
        }

        subViewModel.subreddits.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty() && subLoading && !viewingImages) {
                showView(HomeFragment.LOADING)
            } else if (it.isEmpty() && !subLoading && !viewingImages && searched) {
                binding.errorInfo.text = "Unable to find subs"
                showView(HomeFragment.ERROR_INFO)
            } else if (!viewingImages && searched) {
                showView(HomeFragment.SUB_SCROLL)
                subAdapter.updateSubreddits(it)
            }
        })

        wallImageViewModel.wallImages.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty() && imageLoading && viewingImages) {
                showView(HomeFragment.LOADING)
            } else if (it.isEmpty() && !imageLoading && viewingImages) {
                binding.errorInfo.text = "Unable to find images"
                showView(HomeFragment.ERROR_INFO)
            } else if (viewingImages) {
                showView(HomeFragment.IMAGE_SCROLL)
                wallImageAdapter.updateWallImages(it)
            }
        })

        subViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            subLoading = it
        })

        wallImageViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            imageLoading = it
        })

        binding.subsScroll.addOnItemTouchListener(RecyclerListener(requireContext(),
                binding.subsScroll, object : RecyclerListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                viewingImages = true
                wallImageViewModel.setQAndSort(subAdapter.subreddits[position].subName.substring(2), RedditApi.HOT)
            }

            override fun onLongItemClick(view: View?, position: Int) {
                prefs.edit().putString(SettingsActivity.DEFAULT, subAdapter.subreddits[position].subName.substring(2)).apply()
                Toast.makeText(requireContext(), "Set as default", Toast.LENGTH_SHORT).show()
            }
        }))

        binding.imageScroll.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    binding.bottomLoad.visibility = View.INVISIBLE
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && imageLoading) {
                    binding.bottomLoad.visibility = View.VISIBLE
                } else if (!recyclerView.canScrollVertically(1) && !imageLoading) {
                    binding.bottomLoad.visibility = View.VISIBLE
                    wallImageViewModel.getNextImages()
                } else {
                    binding.bottomLoad.visibility = View.INVISIBLE
                }
            }
        })

        binding.imageScroll.addOnItemTouchListener(RecyclerListener(requireContext(),
                binding.imageScroll, object : RecyclerListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val selectedImage = wallImageAdapter.getWallImage(position)
                AppUtils.startWallActivity(requireContext(), selectedImage)
            }

            override fun onLongItemClick(view: View?, position: Int) {}
        }))

        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val inputManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(binding.root.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS)
                searched = true
                viewingImages = false
                subViewModel.searchSubs(binding.search.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun showView(view: Int) {
        if (view == HomeFragment.IMAGE_SCROLL) {
            binding.imageScroll.visibility = View.VISIBLE
        } else {
            binding.imageScroll.visibility = View.GONE
        }

        if (view == HomeFragment.SUB_SCROLL) {
            binding.subsScroll.visibility = View.VISIBLE
        } else {
            binding.subsScroll.visibility = View.GONE
        }

        if (view == HomeFragment.LOADING) {
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.loading.visibility = View.GONE
        }

        if (view == HomeFragment.BOTTOM_LOADING) {
            binding.bottomLoad.visibility = View.VISIBLE
        } else {
            binding.bottomLoad.visibility = View.GONE
        }

        if (view == HomeFragment.ERROR_INFO) {
            binding.errorInfo.visibility = View.VISIBLE
        } else {
            binding.errorInfo.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

