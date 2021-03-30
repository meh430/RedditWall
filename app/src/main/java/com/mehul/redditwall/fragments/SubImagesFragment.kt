package com.mehul.redditwall.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.mehul.redditwall.AppUtils
import com.mehul.redditwall.R
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.databinding.FragmentSubImagesBinding
import com.mehul.redditwall.objects.RecyclerListener
import com.mehul.redditwall.rest.RedditApi
import com.mehul.redditwall.rv_adapters.WallImageAdapter
import com.mehul.redditwall.viewmodels.WallImageViewModel
import kotlinx.coroutines.InternalCoroutinesApi

class SubImagesFragment : Fragment() {

    private val args: SubImagesFragmentArgs by navArgs()

    private var _binding: FragmentSubImagesBinding? = null
    private val binding get() = _binding!!

    lateinit var subImagesViewModel: WallImageViewModel
    private lateinit var subImagesAdapter: WallImageAdapter
    private var isLoading = false

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
        when (item.itemId) {
            R.id.hot -> {
                subImagesViewModel.setQAndSort(subImagesViewModel.query, RedditApi.HOT)
            }

            R.id.new_sort -> {
                subImagesViewModel.setQAndSort(subImagesViewModel.query, RedditApi.NEW)
            }

            R.id.top_week -> {
                subImagesViewModel.setQAndSort(subImagesViewModel.query, RedditApi.TOP_WEEK)
            }

            R.id.top_year -> {
                subImagesViewModel.setQAndSort(subImagesViewModel.query, RedditApi.TOP_YEAR)
            }

            R.id.top_all -> {
                subImagesViewModel.setQAndSort(subImagesViewModel.query, RedditApi.TOP_ALL)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subImagesViewModel = ViewModelProvider(this).get(WallImageViewModel::class.java)
        subImagesViewModel.query = args.subName
        Log.e("ARG", args.subName)

        (activity as AppCompatActivity).supportActionBar?.title = args.subName
        val prefs = AppUtils.getPreferences(requireContext())
        val dimensions = AppUtils.getDimensions(requireContext())
        val lowRes = prefs.getBoolean(SettingsActivity.PREVIEW_RES, false)
        subImagesAdapter = WallImageAdapter(dimensions, lowRes)
        binding.imageScroll.adapter = subImagesAdapter

        if (!AppUtils.networkAvailable(requireContext())) {
            binding.imageScroll.visibility = View.GONE
            binding.errorInfo.visibility = View.VISIBLE
            binding.errorInfo.text = "No Network :("
        } else {
            subImagesViewModel.setQAndSort(subImagesViewModel.query, RedditApi.HOT)
        }

        subImagesViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            isLoading = it
        })

        subImagesViewModel.wallImages.observe(viewLifecycleOwner, Observer {
            if (it.size == 0 && isLoading) {
                showView(LOADING)
            } else if (it.size == 0 && !isLoading) {
                showView(ERROR_INFO)
            } else {
                showView(IMAGE_SCROLL)
                subImagesAdapter.updateWallImages(it)
            }
        })

        binding.imageScroll.addOnItemTouchListener(RecyclerListener(requireContext(),
                binding.imageScroll, object : RecyclerListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val selectedImage = subImagesAdapter.getWallImage(position)
                AppUtils.startWallActivity(requireContext(), selectedImage)
            }

            override fun onLongItemClick(view: View?, position: Int) {}
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

                if (!recyclerView.canScrollVertically(1) && isLoading) {
                    binding.bottomLoad.visibility = View.VISIBLE
                } else if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    binding.bottomLoad.visibility = View.VISIBLE
                    subImagesViewModel.getNextImages()
                } else {
                    binding.bottomLoad.visibility = View.INVISIBLE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        subImagesViewModel.clear()
    }

    private fun showView(view: Int) {
        if (view == IMAGE_SCROLL) {
            binding.imageScroll.visibility = View.VISIBLE
        } else {
            binding.imageScroll.visibility = View.GONE
        }

        if (view == LOADING) {
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.loading.visibility = View.GONE
        }

        if (view == BOTTOM_LOADING) {
            binding.bottomLoad.visibility = View.VISIBLE
        } else {
            binding.bottomLoad.visibility = View.GONE
        }

        if (view == ERROR_INFO) {
            binding.errorInfo.visibility = View.VISIBLE
        } else {
            binding.errorInfo.visibility = View.GONE
        }
    }

    companion object {
        const val IMAGE_SCROLL = 0
        const val LOADING = 1
        const val BOTTOM_LOADING = 2
        const val ERROR_INFO = 3
        const val SUB_SCROLL = 4
    }
}