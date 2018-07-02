package com.lilac.priyacoder.materialdesinginkotlin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import lib.folderpicker.FolderPicker
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Created by 1021422 on 10/15/2017.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var menu: Menu
    private val SDCARD_PERMISSION_FOLDER = 12
    private val SDCARD_PERMISSION_FILE = 123
    private val FOLDER_PICKER_CODE = 78
    private val FILE_PICKER_CODE = 786
    private var showFileSelector = true
    private var showGridToggle = true

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
        if (Build.VERSION.SDK_INT < 23) {
            if (Constansts.IS_FOLDER) {
                pickFolder()
            } else {
                pickFile()
            }
        } else
            if (storagePermissionAvailable()) {

                if (Constansts.IS_FOLDER) {
                    pickFolder()
                } else {
                    pickFile()
                }
            } else {
                if (Constansts.IS_FOLDER) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), SDCARD_PERMISSION_FOLDER)

                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), SDCARD_PERMISSION_FILE)
                }
            }
    }

    fun pickFolder() {
        val intent = Intent(this.applicationContext, FolderPicker::class.java)
        startActivityForResult(intent, FOLDER_PICKER_CODE)

    }

    fun pickFile() {

        val intent = Intent(this, FolderPicker::class.java)
        intent.putExtra("title", "Select file to upload")
        intent.putExtra("location", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath)
        intent.putExtra("pickFiles", true)

        startActivityForResult(intent, FILE_PICKER_CODE)

    }

    fun storagePermissionAvailable(): Boolean {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            SDCARD_PERMISSION_FOLDER -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {

                pickFolder()

            }

            SDCARD_PERMISSION_FILE -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {

                pickFile()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        if (requestCode == FOLDER_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            var monthImageMap: HashMap<String, List<File>> = HashMap()
            val df: DateFormat = SimpleDateFormat("MMM")

            val folderLocation = intent.extras!!.getString("data")
            Log.i("folderLocation", folderLocation)
            val directory = File(folderLocation)
            val files: Array<File> = directory.listFiles()

            for (imageFile in files) {
                val month = df.format(imageFile.lastModified())

                if (!monthImageMap.containsKey(month)) {
                    monthImageMap[month] = ArrayList()
                }
                (monthImageMap[month]!! as ArrayList).add(imageFile)
            }

            val intent = Intent(this.applicationContext, MainActivity::class.java)
            intent.putExtra("imageMap", monthImageMap)
            startActivity(intent)
        }
    }
}