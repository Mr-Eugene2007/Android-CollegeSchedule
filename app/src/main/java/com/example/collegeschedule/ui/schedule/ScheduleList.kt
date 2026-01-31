package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto

@Composable
fun ScheduleList(
    data: List<ScheduleByDateDto>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("Нет занятий на этой неделе")
        }
    } else {
        // Простой вертикальный скролл
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Отступ сверху
            Spacer(modifier = Modifier.height(8.dp))

            data.forEach { day ->
                SimpleDayCard(day)
            }

            // Отступ снизу для удобного скроллинга
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SimpleDayCard(daySchedule: ScheduleByDateDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Заголовок
            Text(
                text = "${daySchedule.weekday ?: ""} (${daySchedule.lessonDate ?: ""})",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Занятия
            daySchedule.lessons.forEach { lesson ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text("Пара ${lesson.lessonNumber} (${lesson.time})")
                    lesson.groupParts?.forEach { (part, info) ->
                        if (info != null) {
                            Text("  $part: ${info.subject}")
                            Text("    ${info.teacher}")
                            Text("    ${info.building}, ${info.classroom}")
                        }
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}