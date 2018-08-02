package com.lilac.priyacoder.materialdesigninkotlin

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.daimajia.swipe.adapters.ArraySwipeAdapter
import kotlinx.android.synthetic.main.edit_entry_alert_dialog.view.*

class EntryListAdapter(context: Context?, resource: Int, textViewResourceId : Int) : ArraySwipeAdapter<String>(context, resource, textViewResourceId) {


    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        convertView?.findViewById<View>(R.id.editEntry)?.setOnClickListener {
            val textData = convertView.findViewById<TextView>(R.id.text_data)
            showInputDialog(textData,parent)
        }
        convertView?.findViewById<View>(R.id.deleteEntry)?.setOnClickListener { Toast.makeText(context,"Delete is clicked",Toast.LENGTH_SHORT).show() }
        return super.getView(position, convertView, parent)
    }

    private fun showInputDialog(entryView : TextView, viewGroup: ViewGroup?) {

        // Create an EditText to show the entry in full in
        val editAlertWindow = LayoutInflater.from(context).inflate(R.layout.edit_entry_alert_dialog,viewGroup,false)
        editAlertWindow.entryEditAlertView.setText(entryView.text.toString())
          editAlertWindow.entryEditAlertView.setSelection(editAlertWindow.entryEditAlertView.text.length)

        // Construct the Alert Dialog to edit the entry
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Title")

        builder.setView(editAlertWindow).setMessage("Edit the entry")

        builder.setPositiveButton("Done") { dialog, which ->
            dialog.dismiss()
            entryView.text = editAlertWindow.entryEditAlertView.text
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }
}