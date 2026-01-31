package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.ui.favorites.FavoritesViewModel
import com.example.collegeschedule.utils.getWeekDateRange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    groupName: String = "ИС-12",
    modifier: Modifier = Modifier
) {
    val favoritesViewModel: FavoritesViewModel = viewModel()
    val favoriteGroups by favoritesViewModel.favoriteGroups.collectAsState()

    var schedule by remember {
        mutableStateOf<List<ScheduleByDateDto>>(emptyList())
    }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var isFavorite by remember { mutableStateOf(false) }

    // Проверяем, в избранном ли группа
    LaunchedEffect(groupName, favoriteGroups) {
        isFavorite = groupName in favoriteGroups
    }

    // Загружаем расписание
    LaunchedEffect(groupName) {
        val (start, end) = getWeekDateRange()
        try {
            schedule = RetrofitInstance.api.getSchedule(
                groupName,
                start,
                end
            )
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Ошибка: $error")
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Расписание: $groupName") },
                    actions = {
                        IconButton(
                            onClick = {
                                if (isFavorite) {
                                    favoritesViewModel.removeGroup(groupName)
                                } else {
                                    favoritesViewModel.addGroup(groupName)
                                }
                                isFavorite = !isFavorite
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Избранное"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            // Используем Box для правильного применения padding
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                ScheduleList(
                    data = schedule
                )
            }
        }
    }
}