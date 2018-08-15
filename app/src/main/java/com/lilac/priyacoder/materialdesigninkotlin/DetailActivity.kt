package com.lilac.priyacoder.materialdesigninkotlin

import android.animation.Animator
import android.app.Dialog
import android.arch.persistence.room.Room
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.graphics.Palette
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.daimajia.swipe.util.Attributes
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.preview_image.*
import java.io.File

class DetailActivity : BaseActivity(){

    private lateinit var entryListAdapter: EntryListAdapter

    private var imageFile: File? = null
    private var isEditMode: Boolean = false

    private var mClickListener : View.OnClickListener? = null

    var inputMethodMgr : InputMethodManager? = null

    companion object {
        var database : PhotoEntryDatabase? = null
    }

    private val animationDuration : Long = 1000

    // Declare target as class member so that the picasso holds a strong reference to it
    private lateinit var target : Target

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(savedInstanceState != null){
            entryEditText.setText(savedInstanceState.getString(getString(R.string.editTextValue)))
        }
        entryListAdapter = EntryListAdapter(this,R.layout.entries_listview,R.id.text_data)
        entriesList.adapter = entryListAdapter
        entryListAdapter.mode = Attributes.Mode.Single

        // Initialize database to fetch or insert entries of the photo from/to database
        database = Room.databaseBuilder(this, PhotoEntryDatabase::class.java,"photo-diary-database").build()

        // Set this property to remove the Grid Toggle button in the app bar as it is not required
        super.showGridToggle = false

        imageFile = intent!!.getSerializableExtra("file") as File

        // Fetch and display entries in the list view
        database?.photoEntryDao()?.getAllEntries(imagePath = imageFile?.absolutePath)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe{photoEntries ->
                    entryListAdapter.clear()
                    entryListAdapter.addAll(photoEntries)
                }

        // Show the image selected by the user
        loadImage()

        // Display the image name
        placeTitle.maxLines = 1
        placeTitle.isSelected = true

        mClickListener = View.OnClickListener { it ->
            val imageDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
            imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            imageDialog.setCancelable(false)
            imageDialog.setContentView(R.layout.preview_image)

            Picasso.with(this).load(imageFile).fit().centerCrop().into(imageDialog.preview_imageView)

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

    // Save value entered in EditText so that the value can be retained during configuration changes like orientation etc.
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(resources.getString(R.string.editTextValue),entryEditText.text.toString())
    }

    private fun loadImage() {
        placeTitle.text = imageFile?.name
        val imageDimensions = getFileDimensions()

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
                            val paletteColor = palette.vibrantSwatch
                            if(palette.vibrantSwatch != null){
                                placeNameHolder.setBackgroundColor(paletteColor!!.rgb)
                            }
                        }
            }
        }

        // Tag the target to the imageView for keeping a strong reference
        placeImageDetail.tag = target
        Picasso.with(this@DetailActivity)
                .load(imageFile)
                .resize(imageDimensions.outWidth, imageDimensions.outHeight)
                .centerCrop()
                .into(target)
    }

    private fun getFileDimensions() : BitmapFactory.Options {

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFile?.absolutePath,options)
        return options
    }

    private fun onClick(view: View) {
        when(view.id){
            R.id.addButton -> {
                if(isEditMode){

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

                    revealView.setBackgroundColor(0)
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
                            submitButton.visibility = View.VISIBLE }

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

                    revealView.setBackgroundColor(0)
                    entryEditText.visibility = View.GONE
                    isEditMode = false
                } else {
                    entryEditText.error = getString(R.string.entry_empty_error)
                }
            }
        }
    }

    private fun addPhotoEntries(){
        val photoEntries = PhotoEntriesModel(0,imageFile?.absolutePath,entryEditText.text.toString())

        Single.fromCallable {
            DetailActivity.database?.photoEntryDao()?.insert(photoEntries) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
}
