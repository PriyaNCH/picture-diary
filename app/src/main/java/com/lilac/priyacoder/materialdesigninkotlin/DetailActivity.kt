package com.lilac.priyacoder.materialdesigninkotlin

import android.animation.Animator
import android.arch.persistence.room.Room
import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.ArraySwipeAdapter
import com.daimajia.swipe.util.Attributes
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.entries_listview.*
import java.io.File
import java.security.KeyStore


class DetailActivity : BaseActivity(){

    private lateinit var entryListAdapter: EntryListAdapter
    private var photoEntries : MutableList<String>? = null

    private var imageFile: File? = null
    private var isEditMode: Boolean = false

    var inputMethodMgr : InputMethodManager? = null

    companion object {
        var database : PhotoEntryDatabase? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if(savedInstanceState != null){
            entryEditText.setText(savedInstanceState.getString(getString(R.string.editTextValue)))
        }

        photoEntries = mutableListOf("a","b")
        entryListAdapter = EntryListAdapter(this,R.layout.entries_listview,R.id.text_data,photoEntries)
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
                    for(entries in photoEntries) {
                        entryListAdapter.add(entries.photoEntry)
                    }
                }

        loadPlace()

        placeTitle.maxLines = 1
        placeTitle.isSelected = true

        submitButton.setOnClickListener { view -> onClick(view) }
        addButton.setOnClickListener { view -> onClick(view) }

    }

    // Save value entered in EditText so that the value can be retained during configuration changes like orientation etc.
    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outPersistentState?.putString(resources.getString(R.string.editTextValue),entryEditText.text.toString())
    }

    private fun loadPlace() {
        placeTitle.text = imageFile?.name
        Picasso.with(this).load(imageFile).fit().centerCrop().into(placeImage)
    }

    private fun onClick(view: View) {
        when(view.id){
            R.id.addButton -> {
                if(isEditMode){

                    inputMethodMgr?.hideSoftInputFromWindow(entryEditText.windowToken,0)
                    submitButton.animate().setListener(object:Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) { return }

                        override fun onAnimationEnd(p0: Animator?) { submitButton.visibility = View.GONE }

                        override fun onAnimationCancel(p0: Animator?) { return }

                        override fun onAnimationStart(p0: Animator?) { return }

                    })
                    submitButton.animate().x(addButton.x).setDuration(1000).start()

                    addButton.setImageResource(R.drawable.icn_rotate_reverse)
                    (addButton.drawable as Animatable).start()

                    revealView.setBackgroundColor(0)
                    entryEditText.visibility = View.GONE
                    isEditMode = false
                }

                else {
                    submitButton.animate().setListener(object:Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) { return }

                        override fun onAnimationEnd(p0: Animator?) { return }

                        override fun onAnimationCancel(p0: Animator?) { return }

                        override fun onAnimationStart(p0: Animator?) { submitButton.visibility = View.VISIBLE }

                    })
                    submitButton.animate().x(addButton.x - 200).setDuration(1000).start()
                    addButton.setImageResource(R.drawable.icn_rotate)
                    (addButton.drawable as Animatable).start()

//                  revealView.setBackgroundColor(R.string.blur_effect)
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
                    entryEditText.setBackgroundResource(0)

                    addPhotoEntries()
                    entryEditText.setText("")
                } else {
                    entryEditText.setBackgroundResource(R.drawable.border)
                    Toast.makeText(this,"Please make an entry",Toast.LENGTH_SHORT).show()
                }

                submitButton.animate().setListener(object:Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) { return }

                    override fun onAnimationEnd(p0: Animator?) { submitButton.visibility = View.GONE }

                    override fun onAnimationCancel(p0: Animator?) { return }

                    override fun onAnimationStart(p0: Animator?) { return }

                })
                submitButton.animate().x(addButton.x).setDuration(500).start()

                addButton.setImageResource(R.drawable.icn_rotate_reverse)
                (addButton.drawable as Animatable).start()

                revealView.setBackgroundColor(0)
                entryEditText.visibility = View.GONE
                isEditMode = false

//              val transAnimation = ObjectAnimator.ofFloat(submitButton,"x",20f)
//              transAnimation.duration = 1000
//              transAnimation.start()
//              submitButton.visibility = View.GONE
//              addButton.setImageResource(R.drawable.icn_rotate_reverse)
//              (addButton.drawable as Animatable).start()
            }
        }
    }

    private fun addPhotoEntries(){
        val photoEntries = PhotoEntries(0,imageFile?.absolutePath,entryEditText.text.toString())

        Single.fromCallable {
            DetailActivity.database?.photoEntryDao()?.insert(photoEntries) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
}
