package com.shdwraze.team_testtask.ui

import androidx.lifecycle.ViewModel
import com.shdwraze.team_testtask.data.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TeamViewModel : ViewModel() {

    private val _players = MutableStateFlow(mutableListOf<Player>())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    fun addPlayer(name: String, surname: String, yearOfBirth: Int) {
        val id = _players.value.size + 1
        _players.value.add(Player(id, name, surname, yearOfBirth))
    }

    fun deletePlayer(player: Player) {
        val updatedPlayers = _players.value.toMutableList()
        updatedPlayers.remove(player)
        _players.value = updatedPlayers
    }
}