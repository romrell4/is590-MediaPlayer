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
import com.romrell4.MediaPlayer.model.Talk
import com.romrell4.MediaPlayer.support.inflateV2

/**
 * Created by romrell4 on 3/4/18
 */
class TalksFragment: Fragment() {
    companion object {
        private const val TALKS_EXTRA = "talks"

        fun newInstance(talks: ArrayList<Talk>): TalksFragment {
            val fragment = TalksFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(TALKS_EXTRA, talks)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflateV2(R.layout.fragment_recycler_view, container)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = TalkAdapter(arguments.getParcelableArrayList<Talk>(TALKS_EXTRA))
        return view
    }

    inner class TalkAdapter(val talks: List<Talk>): RecyclerView.Adapter<TalkAdapter.ViewHolder>() {
        override fun getItemCount() = talks.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(activity.layoutInflater.inflateV2(R.layout.row_card_text, parent))
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(talks[position])
        }

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val textView = view.findViewById<TextView>(R.id.textView)

            fun bind(talk: Talk) {
                textView.text = talk.title

                itemView.setOnClickListener {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, ExoMediaFragment.newInstance(talk.videoUrl, talk.audioUrl))
                            .addToBackStack(null)
                            .commit()
                }
            }
        }
    }
}