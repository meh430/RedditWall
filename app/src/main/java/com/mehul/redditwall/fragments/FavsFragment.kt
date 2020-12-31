package com.mehul.redditwall.fragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
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
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.activities.WallActivity
import com.mehul.redditwall.databinding.FragmentFavsBinding
import com.mehul.redditwall.objects.RecyclerListener
import com.mehul.redditwall.objects.WallImage
import com.mehul.redditwall.rv_adapters.WallImageAdapter
import com.mehul.redditwall.viewmodels.FavViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavsFragment : Fragment() {
    private var _binding: FragmentFavsBinding? = null
    private val binding get() = _binding!!
    private lateinit var favViewModel: FavViewModel
    private lateinit var favImageAdapter: WallImageAdapter
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavsBinding.inflate(inflater, container, false)
        favViewModel = ViewModelProvider(this).get(FavViewModel::class.java)

        val dimensions = AppUtils.getDimensions(requireContext())
        val lowRes = AppUtils.getPreferences(requireContext()).getBoolean(SettingsActivity.PREVIEW_RES, false)
        favImageAdapter = WallImageAdapter(dimensions, lowRes)
        binding.imageScroll.adapter = favImageAdapter

        binding.imageScroll.addOnItemTouchListener(RecyclerListener(requireContext(),
                binding.imageScroll, object : RecyclerListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val selectedImage = favImageAdapter.getWallImage(position)
                AppUtils.startWallActivity(requireContext(), selectedImage)
            }

            override fun onLongItemClick(view: View?, position: Int) {}
        }))

        favViewModel.allFav.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.imageScroll.visibility = View.GONE
                binding.emptyInfo.visibility = View.VISIBLE
            } else {
                binding.imageScroll.visibility = View.VISIBLE
                binding.emptyInfo.visibility = View.GONE
                favImageAdapter.updateWallImages(it as ArrayList<WallImage>)
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
                                setMessage("Do you want to clear ${favImageAdapter.itemCount} favorite images?")
                                setPositiveButton("Yes") { _, _ ->
                                    favViewModel.deleteAll()
                                    Toast.makeText(context, "Deleted favorite images", Toast.LENGTH_SHORT).show()
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
                        if (favImageAdapter.itemCount == 0) {
                            Toast.makeText(context, "No items", Toast.LENGTH_SHORT).show()
                            return@OnActionSelectedListener false
                        }

                        uiScope.launch {
                            AppUtils.downloadAllImages(context, favImageAdapter.wallImages.map { it.imgUrl } as ArrayList<String>)
                        }
                    }

                    return@OnActionSelectedListener false // false will close it without animation
                }
                R.id.random -> {
                    if (favImageAdapter.itemCount == 0) {
                        Toast.makeText(context, "No items", Toast.LENGTH_SHORT).show()
                        return@OnActionSelectedListener false
                    }

                    var randomIndex = (0..favImageAdapter.itemCount).random()
                    while (randomIndex >= favImageAdapter.itemCount || randomIndex < 0) {
                        randomIndex = (0..favImageAdapter.itemCount).random()
                    }

                    val selectedImage = favImageAdapter.getWallImage(randomIndex)
                    AppUtils.startWallActivity(context, selectedImage)
                    return@OnActionSelectedListener false
                }
            }

            false
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

