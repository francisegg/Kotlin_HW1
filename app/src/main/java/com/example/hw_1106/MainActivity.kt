package com.example.hw_1106

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hw_1106.databinding.ActivityMainBinding
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnMora.setOnClickListener {
            val playerName = binding.edName.text.toString()
            val selectedMora = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.btnScissor -> Mora.SCISSOR
                R.id.btnStone -> Mora.STONE
                else -> Mora.PAPER
            }
            viewModel.playGame(playerName, selectedMora)
        }
    }

    private fun observeViewModel() {
        viewModel.gameResult.observe(this) { result ->
            binding.tvName.text = getString(R.string.player_name, result.playerName)
            binding.tvMyMora.text = getString(R.string.my_mora, getMoraString(result.playerMora))
            binding.tvTargetMora.text = getString(R.string.target_mora, getMoraString(result.computerMora))

            when (result.winner) {
                Winner.PLAYER -> {
                    binding.tvWinner.text = getString(R.string.winner_player, result.playerName)
                    binding.tvText.text = getString(R.string.win_message)
                }
                Winner.COMPUTER -> {
                    binding.tvWinner.text = getString(R.string.winner_computer)
                    binding.tvText.text = getString(R.string.lose_message)
                }
                Winner.DRAW -> {
                    binding.tvWinner.text = getString(R.string.winner_draw)
                    binding.tvText.text = getString(R.string.draw_message)
                }
            }
        }

        viewModel.errorEvent.observe(this) { errorMessage ->
            binding.tvText.text = errorMessage
        }
    }

    private fun getMoraString(mora: Mora): String {
        return when (mora) {
            Mora.SCISSOR -> getString(R.string.scissor)
            Mora.STONE -> getString(R.string.stone)
            Mora.PAPER -> getString(R.string.paper)
        }
    }
}
