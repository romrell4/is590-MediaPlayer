package com.romrell4.MediaPlayer.controller

import android.os.Bundle
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
}
