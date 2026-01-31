package com.example.collegeschedule.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")

class FavoriteGroupsDataSource(private val context: Context) {
    private val favoriteGroupsKey = stringSetPreferencesKey("favorite_groups")

    // Получить список избранных групп
    val favoriteGroups: Flow<List<String>> = context.dataStore.data
        .map { preferences ->
            preferences[favoriteGroupsKey]?.toList() ?: emptyList()
        }

    // Добавить группу в избранное
    suspend fun addGroup(groupName: String) {
        context.dataStore.edit { preferences ->
            val currentSet = preferences[favoriteGroupsKey]?.toMutableSet() ?: mutableSetOf()
            currentSet.add(groupName)
            preferences[favoriteGroupsKey] = currentSet
        }
    }

    // Удалить группу из избранного
    suspend fun removeGroup(groupName: String) {
        context.dataStore.edit { preferences ->
            val currentSet = preferences[favoriteGroupsKey]?.toMutableSet() ?: mutableSetOf()
            currentSet.remove(groupName)
            preferences[favoriteGroupsKey] = currentSet
        }
    }
}