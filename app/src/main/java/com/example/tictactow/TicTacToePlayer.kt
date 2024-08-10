package com.example.tictactow

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

enum class Player {
    One, Two,None
}

data class Play(val turn: Player)

data class Game(val turn: Player?, val isWon: Boolean)

@Composable
fun TicTacToeGame() {

    val (gameData,setGameData) = remember {
        mutableStateOf( Array(3) { Array<Play?>(3) { null } })
    }
    val (turn, setTurn) = remember {
        mutableStateOf(Player.One)
    }

    val (isGameWon, setIsGameWon) = remember {
        mutableStateOf<Game>(Game(null, false))
    }

    fun resetGameData() {
        setGameData(Array(3) { Array<Play?>(3) { null }})
        setIsGameWon(Game(null, false))
    }

    fun updateGameData(i: Int,j: Int) {
        gameData[i][j] = Play(turn);

        setGameData(gameData)

        if (turn == Player.One)
            setTurn(Player.Two)
        else
            setTurn(Player.One)
    }

    fun handleWin(): Player? {

        println(gameData)

        for(i in 0..2) {
            var turnX : Player? = gameData[i][0]?.turn;
            var turnY : Player? = gameData[0][i]?.turn;

            var isWon: Player? = null;

            for (j in 1..2) {
                if (gameData[i][j]?.turn == turnX && turnX != null) {
                    if(j == 2)
                        isWon = turnX;
                }else {
                    turnX = null;
                }

                if(gameData[j][i]?.turn == turnY && turnY != null) {
                    if(j == 2)
                        isWon = turnY
                } else {
                    turnY = null;
                }
            }

            if(isWon != null) {
                return isWon;
            }
        }
        val turnX = gameData[0][0]?.turn == gameData[1][1]?.turn && gameData[1][1]?.turn == gameData[2][2]?.turn
        val turnY = gameData[0][2]?.turn == gameData[1][1]?.turn && gameData[1][1]?.turn == gameData[2][0]?.turn

        if(turnX || turnY) {
            return gameData[1][1]?.turn;
        }

        return null;
    }

    DisposableEffect(key1= turn, key2= gameData) {

        onDispose {
            val winner = handleWin()

            if(winner != null) {
                setIsGameWon(Game(winner, true));
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for(i in 1..3) {
            Row(
                modifier = Modifier
                    .weight(1F)
                    .padding(10.dp)
            ) {
                for(item in 1..3) {
                    Row(
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(10.dp)
                    ) {
                        var color = Color.Gray.copy(0.5f)

                        if(gameData[i-1][item-1]?.turn == Player.One)
                            color = Color.Green.copy(0.5f)
                        else if(gameData[i-1][item-1]?.turn == Player.Two)
                            color = Color.Red.copy(0.5f)


                        Surface(
                            onClick = {
                                updateGameData(i-1,item-1)
                            },
                            modifier = Modifier.weight(1F)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1F)
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )
                                    .background(color),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,

                            ) {
                                if(gameData[i-1][item-1] != null) {
                                    if(gameData[i-1][item-1]?.turn == Player.One)
                                        Icon(Icons.Rounded.Done, "done", modifier = Modifier.scale(3f))
                                    else
                                        Icon(
                                            Icons.Rounded.Close,
                                            "close",
                                            modifier = Modifier.scale(3f)
                                        )
                                }
                            }
                        }
                    }
                }
            }

        }
        Column (
            modifier = Modifier.weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Text(text = "Player $turn's Turn")
            if(isGameWon.isWon) {
                Text(text = "Game Ended! Player ${isGameWon.turn} won the game")
                Button(onClick = { resetGameData() }) {
                    Text(text = "Restart Game")
                }
            }
        }
    }
}