package com.example.collegeschedule.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegeschedule.data.local.FavoriteGroupsDataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val dataSource: FavoriteGroupsDataSource
) : ViewModel() {

    val favoriteGroups: StateFlow<List<String>> = dataSource.favoriteGroups
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addGroup(groupName: String) {
        viewModelScope.launch {
            dataSource.addGroup(groupName)
        }
    }

    fun removeGroup(groupName: String) {
        viewModelScope.launch {
            dataSource.removeGroup(groupName)
        }
    }

    // Метод для проверки, является ли группа избранной
    suspend fun isFavorite(groupName: String): Boolean {
        val groups = favoriteGroups.value
        return groupName in groups
    }
}