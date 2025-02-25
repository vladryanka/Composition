package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    private lateinit var result: GameResult


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    private fun parseArguments() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            result = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        bindViews()
    }

    private fun bindViews() {
        with(binding) {
            percentTextView.text = String.format(
                getString(R.string.progress_answers),
                getPercentOfRightAnswers()
            )
            countTextView.text = String.format(
                getString(R.string.score_answers),
                result.gameSettings.minWinValue.toString()
            )
            smileImage.setImageResource(getSmileResId())
            requiredQuantityTextView.text = String.format(getString(R.string.required_score),
                result.countOfRightAnswers.toString()
                )
            requiredQuantityPercentTextView.text = String.format(
                getString(R.string.required_percentage),
                result.gameSettings.minPresentOfRightAnswers.toString()
            )
        }
    }
    private fun getPercentOfRightAnswers() = with(result){
        if (countOfQuestions == 0)  {0}
        else{
            ((countOfRightAnswers/countOfQuestions.toDouble())*100).toInt()
        }
    }

    private fun getSmileResId(): Int {
        return if (result.winResult) R.drawable.smile else R.drawable.sad_smile
    }

    private fun setOnClickListeners() {
        binding.button.setOnClickListener {
            retryGame()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            })
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {
        private const val KEY_GAME_RESULT = "result"

        @JvmStatic
        fun newInstance(result: GameResult) =
            GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, result)
                }
            }
    }
}