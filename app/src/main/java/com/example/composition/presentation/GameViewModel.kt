package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var settings: GameSettings
    private lateinit var level: Level
    private val repository = GameRepositoryImpl
    private val generateQuestion = GenerateQuestionUseCase(repository)
    private val getGameSettings = GetGameSettingsUseCase(repository)

    private val context = application

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String> get() = _formattedTime

    private var timer: CountDownTimer? = null

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question> get() = _question

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String> get() = _progressAnswers

    private val _percentProgress = MutableLiveData<Int>()
    val percentProgress: LiveData<Int> get() = _percentProgress

    private val _enoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean> get() = _enoughCountOfRightAnswers

    private val _enoughPercentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean> get() = _enoughPercentOfRightAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int> get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult> get() = _gameResult

    private var countOfRightAnswers = 0
    private var _countOfQuestions = 0

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentProgress.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countOfRightAnswers.toString(),
            settings.minWinValue.toString()
        )
        _enoughCountOfRightAnswers.value = countOfRightAnswers >= settings.minWinValue
        _enoughPercentOfRightAnswers.value = percent >= settings.minPresentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        return (countOfRightAnswers / _countOfQuestions.toDouble() * 100).toInt()
    }

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateNewQuestion()
    }

    fun chooseAnswer(answer: Int) {
        checkAnswer(answer)
        generateNewQuestion()
    }

    private fun checkAnswer(answer: Int) {
        val rightAnswer = _question.value?.rightAnswer
        if (answer == rightAnswer) countOfRightAnswers++
        _countOfQuestions++
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        settings = repository.getGameSettings(level)
        _minPercent.value = settings.minPresentOfRightAnswers
    }

    private fun generateNewQuestion() {
        _question.value = generateQuestion(settings.maxSumValue)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            settings.gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished % MILLIS_IN_SECONDS
                val minutes = seconds / SECONDS_IN_MINUTES
                val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
                _formattedTime.value = String.format("%02d:%02d", minutes, leftSeconds)
            }

            override fun onFinish() {
                finishGame()
            }

        }
        timer?.start()
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswers,
            _countOfQuestions,
            settings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}