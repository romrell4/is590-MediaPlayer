package com.romrell4.MediaPlayer.controller

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.romrell4.MediaPlayer.R

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager.beginTransaction()
                .add(R.id.frameLayout, ConferencesFragment.onNewInstance())
                .addToBackStack(null)
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.cache, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.delete_cache -> {
            try {
                val videoCacheDir = cacheDir.listFiles().first { it.name == "video-cache" }
                val cachedFiles = videoCacheDir.listFiles()
                val toDelete = arrayListOf<Int>()
                AlertDialog.Builder(this)
                        .setMultiChoiceItems(cachedFiles.map { it.name }.toTypedArray(), null, { _, which, isChecked ->
                            if (isChecked) {
                                toDelete.add(which)
                            } else {
                                toDelete.remove(which)
                            }
                        })
                        .setPositiveButton("Delete", { dialog, which ->
                            toDelete.map { cachedFiles.get(it) }.forEach { it.delete() }
                        })
                        .setNegativeButton("Cancel", null)
                        .show()
            } catch (ignore: NoSuchElementException) {
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
