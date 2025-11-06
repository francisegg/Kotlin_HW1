package com.example.hw_1106

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class Mora {
    SCISSOR, // 剪刀
    STONE,   // 石頭
    PAPER    // 布
}

enum class Winner {
    PLAYER,   // 玩家
    COMPUTER, // 電腦
    DRAW      // 平手
}

data class GameResult(
    val playerMora: Mora,
    val computerMora: Mora,
    val winner: Winner,
    val playerName: String
)

class GameViewModel : ViewModel() {

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult> = _gameResult

    private val _errorEvent = MutableLiveData<String>()
    val errorEvent: LiveData<String> = _errorEvent

    fun playGame(playerName: String, playerMora: Mora) {
        if (playerName.isBlank()) {
            _errorEvent.value = "請輸入玩家姓名"
            return
        }

        val computerMora = generateComputerMora()
        val winner = determineWinner(playerMora, computerMora)
        _gameResult.value = GameResult(playerMora, computerMora, winner, playerName)
    }

    private fun generateComputerMora(): Mora {
        return Mora.values().random()
    }

    private fun determineWinner(playerMora: Mora, computerMora: Mora): Winner {
        return when {
            playerMora == computerMora -> Winner.DRAW
            (playerMora == Mora.SCISSOR && computerMora == Mora.PAPER) ||
            (playerMora == Mora.STONE && computerMora == Mora.SCISSOR) ||
            (playerMora == Mora.PAPER && computerMora == Mora.STONE) -> Winner.PLAYER
            else -> Winner.COMPUTER
        }
    }
}
