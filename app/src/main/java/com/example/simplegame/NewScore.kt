package com.example.simplegame

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.leaderboard.*
import kotlinx.android.synthetic.main.leaderboardnewscore.*

class NewScore : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboardnewscore)
        save_score.setOnClickListener {
            val score = intent.getIntExtra("score", 0)
            var name = input_name.text.toString()
            if (name.isNullOrBlank()) { //so everyone on the leaderboard has a name
                name = "No Name"
            }
            val scoreInt = Intent(this, LeaderboardActivity::class.java)
            saveScore(name, score.toString())
            startActivity(scoreInt)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val back = Intent(this, MainActivity::class.java)
        startActivity(back)
    }

    private fun saveScore(name : String, score: String) {
        val sharedPref = this.getSharedPreferences("board", MODE_PRIVATE)
        val editor = sharedPref.edit()
        if (sharedPref.contains("scores")) {
            val scores = sharedPref.getString("scores", "")
            editor.putString("scores", "$scores&$name+$score")
        } else {
            editor.putString("scores", "$name+$score")
        }
        editor.apply()
    }
}