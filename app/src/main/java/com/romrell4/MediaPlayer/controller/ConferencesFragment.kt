package com.romrell4.MediaPlayer.controller

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.romrell4.MediaPlayer.R
import com.romrell4.MediaPlayer.model.Conference
import com.romrell4.MediaPlayer.support.inflateV2

/**
 * Created by romrell4 on 3/4/18
 */
class ConferencesFragment: Fragment() {
    companion object {
        fun onNewInstance(): ConferencesFragment {
            return ConferencesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflateV2(R.layout.fragment_recycler_view, container)

        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = ConferenceAdapter(Conference.readConferences(activity))

        return view
    }

    inner class ConferenceAdapter(var conferences: List<Conference>): RecyclerView.Adapter<ConferenceAdapter.ViewHolder>() {
        override fun getItemCount() = conferences.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(activity.layoutInflater.inflateV2(R.layout.row_card_text, parent))
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(conferences[position])
        }

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val textView = view.findViewById<TextView>(R.id.textView)

            fun bind(conference: Conference) {
                textView.text = getString(R.string.conference_name_format, conference.month, conference.year)

                itemView.setOnClickListener {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, SessionsFragment.newInstance(conference.sessions))
                            .addToBackStack(null)
                            .commit()
                }
            }
        }
    }
}