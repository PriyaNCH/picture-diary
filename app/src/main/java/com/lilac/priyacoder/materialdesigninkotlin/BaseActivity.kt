package com.lilac.priyacoder.materialdesigninkotlin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import lib.folderpicker.FolderPicker
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 1021422 on 10/15/2017.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var menu: Menu
    protected var showFileSelector = true
    protected var showGridToggle = true
    var myAppPrefs : Prefs? = null

    companion object {
        private const val SDCARD_PERMISSION_FOLDER = 12
        private const val FOLDER_PICKER_CODE = 78
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        when {
            !showFileSelector -> menu.findItem(R.id.file_browser).isVisible = false
            !showGridToggle -> menu.findItem(R.id.action_toggle).isVisible = false
        }

        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.file_browser) {
            displayFileChooser()
        }
        return super.onOptionsItemSelected(item)
    }

    fun displayFileChooser() {

        if(Build.VERSION.SDK_INT >= 23) {
            if (!storagePermissionAvailable()) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), SDCARD_PERMISSION_FOLDER)
            }else {
                pickFolder()
            }
        } else {
            pickFolder()
        }
    }

    private fun pickFolder() {
        val intent = Intent(this.applicationContext, FolderPicker::class.java)
        startActivityForResult(intent, FOLDER_PICKER_CODE)
    }

    private fun storagePermissionAvailable(): Boolean {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            SDCARD_PERMISSION_FOLDER -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {

                pickFolder()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        if (requestCode == FOLDER_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            val monthImageMap: HashMap<String, List<File>> = HashMap()
            val df: DateFormat = SimpleDateFormat("MMM", Locale.GERMANY)

            val folderLocation = intent.extras!!.getString("data")

            val prefs = Prefs.getInstance(applicationContext)
            prefs.setValue(prefs.PREFERRED_FOLDER_KEY,folderLocation)

            val directory = File(folderLocation)
            val files: Array<File> = directory.listFiles()

            for (imageFile in files) {
                val month = df.format(imageFile.lastModified())

                if (!monthImageMap.containsKey(month)) {
                    monthImageMap[month] = ArrayList()
                }
                (monthImageMap[month]!! as ArrayList).add(imageFile)
            }

            val mainActivityIntent = Intent(this.applicationContext, MainActivity::class.java)
            mainActivityIntent.putExtra("imageMap", monthImageMap)
            startActivity(mainActivityIntent)
        }
    }
}