package com.example.composition.domain.entity

data class GameResult (
    val winResult: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions:Int,
    val gameSettings: GameSettings
)