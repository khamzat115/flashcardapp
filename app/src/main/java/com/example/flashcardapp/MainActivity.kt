package com.example.flashcardapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.flashcard_question).setOnClickListener {
            findViewById<TextView>(R.id.flashcard_answer).visibility = TextView.VISIBLE
            findViewById<TextView>(R.id.flashcard_question).visibility = TextView.INVISIBLE
            // Do something here
        }
        findViewById<TextView>(R.id.flashcard_answer).setOnClickListener {
            findViewById<TextView>(R.id.flashcard_answer).visibility = TextView.INVISIBLE
            findViewById<TextView>(R.id.flashcard_question).visibility = TextView.VISIBLE
            // Do something here
        }
    }

}