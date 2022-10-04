package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity: AppCompatActivity() {
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
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (data != null) { // Check that we have data returned
                val questionStr = data.getStringExtra("questionKey") // 'string1' needs to match the key we used when we put the string in the Intent
                val answerStr = data.getStringExtra("answerKey")

                findViewById<TextView>(R.id.flashcard_question).text = questionStr
                findViewById<TextView>(R.id.flashcard_answer).text = answerStr
                // Log the value of the strings for easier debugging
                Log.i("MainActivity", "question: $questionStr")
                Log.i("MainActivity", "answer: $answerStr")
            } else {
                Log.i("MainActivity", "Returned null data from AddCardActivity")
            }
        }

        findViewById<ImageView>(R.id.addBtn).setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            // Launch EndingActivity with the resultLauncher so we can execute more code
            // once we come back here from EndingActivity
//            intent.putExtra("stringKey1", "harry potter");
//            intent.putExtra("stringKey2", "voldemort");
            resultLauncher.launch(intent)
        }
    }

}