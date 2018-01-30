package com.lilac.priyacoder.materialdesinginkotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : BaseActivity() {

    lateinit private var staggeredLayoutManager: StaggeredGridLayoutManager
    private var isListView: Boolean = false
    lateinit private var adapter: ImageBucketsAdapter

    private val onItemClickListener = object : ImageBucketsAdapter.OnItemClickListener {
        override fun onItemClick(view: View, imageMap: HashMap<String,List<File>>, monthCode: String) {
            val intent = Intent(this@MainActivity,ImagesViewActivity::class.java)
            intent.putExtra("imageMap",imageMap)
            intent.putExtra("monthCode",monthCode)
            startActivity(intent)
        }
    }

//    private val onDefaultItemClickListener = object : TravelListAdapter.OnItemClickListener {
//        override fun onItemClick(view: View, position: Int) {
//            startActivity(DetailActivity.newIntent(this@MainActivity, position))
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(intent.hasExtra("imageMap")) {
            val imageMap: HashMap<String,List<File>> = intent.getSerializableExtra("imageMap") as HashMap<String, List<File>>

            isListView = true
            staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            list.layoutManager = staggeredLayoutManager

            adapter = ImageBucketsAdapter(this,imageMap)
            adapter.loadData()
            list.adapter = adapter

            adapter.setOnItemClickListener(onItemClickListener)
        } else {

            //Show file selection dialog
            displayFileChooser()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_toggle) {
            toggle()
            return true
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

