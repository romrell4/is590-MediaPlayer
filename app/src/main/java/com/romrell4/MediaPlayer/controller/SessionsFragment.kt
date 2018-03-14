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
import com.romrell4.MediaPlayer.model.Session
import com.romrell4.MediaPlayer.support.inflateV2

/**
 * Created by romrell4 on 3/4/18
 */
class SessionsFragment: Fragment() {
    companion object {
        private const val SESSIONS_EXTRA = "sessions"

        fun newInstance(sessions: ArrayList<Session>): SessionsFragment {
            val fragment = SessionsFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(SESSIONS_EXTRA, sessions)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflateV2(R.layout.fragment_recycler_view, container)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = SessionAdapter(arguments.getParcelableArrayList(SESSIONS_EXTRA))
        return view
    }

    inner class SessionAdapter(var sessions: List<Session>): RecyclerView.Adapter<SessionAdapter.ViewHolder>() {
        override fun getItemCount() = sessions.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(activity.layoutInflater.inflateV2(R.layout.row_card_text, parent))
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(sessions[position])
        }

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val textView = view.findViewById<TextView>(R.id.textView)

            fun bind(session: Session) {
                textView.text = session.title

                itemView.setOnClickListener {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, TalksFragment.newInstance(session.talks))
                            .addToBackStack(null)
                            .commit()
                }
            }
        }
    }
}