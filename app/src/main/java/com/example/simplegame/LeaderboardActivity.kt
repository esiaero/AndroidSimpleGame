package com.example.simplegame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.leaderboard.*

class LeaderboardActivity : AppCompatActivity() {
    private var board: MutableList<Score> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboard)
        loadLeaderboard()
        entry.adapter?.notifyDataSetChanged()
        entry.layoutManager = LinearLayoutManager(this)
        entry.adapter = LeaderboardAdapter(this, board)

        resetLeaderboard.setOnClickListener {
            board.clear()
            entry.adapter?.notifyDataSetChanged()
            val editor = this.getSharedPreferences("board", MODE_PRIVATE).edit()
            editor.clear()
            editor.apply()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val back = Intent(this, MainActivity::class.java)
        startActivity(back)
    }

    private fun loadLeaderboard() {
        val boardUnsort = ArrayList<Score>()
        val sharedPref = this.getSharedPreferences("board", MODE_PRIVATE)
        if (sharedPref.contains("scores")) {
            val scoreString = sharedPref.getString("scores", "")
            val scores = scoreString!!.split("&")
            for (pairs in scores) {
                val nameAndScore = pairs.split("+")
                boardUnsort.add(Score(nameAndScore[0], nameAndScore[1].toInt()))
            }
        }

        board = boardUnsort.sortedByDescending { it.score }.toMutableList()
    }
}