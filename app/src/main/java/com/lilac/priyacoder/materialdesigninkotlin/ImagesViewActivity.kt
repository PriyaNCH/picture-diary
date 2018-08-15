package com.lilac.priyacoder.materialdesigninkotlin

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.images_view.*
import java.io.File

/**
 * Created by 1021422 on 10/15/2017.
 */
class ImagesViewActivity: BaseActivity() {

    private var isListView: Boolean = false
    private lateinit var adapter: ImagesViewAdapter
    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager

    private val onItemClickListener = object : ImagesViewAdapter.OnItemClickListener {
        override fun onItemClick(view: View, file: File) {
            val intent = Intent(this@ImagesViewActivity,DetailActivity::class.java)
            intent.putExtra("file",file)
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
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

        adapter = ImagesViewAdapter(this, listOfFiles)
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
}