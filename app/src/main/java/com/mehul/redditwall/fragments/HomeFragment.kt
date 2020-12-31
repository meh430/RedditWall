package com.mehul.redditwall.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mehul.redditwall.AppUtils
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.databinding.FragmentHomeBinding
import com.mehul.redditwall.objects.RecyclerListener
import com.mehul.redditwall.rest.RedditApi
import com.mehul.redditwall.rv_adapters.WallImageAdapter
import com.mehul.redditwall.viewmodels.WallImageViewModel
import kotlinx.coroutines.InternalCoroutinesApi

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var wallImageViewModel: WallImageViewModel
    private lateinit var wallImageAdapter: WallImageAdapter
    private var isLoading = false

    @InternalCoroutinesApi
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        wallImageViewModel = ViewModelProvider(this).get(WallImageViewModel::class.java)

        val prefs = AppUtils.getPreferences(requireContext())
        val defaultSub = prefs.getString(SettingsActivity.DEFAULT, "mobilewallpaper").toString()
        val dimensions = AppUtils.getDimensions(requireContext())
        val lowRes = prefs.getBoolean(SettingsActivity.PREVIEW_RES, false)
        wallImageAdapter = WallImageAdapter(dimensions, lowRes)
        binding.imageScroll.adapter = wallImageAdapter

        if (!AppUtils.networkAvailable(requireContext())) {
            binding.imageScroll.visibility = View.GONE
            binding.errorInfo.visibility = View.VISIBLE
            binding.errorInfo.text = "No Network :("
        } else {
            wallImageViewModel.setQAndSort(defaultSub, RedditApi.HOT)
        }

        wallImageViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            isLoading = it
        })

        wallImageViewModel.wallImages.observe(viewLifecycleOwner, Observer {
            if (it.size == 0 && isLoading) {
                showView(LOADING)
            } else if (it.size == 0 && !isLoading) {
                showView(ERROR_INFO)
            } else {
                showView(IMAGE_SCROLL)
                wallImageAdapter.updateWallImages(it)
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
                    wallImageViewModel.getNextImages()
                } else {
                    binding.bottomLoad.visibility = View.INVISIBLE
                }
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

