package com.lilac.priyacoder.materialdesigninkotlin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : BaseActivity() {

    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
    private var isListView: Boolean = false
    private lateinit var imageBucketAdapter: ImageBucketsAdapter

    private val onItemClickListener = object : ImageBucketsAdapter.OnItemClickListener {
        override fun onItemClick(view: View, imageMap: HashMap<String,List<File>>, monthCode: String) {
            val intent = Intent(this@MainActivity,ImagesViewActivity::class.java)
            intent.putExtra("imageMap",imageMap)
            intent.putExtra("monthCode",monthCode)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = Prefs.getInstance(applicationContext)

        prefs.getValues().getString(prefs.PREFERRED_FOLDER_KEY,null) ?: displayFileChooser()

        val imageMap: HashMap<String, List<File>> = intent.getSerializableExtra("imageMap") as HashMap<String, List<File>>

        isListView = true
        staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        list.layoutManager = staggeredLayoutManager

        imageBucketAdapter = ImageBucketsAdapter(this, imageMap)
        imageBucketAdapter.loadData()
        list.adapter = imageBucketAdapter

        imageBucketAdapter.setOnItemClickListener(onItemClickListener)
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

