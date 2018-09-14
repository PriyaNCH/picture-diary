package com.lilac.priyacoder.materialdesigninkotlin.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.lilac.priyacoder.materialdesigninkotlin.*
import com.lilac.priyacoder.materialdesigninkotlin.di.model.ImageLoader
import com.lilac.priyacoder.materialdesigninkotlin.ui.activity.ImagesViewActivity
import com.lilac.priyacoder.materialdesigninkotlin.ui.adapter.ImageBucketsAdapter
import com.lilac.priyacoder.materialdesigninkotlin.ui.preferences.Prefs
import com.lilac.priyacoder.materialdesigninkotlin.ui.activity.base.BaseActivity
import com.lilac.priyacoder.materialdesigninkotlin.ui.activity.base.getDisplayMetrics
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
    private var isListView: Boolean = false
    private lateinit var imageBucketAdapter: ImageBucketsAdapter
    @Inject lateinit var imageLoader : ImageLoader

    private val onItemClickListener = object : ImageBucketsAdapter.OnItemClickListener {
        override fun onItemClick(view: View, imageMap: HashMap<String,List<File>>, monthCode: String) {
            val intent = Intent(this@MainActivity, ImagesViewActivity::class.java)
            intent.putExtra("imageMap",imageMap)
            intent.putExtra("monthCode",monthCode)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppComponent()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the shared preferences of the application
        val prefs = Prefs.getInstance(applicationContext)
        //Get the folder details the user selected previously
        val preferredFolderValue = prefs.getValues().getString(prefs.PREFERRED_FOLDER_KEY,null)

        // If the user has already selected folder, get the folder details
        if(preferredFolderValue != null) {
            val imageMap: HashMap<String, List<File>> = getImageMap(preferredFolderValue)

            if(imageMap.isNotEmpty()){
                // If the user selected folder has images, display them
                list.visibility = View.VISIBLE
                isListView = true
                staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                list.layoutManager = staggeredLayoutManager

                imageBucketAdapter = ImageBucketsAdapter(this, imageMap, imageLoader)
                imageBucketAdapter.loadData()
                list.adapter = imageBucketAdapter

                imageBucketAdapter.setOnItemClickListener(onItemClickListener)
            } else{
                // If there are no images in the folder or the folder is empty, display a message to user
                list.visibility = View.GONE

                val metrics = getDisplayMetrics(this)
                val deviceWidth = metrics.widthPixels
                val deviceHeight = metrics.heightPixels

                // Create an image view to display No images found
                val not_found_imageView = ImageView(this)
                not_found_imageView.setImageResource(R.drawable.ic_no_images_found)

                //Remove the Grid layout icon
                showGridToggle = false
                val imageLayoutParams = LinearLayout.LayoutParams(deviceWidth/2,deviceHeight/2)

                //Create a textview to prompt user to click on the folder icon in the app bar
                val click_folderIcon_textView = TextView(this)
                val imageSpan = ImageSpan(this, R.drawable.ic_open_folder)
                val spannableString = SpannableString(getString(R.string.click_folder_icon))

                // Add the folder icon at the index given for illustration
                spannableString.setSpan(imageSpan,15,16, 0)
                click_folderIcon_textView.text = spannableString

                val textViewLayoutparams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                textViewLayoutparams.setMargins(resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin),resources.getDimensionPixelSize(R.dimen.activity_vertical_margin),
                        resources.getDimensionPixelSize(R.dimen.activity_vertical_margin),resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin))

                // Add both the views to the layout manager
                mainActivity_layout.setHorizontalGravity(Gravity.CENTER)
                mainActivity_layout.addView(not_found_imageView,imageLayoutParams)
                mainActivity_layout.addView(click_folderIcon_textView,textViewLayoutparams)
            }
        }
        else {
            //In case user has not selected any folder yet, display the Directory Chooser Popup
            displayFolderChooserPopup()
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

    private fun toggle() {
        if (isListView) {
            staggeredLayoutManager.spanCount = 2
            showGridView()
        } else {
            staggeredLayoutManager.spanCount = 1
            showListView()
        }
    }

    private fun showListView() {
        val item = menu.findItem(R.id.action_toggle)
        item.setIcon(R.drawable.ic_action_grid)
        item.title = getString(R.string.show_as_grid)
        isListView = true
    }

    private fun showGridView() {
        val item = menu.findItem(R.id.action_toggle)
        item.setIcon(R.drawable.ic_action_list)
        item.title = getString(R.string.show_as_list)
        isListView = false
    }

    private fun initAppComponent(){
        PhotoDiaryApp.app()?.appComponent()?.inject(this)
    }
}

