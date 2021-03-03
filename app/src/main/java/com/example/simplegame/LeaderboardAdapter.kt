package com.example.simplegame

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.leaderboardscore.view.*

class EntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var name: TextView = view.name
    var score: TextView = view.score

    fun bind(item: Score) {
        name.text = item.name
        score.text = item.score.toString()
    }
}

class LeaderboardAdapter(private var activity: Activity, private var entries: List<Score>)
    : RecyclerView.Adapter<EntryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder =
        EntryViewHolder(LayoutInflater.from(activity).inflate(
            R.layout.leaderboardscore, parent, false))
    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) =
        holder.bind(entries[position])

    override fun getItemCount(): Int = entries.size
}