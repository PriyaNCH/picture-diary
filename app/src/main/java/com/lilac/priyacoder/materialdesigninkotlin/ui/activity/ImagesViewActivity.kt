package com.lilac.priyacoder.materialdesigninkotlin.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MenuItem
import android.view.View
import com.lilac.priyacoder.materialdesigninkotlin.PhotoDiaryApp
import com.lilac.priyacoder.materialdesigninkotlin.R
import com.lilac.priyacoder.materialdesigninkotlin.di.model.ImageLoader
import com.lilac.priyacoder.materialdesigninkotlin.ui.adapter.ImagesViewAdapter
import com.lilac.priyacoder.materialdesigninkotlin.ui.activity.base.BaseActivity
import kotlinx.android.synthetic.main.images_view.*
import java.io.File
import javax.inject.Inject

/**
 * Created by Vishnu Priya Nallan on 10/15/2017.
 */
class ImagesViewActivity: BaseActivity() {

    private var isListView: Boolean = false
    private lateinit var adapter: ImagesViewAdapter
    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
    @Inject lateinit var imageLoader : ImageLoader

    private val onItemClickListener = object : ImagesViewAdapter.OnItemClickListener {
        override fun onItemClick(view: View, file: File) {
            val intent = Intent(this@ImagesViewActivity, DetailActivity::class.java)
            intent.putExtra("file",file)
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        initAppComponent()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.images_view)

        //Enable this to add an Up action
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val monthCode = intent.getStringExtra("monthCode")
        val imageMap: HashMap<String,List<File>> = intent.getSerializableExtra("imageMap") as HashMap<String, List<File>>
        val listOfFiles: List<File>? = imageMap[monthCode]

        isListView = true
        staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        imagelist.layoutManager = staggeredLayoutManager

        adapter = ImagesViewAdapter(this, listOfFiles, imageLoader)
        imagelist.adapter = adapter

        adapter.setOnItemClickListener(onItemClickListener)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_toggle -> {
                toggle()
                return true
            }
            R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun toggle() {
        if (isListView) {
            staggeredLayoutManager.spanCount = 2
            showGridView()
        } else {
            staggeredLayoutManager.spanCount = 1
            showListView()
        }
    }

    fun showListView() {
        val item = menu.findItem(R.id.action_toggle)
        item.setIcon(R.drawable.ic_action_grid)
        item.title = getString(R.string.show_as_grid)
        isListView = true
    }

    fun showGridView() {
        val item = menu.findItem(R.id.action_toggle)
        item.setIcon(R.drawable.ic_action_list)
        item.title = getString(R.string.show_as_list)
        isListView = false
    }

    private fun initAppComponent(){
        PhotoDiaryApp.app()?.appComponent()?.inject(this)
    }
}