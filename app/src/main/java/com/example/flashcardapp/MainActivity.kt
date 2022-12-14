package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.snackbar.Snackbar

class MainActivity: AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()
    var currentCardDisplayedIndex = 0
    val addStr = "Add a card!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()
        if (allFlashcards.size > 0) {
            findViewById<TextView>(R.id.flashcard_question).text = allFlashcards[0].question
            findViewById<TextView>(R.id.flashcard_answer).text = allFlashcards[0].answer
        }

        findViewById<ImageView>(R.id.delBtn).setOnClickListener {
            val flashcardQuestionToDelete = findViewById<TextView>(R.id.flashcard_question).text.toString()
            flashcardDatabase.deleteCard(flashcardQuestionToDelete)
            allFlashcards = flashcardDatabase.getAllCards().toMutableList()
            currentCardDisplayedIndex = currentCardDisplayedIndex++
        }
        if (allFlashcards.size == 0) {
            findViewById<TextView>(R.id.flashcard_answer).text = addStr
        }

       val answerSideView = findViewById<TextView>(R.id.flashcard_answer)
        val questionSideView = findViewById<TextView>(R.id.flashcard_question)

        findViewById<TextView>(R.id.flashcard_question).setOnClickListener {
            findViewById<TextView>(R.id.flashcard_answer).visibility = TextView.VISIBLE
            findViewById<TextView>(R.id.flashcard_question).visibility = TextView.INVISIBLE
            val cx = answerSideView.width / 2
            val cy = answerSideView.height / 2

            // get the final radius for the clipping circle
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            // create the animator for this view (the start radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius)

            // hide the question and show the answer to prepare for playing the animation!
            questionSideView.visibility = TextView.INVISIBLE
            answerSideView.visibility = TextView.VISIBLE

            anim.duration = 3000
            anim.start()
        }
        findViewById<TextView>(R.id.flashcard_answer).setOnClickListener {
            findViewById<TextView>(R.id.flashcard_answer).visibility = TextView.INVISIBLE
            findViewById<TextView>(R.id.flashcard_question).visibility = TextView.VISIBLE

        }

        findViewById<ImageView>(R.id.nxtBtn).setOnClickListener {
            val leftOutAnim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.left_out)
            val rightInAnim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.right_in)
            leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // this method is called when the animation first starts
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // this method is called when the animation is finished playing
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // we don't need to worry about this method
                }
            })

            if (allFlashcards.size == 0) {
                return@setOnClickListener
            }
            currentCardDisplayedIndex++
            // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
            if(currentCardDisplayedIndex >= allFlashcards.size) {
                Snackbar.make(
                    findViewById<TextView>(R.id.flashcard_question), // This should be the TextView for displaying your flashcard question
                    "You've reached the end of the cards, going back to start.",
                    Snackbar.LENGTH_SHORT)
                    .show()
                currentCardDisplayedIndex = 0
            }
            // set the question and answer TextViews with data from the database
            allFlashcards = flashcardDatabase.getAllCards().toMutableList()
            val (question, answer) = allFlashcards[currentCardDisplayedIndex]

            findViewById<TextView>(R.id.flashcard_question).startAnimation(leftOutAnim)
            findViewById<TextView>(R.id.flashcard_question).startAnimation(rightInAnim)

            findViewById<TextView>(R.id.flashcard_answer).text = answer
            findViewById<TextView>(R.id.flashcard_question).text = question
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (data != null) { // Check that we have data returned
                val questionStr = data.getStringExtra("questionKey")
                val answerStr = data.getStringExtra("answerKey")

                findViewById<TextView>(R.id.flashcard_question).text = questionStr
                findViewById<TextView>(R.id.flashcard_answer).text = answerStr

                flashcardDatabase.insertCard(Flashcard(questionStr.toString(), answerStr.toString()))
                allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                // Log the value of the strings for easier debugging
                Log.i("MainActivity", "question: $questionStr")
                Log.i("MainActivity", "answer: $answerStr")
            } else {
                Log.i("MainActivity", "Returned null data from AddCardActivity")
            }


        }

        findViewById<ImageView>(R.id.addBtn).setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }
    }

}