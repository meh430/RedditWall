package com.mehul.redditwall.fragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.leinardi.android.speeddial.SpeedDialView
import com.mehul.redditwall.AppUtils
import com.mehul.redditwall.R
import com.mehul.redditwall.activities.WallActivity
import com.mehul.redditwall.databinding.FragmentHistoryBinding
import com.mehul.redditwall.objects.HistoryItem
import com.mehul.redditwall.objects.RecyclerListener
import com.mehul.redditwall.objects.WallImage
import com.mehul.redditwall.rv_adapters.HistAdapter
import com.mehul.redditwall.viewmodels.HistViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistViewModel
    private lateinit var historyAdapter: HistAdapter
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("RESET", "history init")
        historyViewModel = ViewModelProvider(this).get(HistViewModel::class.java)
        historyAdapter = HistAdapter()

        binding.historyScroll.adapter = historyAdapter
        binding.historyScroll.addOnItemTouchListener(RecyclerListener(requireContext(),
                binding.historyScroll, object : RecyclerListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val selectedImage = historyAdapter.getHistory(position)
                AppUtils.startWallActivity(requireContext(), WallImage(imgUrl = selectedImage.imgUrl, subName = selectedImage.subName,
                        previewUrl = selectedImage.previewUrl, postLink = selectedImage.postLink))
            }

            override fun onLongItemClick(view: View?, position: Int) {}
        }))

        historyViewModel.allHist.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.historyScroll.visibility = View.GONE
                binding.emptyInfo.visibility = View.VISIBLE
            } else {
                binding.historyScroll.visibility = View.VISIBLE
                binding.emptyInfo.visibility = View.GONE
                historyAdapter.updateHistories(it as ArrayList<HistoryItem>)
            }
        })

        val speedView = binding.speedDial
        speedView.inflate(R.menu.fab_menu)
        speedView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            val context = requireContext()
            when (actionItem.id) {
                R.id.delete_all -> {
                    val confirmDelete =
                            MaterialAlertDialogBuilder(context, R.style.MyThemeOverlayAlertDialog).apply {
                                setTitle("Are You Sure?")
                                setMessage("Do you want to clear ${historyAdapter.itemCount} history items?")
                                setPositiveButton("Yes") { _, _ ->
                                    historyViewModel.deleteAll()
                                    Toast.makeText(context, "Deleted history", Toast.LENGTH_SHORT).show()
                                }
                                setNegativeButton("No") { _, _ ->
                                    Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                                }
                            }
                    confirmDelete.show()
                    return@OnActionSelectedListener false
                }
                R.id.down_all -> {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity as Activity,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WallActivity.WRITE)
                    } else {
                        if (historyAdapter.itemCount == 0) {
                            Toast.makeText(context, "No items", Toast.LENGTH_SHORT).show()
                            return@OnActionSelectedListener false
                        }

                        uiScope.launch {
                            AppUtils.downloadAllImages(context, historyAdapter.histories.map { it.imgUrl } as ArrayList<String>)
                        }
                    }

                    return@OnActionSelectedListener false // false will close it without animation
                }
                R.id.random -> {
                    if (historyAdapter.itemCount == 0) {
                        Toast.makeText(context, "No items", Toast.LENGTH_SHORT).show()
                        return@OnActionSelectedListener false
                    }

                    var randomIndex = (0..historyAdapter.itemCount).random()
                    while (randomIndex >= historyAdapter.itemCount || randomIndex < 0) {
                        randomIndex = (0..historyAdapter.itemCount).random()
                    }

                    val selectedImage = historyAdapter.getHistory(randomIndex)
                    AppUtils.startWallActivity(requireContext(), WallImage(imgUrl = selectedImage.imgUrl, subName = selectedImage.subName,
                            previewUrl = selectedImage.previewUrl, postLink = selectedImage.postLink))
                    return@OnActionSelectedListener false
                }
            }

            false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

