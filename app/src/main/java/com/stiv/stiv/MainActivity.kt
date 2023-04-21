package com.stiv.stiv

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.common_string
import kotlinx.android.synthetic.main.activity_main.computer_score
import kotlinx.android.synthetic.main.activity_main.enter_number
import kotlinx.android.synthetic.main.activity_main.number
import kotlinx.android.synthetic.main.activity_main.player_score
import kotlinx.android.synthetic.main.activity_main.winner_text

class MainActivity : AppCompatActivity() {

    var playerScore = 100
    var computerScore = 100
    var commonString = (1000000..9999999).shuffled().last().toString()
    var playerMove = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateData()

        enter_number.setOnClickListener {
            val str = number.text.toString()
            if (playerMove) {
                playerTurn()
            }
        }

    }

    fun playerTurn() {
        val arr = commonString.toCharArray().map { Character.getNumericValue(it) }.toIntArray()
        var num = number.text.toString().toInt()
        val indexToRemove = arr.indexOf(num)
        commonString = (arr.copyOfRange(0, indexToRemove) + arr.copyOfRange(
            indexToRemove + 1,
            arr.size
        )).joinToString("")
        playerScore -= num
        playerMove = false
        updateData()
        computerTurn()
    }

    fun computerTurn() {
        if (commonString == "") {
            gamewin()
            return
        }
        val arr = commonString.toCharArray().map { Character.getNumericValue(it) }.toIntArray()
        var num = getBestMove(arr, computerScore)
        val indexToRemove = arr.indexOf(num)
        commonString = (arr.copyOfRange(0, indexToRemove) + arr.copyOfRange(
            indexToRemove + 1,
            arr.size
        )).joinToString("")
        computerScore -= num
        playerMove = true
        updateData()
    }

    fun gamewin() {
        winner_text.visibility = View.VISIBLE
        if (playerScore > computerScore) {
            winner_text.text = "Player Wins"
        } else {
            winner_text.text = "Computer Wins"
        }
    }

    fun getBestMove(array: IntArray, score: Int): Int {

        var bestMove = -1
        var bestScore = Int.MIN_VALUE


        for (i in array.indices) {
            val newArray = array.copyOf()
            val move = newArray[i]
            newArray[i] = -1


            val scoreForMove = minimax(newArray, score + move, false, 2)


            if (scoreForMove > bestScore) {
                bestMove = move
                bestScore = scoreForMove
            }
        }

        return bestMove
    }

    fun minimax(array: IntArray, score: Int, isMaximizing: Boolean, depth: Int): Int {


        if (depth == 0) {
            return score
        }

        var bestScore = if (isMaximizing) Int.MIN_VALUE else Int.MAX_VALUE

        for (i in array.indices) {
            val newArray = array.copyOf()
            val move = newArray[i]
            newArray[i] = -1

            val newScore = score + if (isMaximizing) move else -move

            val scoreForMove = minimax(newArray, newScore, !isMaximizing, depth - 1)

            if (isMaximizing) {
                bestScore = maxOf(bestScore, scoreForMove)
            } else {
                bestScore = minOf(bestScore, scoreForMove)
            }
        }

        return bestScore
    }


    fun updateData() {
        player_score.text = playerScore.toString()
        computer_score.text = computerScore.toString()
        common_string.text = commonString
    }


}