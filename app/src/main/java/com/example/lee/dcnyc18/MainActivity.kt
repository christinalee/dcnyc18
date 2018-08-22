package com.example.lee.dcnyc18

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.example.lee.dcnyc18.models.Photo
import com.example.lee.dcnyc18.ui.ListIntentHandler
import com.example.lee.dcnyc18.ui.PhotoAdapter
import com.example.lee.dcnyc18.ui.PhotoCellIntentHandler
import com.example.lee.dcnyc18.ui.PhotoListViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

// This demo app:
// fetches trending photos from unsplash
// displays them in a grid
// tapping a cell in the grid will "like" it
// likes are persisted to db


// Remaining items to establish tonight:
// Possibly move models/db/etc to modules

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhotoAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var listIntentHandler: ListIntentHandler
    private lateinit var photoCellIntentHandler: PhotoCellIntentHandler

    private val dataRefreshListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                if (visibleItemCount + 4 >= adapter.itemCount) {
                    listIntentHandler.onReachedEndOfData()
                }
            }
        }
    }

    @Inject
    protected lateinit var viewModelProvider: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViewModel()
        initializeUi()
    }

    private fun initializeViewModel() {
        val viewModel = ViewModelProviders.of(this, viewModelProvider)
                .get(PhotoListViewModel::class.java)
                .apply {
                    listIntentHandler = this
                    photoCellIntentHandler = this
                }
        viewModel.getPhotosListData().observe(this, Observer(::updateAdapterData))
    }

    private fun initializeUi() {
        adapter = PhotoAdapter(photoCellIntentHandler)

        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        recyclerView = findViewById<RecyclerView>(R.id.activity_main_rv).apply {
            layoutManager = this@MainActivity.layoutManager
            adapter = this@MainActivity.adapter
            addOnScrollListener(dataRefreshListener)
            itemAnimator = null
        }
    }

    private fun updateAdapterData(photos: List<Photo>?) {
        photos ?: return

        adapter.updateData(photos)
    }
}