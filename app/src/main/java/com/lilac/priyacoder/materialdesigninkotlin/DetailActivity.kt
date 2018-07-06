package com.lilac.priyacoder.materialdesigninkotlin

import android.animation.Animator
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.File

class DetailActivity : BaseActivity(){

    private lateinit var inputManager: InputMethodManager
    private lateinit var todoList: ArrayList<String>
    private lateinit var toDoAdapter: ArrayAdapter<*>

    private var imageFile: File? = null
    private var isEditMode: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)

    // Set this property to remove the Grid Toggle button in the app bar as it is not required
    super.showGridToggle = false

    imageFile = intent!!.getSerializableExtra("file") as File
    loadPlace()

    placeTitle.maxLines = 1
    placeTitle.isSelected = true

    submitButton.setOnClickListener { view -> onClick(view) }
      addButton.setOnClickListener { view -> onClick(view) }
  }

  private fun loadPlace() {
    placeTitle.text = imageFile?.name
    Picasso.with(this).load(imageFile).fit().centerCrop().into(placeImage)
  }

  private fun onClick(view: View) {
      when(view.id){
          R.id.addButton -> {
              if(isEditMode){

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
                  todoText.visibility = View.GONE
                  isEditMode = false
              }

              else {
                  submitButton.animate().setListener(object:Animator.AnimatorListener {
                      override fun onAnimationRepeat(p0: Animator?) { return }

                      override fun onAnimationEnd(p0: Animator?) { return }

                      override fun onAnimationCancel(p0: Animator?) { return }

                      override fun onAnimationStart(p0: Animator?) { submitButton.visibility = View.VISIBLE }

                  })
                  submitButton.animate().x(addButton.x - 200).setDuration(500).start()
                  addButton.setImageResource(R.drawable.icn_rotate)
                  (addButton.drawable as Animatable).start()

                  revealView.setBackgroundColor(R.string.blur_effect)
                  todoText.visibility = View.VISIBLE
                  todoText.requestFocus()
                  isEditMode = true
              }
          }

          R.id.submitButton -> {
              return
//              val transAnimation = ObjectAnimator.ofFloat(submitButton,"x",20f)
//              transAnimation.duration = 1000
//              transAnimation.start()
//              submitButton.visibility = View.GONE
//              addButton.setImageResource(R.drawable.icn_rotate_reverse)
//              (addButton.drawable as Animatable).start()
          }
      }
    }
}
