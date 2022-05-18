package com.example.tictactoe

import GameImp
import TicTacToe
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import toSign

class MainActivity : AppCompatActivity() {  //главный класс приложения
    private val game: TicTacToe = GameImp() //переменная игры

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {    //функция создания активити

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewAnimator = findViewById<ViewAnimator>(R.id.viewAnimator)   //переменная для переключения анимации поля
        val inAnimation: Animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in) //анимация в одну сторону и в другую
        val outAnimation: Animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        viewAnimator.inAnimation = inAnimation
        inAnimation.duration = 1000     //длительность анимаций
        viewAnimator.outAnimation = outAnimation
        outAnimation.duration = 2000
        val restartButton = findViewById<Button>(R.id.RestartBTN)   //кнопка рестарта игры
        val tWinner = findViewById<TextView>(R.id.tWinner)      //текстовые поля для вывода результата
        val tWinner2 = findViewById<TextView>(R.id.tWinner2)
        restartButton.setBackgroundColor(Color.rgb(103, 58, 183))   //установка цвета кнопки и текста на ней
        restartButton.setTextColor(Color.WHITE)
        tWinner.textSize = 50F      //размеры текста
        tWinner2.textSize = 170F

        val fieldRvMA = findViewById<RecyclerView>(R.id.fieldRv)    //переменная поля игры

        fieldRvMA.apply {
            layoutManager = GridLayoutManager(context, game.field.size) //установка табличного Layout для recyclerview
            adapter = FieldAdapter(game.field) { row, col ->
                if (game.actionPlayer(row, col)) {
                    adapter?.notifyDataSetChanged()    //дает информацию полю о изменении
                    if (game.isFinished) {  //если игра закончена выводит результат
                        val winnerSign = game.winner?.let { "${it.toSign()}!" } ?: "XO"
                        val winnerText = game.winner?.let { "The winner is " } ?: "Tie!"
                        for (i in 0 until game.field.size * game.field.size) fieldRvMA[i].isClickable = false
                        viewAnimator.showNext() //переключение на результат игры
                        tWinner.text = winnerText
                        tWinner2.text = winnerSign
                        adapter?.notifyDataSetChanged() //дает информацию полю о изменении
                    }
                }
            }  //установка adapter

            restartButton.setOnClickListener {
                if (game.isFinished) viewAnimator.showNext()   //если игра закончена, то переключить поле, если нет, то просто обновить
                game.restart()
                for (i in 0 until game.field.size * game.field.size) fieldRvMA[i].isClickable = true
                adapter?.notifyDataSetChanged() //дает информацию полю о изменении
            }
        }
    }
}