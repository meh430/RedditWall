package com.mehul.redditwall.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mehul.redditwall.R
import com.mehul.redditwall.activities.SubActivity
import com.mehul.redditwall.adapters.SubAdapter
import com.mehul.redditwall.databinding.FragmentSavedSubsBinding
import com.mehul.redditwall.objects.Subreddit
import com.mehul.redditwall.viewmodels.SubViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private var currSort = R.id.recent

private var _binding: FragmentSavedSubsBinding? = null
private val binding get() = _binding!!

private var savedList: List<Subreddit> = ArrayList<Subreddit>()
private lateinit var subViewModel: SubViewModel
private lateinit var subAdapter: SubAdapter

class SavedSubsFragment : Fragment(), SubActivity.Sorting {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSavedSubsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCon(): Context {
        return activity as Context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subViewModel = ViewModelProvider(this).get(SubViewModel::class.java)
        subAdapter = SubAdapter(getCon(), subViewModel)
        binding.savedScroll.adapter = subAdapter
        binding.savedScroll.layoutManager = LinearLayoutManager(getCon())
        val helper = ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                    override fun onMove(recyclerView: RecyclerView,
                                        viewHolder: RecyclerView.ViewHolder,
                                        target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        val saved = subAdapter.getSubAtPosition(position)
                        subViewModel.deleteSubreddit(saved)
                        subAdapter.notifyDataSetChanged()
                    }
                })
        helper.attachToRecyclerView(binding.savedScroll)
        val bottomBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        binding.savedScroll.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        subViewModel.allSubs.observe(getCon() as LifecycleOwner, Observer { subreddits ->
            savedList = sortList(currSort, subreddits)
            subAdapter.setSubs(savedList)
            binding.emptySaved.visibility = if (subAdapter.itemCount == 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }

    private fun sortList(sort: Int, list: List<Subreddit>): List<Subreddit> {
        return when (sort) {
            R.id.recent -> {
                list.sortedWith(compareBy
                { SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).parse(it.internalDate!!) }).asReversed()
            }
            R.id.alpha -> {
                list.sortedWith(compareBy { it.subName })
            }
            else -> {
                list.sortedWith(compareBy
                { SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).parse(it.internalDate!!) })
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = SavedSubsFragment()
    }

    override fun sortOrder(sort: Int) {
        savedList = sortList(sort, savedList)
        currSort = sort
        subAdapter.setSubs(savedList)
    }
}
