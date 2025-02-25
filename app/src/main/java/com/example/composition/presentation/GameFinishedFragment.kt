package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding

class GameFinishedFragment : Fragment() {

    private val args by navArgs<GameFinishedFragmentArgs>()

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

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
                args.result.gameSettings.minWinValue.toString()
            )
            smileImage.setImageResource(getSmileResId())
            requiredQuantityTextView.text = String.format(
                getString(R.string.required_score),
                args.result.countOfRightAnswers.toString()
            )
            requiredQuantityPercentTextView.text = String.format(
                getString(R.string.required_percentage),
                args.result.gameSettings.minPresentOfRightAnswers.toString()
            )
        }
    }

    private fun getPercentOfRightAnswers() = with(args.result) {
        if (countOfQuestions == 0) {
            0
        } else {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
        }
    }

    private fun getSmileResId(): Int {
        return if (args.result.winResult) R.drawable.smile else R.drawable.sad_smile
    }

    private fun setOnClickListeners() {
        binding.button.setOnClickListener {
            retryGame()
        }
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }
}