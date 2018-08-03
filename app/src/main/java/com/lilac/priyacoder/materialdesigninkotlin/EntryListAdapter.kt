package com.lilac.priyacoder.materialdesigninkotlin

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.daimajia.swipe.adapters.ArraySwipeAdapter
import com.lilac.priyacoder.materialdesigninkotlin.DetailActivity.Companion.database
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.edit_entry_alert_dialog.view.*

class EntryListAdapter(context: Context?, resource: Int, textViewResourceId : Int) : ArraySwipeAdapter<PhotoEntriesModel>(context, resource, textViewResourceId) {


    var resource : Int = resource
    private var photoEntryObject : PhotoEntriesModel
    var textResId : Int = textViewResourceId

    init{
        photoEntryObject = PhotoEntriesModel(0,"","")
    }
    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var returnView : View
        var viewHolder: ArrayAdapterViewHolder?
        photoEntryObject = getItem(position) as PhotoEntriesModel

        if(convertView == null){
            returnView = LayoutInflater.from(context).inflate(R.layout.entries_listview,parent,false)

            viewHolder = ArrayAdapterViewHolder(returnView)

            returnView.tag = viewHolder
        } else {
            returnView = convertView
            viewHolder = returnView.tag as ArrayAdapterViewHolder
        }

        // Set values in the list view
        viewHolder.textData?.text = photoEntryObject.photoEntry

        returnView?.findViewById<View>(R.id.editEntry)?.setOnClickListener {
                showInputDialog(viewHolder?.textData,parent, position) // Need to remove textData and in the function directly call textResourceId instead of entryview
        }
        returnView?.findViewById<View>(R.id.deleteEntry)?.setOnClickListener { Toast.makeText(context,"Delete is clicked",Toast.LENGTH_SHORT).show() }
        return returnView
    }

    private fun showInputDialog(entryView : TextView?, viewGroup: ViewGroup?, pos : Int) {

        // Create an EditText to show the entry in full in
        val editAlertWindow = LayoutInflater.from(context).inflate(R.layout.edit_entry_alert_dialog,viewGroup,false)
        editAlertWindow.entryEditAlertView.setText(entryView?.text.toString())
        editAlertWindow.entryEditAlertView.setSelection(editAlertWindow.entryEditAlertView.text.length)

        // Construct the Alert Dialog to edit the entry
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Entry")

        builder.setView(editAlertWindow).setMessage("Click Done to save or Cancel to cancel editing")

        builder.setPositiveButton("Done") { dialog, which ->
            dialog.dismiss()

            val editedText = editAlertWindow.entryEditAlertView.text

            if(editedText != null) {
                photoEntryObject = getItem(pos) as PhotoEntriesModel
                photoEntryObject.setEntry(editedText.toString())
                Single.fromCallable{
                    database?.photoEntryDao()?.update(photoEntryObject)}
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe()
                this.notifyDataSetChanged()
            } else{
                Toast.makeText(context,"Entry must be made before saving",Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }
}

private class ArrayAdapterViewHolder(view : View) {
    var textData : TextView? = null

    init{
        textData = view.findViewById(R.id.text_data)
    }
}