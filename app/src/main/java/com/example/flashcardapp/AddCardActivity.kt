package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        findViewById<ImageView>(R.id.cancel_button).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.saveBtn).setOnClickListener {
            val questionStr = findViewById<EditText>(R.id.QuestionTextField).text.toString()
            val answerStr = findViewById<EditText>(R.id.AnswerTextField).text.toString()

            val data = Intent() // create a new Intent, this is where we will put our data

            data.putExtra("questionKey", questionStr) // puts questionStr into the Intent, with a key

            data.putExtra("answerKey", answerStr) // puts answerStr into the Intent, with a key

            setResult(RESULT_OK, data) // set result code and bundle data for response

            finish() // closes this activity and pass data to the original activity that launched this activity
        }
    }
}