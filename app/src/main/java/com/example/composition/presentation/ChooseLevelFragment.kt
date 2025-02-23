package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.composition.R
import com.example.composition.databinding.FragmentChooseLevelBinding
import com.example.composition.domain.entity.Level

class ChooseLevelFragment : Fragment() {

    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.easyButton.setOnClickListener{
            launchGameFragment(Level.EASY)
        }
        binding.hardButton.setOnClickListener{
            launchGameFragment(Level.HARD)
        }
        binding.mediumButton.setOnClickListener{
            launchGameFragment(Level.NORMAL)
        }
        binding.testButton.setOnClickListener{
            launchGameFragment(Level.TEST)
        }
    }

    private fun launchGameFragment(level: Level){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main, GameFragment.newInstance(level))
            .addToBackStack(GameFragment.NAME)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val NAME = "ChooseLevelFragment"
        fun newInstance() =
            ChooseLevelFragment()
    }
}