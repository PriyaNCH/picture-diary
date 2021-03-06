package com.lilac.priyacoder.materialdesigninkotlin.ui.activity

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.daimajia.swipe.util.Attributes
import com.lilac.priyacoder.materialdesigninkotlin.PhotoDiaryApp
import com.lilac.priyacoder.materialdesigninkotlin.R
import com.lilac.priyacoder.materialdesigninkotlin.data.db.PhotoEntryDatabase
import com.lilac.priyacoder.materialdesigninkotlin.data.db.model.PhotoEntriesModel
import com.lilac.priyacoder.materialdesigninkotlin.di.model.ImageLoader
import com.lilac.priyacoder.materialdesigninkotlin.ui.activity.base.BaseActivity
import com.lilac.priyacoder.materialdesigninkotlin.ui.adapter.EntryListAdapter
import com.lilac.priyacoder.materialdesigninkotlin.utils.ImageUtils
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.preview_image.*
import java.io.File
import javax.inject.Inject

class DetailActivity : BaseActivity(){

    private lateinit var entryListAdapter: EntryListAdapter

    private var imageFile: File? = null
    private var isEditMode: Boolean = false

    private var mClickListener : View.OnClickListener? = null

    var inputMethodMgr : InputMethodManager? = null

    @Inject lateinit var database : PhotoEntryDatabase
    @Inject lateinit var imageLoader : ImageLoader

    private val animationDuration : Long = 500
    private var paletteColor : Palette.Swatch? = null

    // Declare target as class member so that the picasso holds a strong reference to it
    private lateinit var target : Target

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppComponent()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(savedInstanceState != null){
            entryEditText.setText(savedInstanceState.getString(getString(R.string.editTextValue)))
        }
        entryListAdapter = EntryListAdapter(this, R.layout.entries_listview, R.id.text_data)
        entriesList.adapter = entryListAdapter
        entryListAdapter.mode = Attributes.Mode.Single

        // Set this property to remove the Grid Toggle button in the app bar as it is not required
        super.showGridToggle = false

        imageFile = intent?.getSerializableExtra("file") as File

        // Show the image selected by the user
        loadImage()

        // Display the image name
        placeTitle.maxLines = 1
        placeTitle.isSelected = true

        mClickListener = View.OnClickListener { it ->
            val imageDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
            imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            imageDialog.setCancelable(true)
            imageDialog.setContentView(R.layout.preview_image)

            imageLoader.loadToImageView(imageFile as File,imageDialog.preview_imageView)
            imageDialog.close_image_preview_button.setOnClickListener {
                imageDialog.dismiss()
            }
            imageDialog.show()
        }

        //On Touch the full image is displayed in the entire screen
        placeImageDetail.setOnClickListener(mClickListener)

        submitButton.setOnClickListener { view -> onClick(view) }
        addButton.setOnClickListener { view -> onClick(view) }
    }

    private fun initAppComponent(){
        PhotoDiaryApp.app()?.appComponent()?.inject(this)
    }

    override fun onResume() {
        super.onResume()
        // Database query is made in onResume so that every time the activity is open most updated list is shown.
        // Fetch and display entries in the list view
        database.photoEntryDao().getAllEntries(imagePath = imageFile?.absolutePath).subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe{photoEntries ->
                    entryListAdapter.clear()
                    entryListAdapter.addAll(photoEntries)
                }
    }

    // Save value entered in EditText so that the value can be retained during configuration changes like orientation etc.
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(resources.getString(R.string.editTextValue),entryEditText.text.toString())
    }

    private fun loadImage() {
        placeTitle.text = imageFile?.name
        val imageDimensions = ImageUtils.getFileDimensions(imageFile)

        target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                Toast.makeText(this@DetailActivity,"Image failed to load",Toast.LENGTH_SHORT).show()
            }

            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                assert(placeImageDetail != null)
                placeImageDetail.setImageBitmap(bitmap)

                Palette.from(bitmap)
                        .generate { palette ->
                            paletteColor = palette.vibrantSwatch
                            if(palette.vibrantSwatch != null){
                                placeNameHolder.setBackgroundColor(paletteColor!!.rgb)
                            }
                        }
            }
        }

        // Tag the target to the imageView for keeping a strong reference
        placeImageDetail.tag = target

        imageLoader.loadToImageView(imageFile as File,target,imageDimensions.outWidth, imageDimensions.outHeight)
    }

    private fun onClick(view: View) {
        when(view.id){
            R.id.addButton -> {
                if(isEditMode){

                    revealEditText()
                    entryEditText.setText("")
                    inputMethodMgr?.hideSoftInputFromWindow(entryEditText.windowToken,0)
                    submitButton.animate().setListener(object:Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) { return }

                        override fun onAnimationEnd(p0: Animator?) {
                            submitButton.visibility = View.GONE
                            addButton.setImageResource(R.drawable.icn_rotate_reverse)
                            (addButton.drawable as Animatable).start()
                        }

                        override fun onAnimationCancel(p0: Animator?) { return }

                        override fun onAnimationStart(p0: Animator?) { return }

                    })
                    submitButton.animate().x(addButton.x).setDuration(animationDuration).start()

                    entryEditText.visibility = View.GONE
                    isEditMode = false
                }

                else {
                    submitButton.animate().setListener(object:Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) { return }

                        override fun onAnimationEnd(p0: Animator?) { return }

                        override fun onAnimationCancel(p0: Animator?) { return }

                        override fun onAnimationStart(p0: Animator?) {
                            addButton.setImageResource(R.drawable.icn_rotate)
                            (addButton.drawable as Animatable).start()
                            submitButton.visibility = View.VISIBLE
                            hideEditText()
                        }

                    })
                    submitButton.animate().x(addButton.x - 200).setDuration(animationDuration).start()

                    entryEditText.visibility = View.VISIBLE
                    entryEditText.requestFocus()
                    inputMethodMgr = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodMgr?.showSoftInput(entryEditText,InputMethodManager.SHOW_IMPLICIT)
                    isEditMode = true
                }
            }

            R.id.submitButton -> {
                revealEditText()
                if(entryEditText.text.toString().isNotEmpty()){
                    inputMethodMgr?.hideSoftInputFromWindow(entryEditText.windowToken,0)

                    addPhotoEntries()
                    entryEditText.setText("")

                    submitButton.animate().setListener(object:Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) { return }

                        override fun onAnimationEnd(p0: Animator?) {
                            addButton.setImageResource(R.drawable.icn_rotate_reverse)
                            (addButton.drawable as Animatable).start()
                            submitButton.visibility = View.GONE }

                        override fun onAnimationCancel(p0: Animator?) { return }

                        override fun onAnimationStart(p0: Animator?) { return }

                    })
                        submitButton.animate().x(addButton.x).setDuration(animationDuration).start()

                    entryEditText.visibility = View.GONE
                    isEditMode = false
                } else {
                    entryEditText.error = getString(R.string.entry_empty_error)
                }
            }
        }
    }

    private fun addPhotoEntries(){
        val photoEntries = PhotoEntriesModel(0, imageFile?.absolutePath, entryEditText.text.toString())

        Single.fromCallable {
            database.photoEntryDao().insert(photoEntries)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun revealEditText(){
        revealView.setBackgroundColor(0)
    }
    private fun hideEditText(){
        if(paletteColor == null) {
            revealView.setBackgroundColor(ContextCompat.getColor(this,android.R.color.transparent))
        }else {
            revealView.setBackgroundColor(paletteColor!!.rgb)
        }
    }

}
