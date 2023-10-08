package com.shdwraze.team_testtask

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shdwraze.team_testtask.data.Player
import com.shdwraze.team_testtask.ui.TeamViewModel
import com.shdwraze.team_testtask.ui.theme.TeamTestTaskTheme

enum class TeamScreen(@StringRes val title: Int) {
    Main(title = R.string.app_name), NewPlayer(title = R.string.add_new_player)
}

@ExperimentalMaterial3Api
@Composable
fun TeamAppBar(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    canNavigateBack: Boolean,
    currentScreen: TeamScreen
) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(text = stringResource(id = currentScreen.title))
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamApp(
    teamViewModel: TeamViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = TeamScreen.valueOf(
        backStackEntry?.destination?.route ?: TeamScreen.Main.name
    )
    val players by teamViewModel.players.collectAsState()

    Scaffold(
        topBar = {
            TeamAppBar(
                navigateUp = { navController.navigateUp() },
                canNavigateBack = navController.previousBackStackEntry != null,
                currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TeamScreen.Main.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = TeamScreen.Main.name) {
                TeamScreen(
                    navController = navController,
                    players = players,
                    onDeleteClicked = teamViewModel::deletePlayer
                )
            }
            composable(route = TeamScreen.NewPlayer.name) {
                NewPlayerScreen(
                    onCreateNewPlayerClicked = teamViewModel::addPlayer,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun TeamScreen(
    players: List<Player>,
    navController: NavHostController,
    onDeleteClicked: (Player) -> Unit
) {
    val onButtonClicked = {
        navController.navigate(TeamScreen.NewPlayer.name)
    }

    if (players.isEmpty()) {
        ContainerButton(onButtonClicked = onButtonClicked)
    } else {
        LazyColumn {
            items(players) { player ->
                PlayerCard(
                    player = player,
                    onDeleteClicked = onDeleteClicked
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            ButtonPlus(
                onButtonClicked = onButtonClicked,
                buttonSize = 70,
                cornerShape = RoundedCornerShape(10.dp)
            )
        }
    }
}

@Composable
fun ContainerButton(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val stroke = Stroke(
            width = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 30f), 0f)
        )

        Column(modifier = Modifier
            .size(250.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color.Gray, style = stroke,
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            }
            .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonPlus(onButtonClicked = onButtonClicked)
            Text(
                text = "Додати гравця",
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun ButtonPlus(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit,
    buttonSize: Int = 100,
    cornerShape: RoundedCornerShape = RoundedCornerShape(30.dp)
) {
    Button(
        onClick = { onButtonClicked() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.hsl(
                131f, 0.71f, 0.39f, 1f
            )
        ),
        modifier = modifier.size(buttonSize.dp),
        shape = cornerShape
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun PlayerCard(
    modifier: Modifier = Modifier,
    player: Player,
    onDeleteClicked: (Player) -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .size(300.dp, 100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Text(text = "${player.id}")
            Image(
                painter = painterResource(id = R.drawable.image1),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${player.surname} ${player.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = "ID: ${player.id}")
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onDeleteClicked(player) }) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    TeamTestTaskTheme {
        TeamApp()
    }
}