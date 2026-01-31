package com.example.collegeschedule

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.collegeschedule.data.local.FavoriteGroupsDataSource
import com.example.collegeschedule.ui.favorites.FavoritesScreen
import com.example.collegeschedule.ui.favorites.FavoritesViewModel
import com.example.collegeschedule.ui.schedule.ScheduleScreen
import com.example.collegeschedule.ui.theme.CollegeScheduleTheme
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollegeScheduleTheme {
                CollegeScheduleApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun CollegeScheduleApp() {
    val context = LocalContext.current

    // ViewModel для избранного
    val favoritesViewModel: FavoritesViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                val dataSource = FavoriteGroupsDataSource(context)
                FavoritesViewModel(dataSource)
            }
        }
    )

    var currentDestination by rememberSaveable {
        mutableStateOf(AppDestinations.HOME)
    }
    var selectedGroup by rememberSaveable {
        mutableStateOf("ИС-12")
    }

    // При клике на группу в избранном открываем её расписание
    val onGroupClick: (String) -> Unit = { groupName ->
        selectedGroup = groupName
        currentDestination = AppDestinations.HOME
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            destination.icon,
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            when (currentDestination) {
                AppDestinations.HOME -> {
                    ScheduleScreen(
                        groupName = selectedGroup,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                AppDestinations.FAVORITES -> {
                    FavoritesScreen(
                        onGroupClick = onGroupClick,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                AppDestinations.PROFILE -> {
                    Text(
                        text = "Профиль студента",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Главная", Icons.Default.Home),
    FAVORITES("Избранное", Icons.Default.Favorite),
    PROFILE("Профиль", Icons.Default.AccountBox),
}