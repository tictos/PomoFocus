package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskItem
import com.example.ui.theme.AmberGoldDark
import com.example.ui.theme.AmberGoldLight
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.EmeraldBreak
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.RedDanger
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.PomodoroViewModel

enum class TaskFilter(val title: String) {
    ALL("Toutes"),
    IN_PROGRESS("En cours"),
    PRIORITY("Prioritaires"),
    COMPLETED("Terminées")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: PomodoroViewModel,
    modifier: Modifier = Modifier,
    onStartFocusOnTask: () -> Unit = {}
) {
    val tasks by viewModel.tasks.collectAsState()
    val activeTaskId by viewModel.activeTaskId.collectAsState()

    var selectedFilter by remember { mutableStateOf(TaskFilter.ALL) }
    var showAddDialog by remember { mutableStateOf(false) }

    val activeTask = remember(tasks, activeTaskId) {
        tasks.find { it.id == activeTaskId } ?: tasks.firstOrNull { !it.isCompleted }
    }

    val filteredTasks = remember(tasks, selectedFilter) {
        when (selectedFilter) {
            TaskFilter.ALL -> tasks
            TaskFilter.IN_PROGRESS -> tasks.filter { !it.isCompleted }
            TaskFilter.PRIORITY -> tasks.filter { !it.isCompleted && it.estimatedPomodoros >= 3 }
            TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Top App Bar: ChronoGlass / Tasks Brand Logo & Actions
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Brand Logo + Name & Subtitle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF191C28),
                            border = BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.5f)),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.HourglassBottom,
                                    contentDescription = "Logo ChronoGlass",
                                    tint = AmberGoldPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "ChronoGlass",
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.3.sp
                            )
                            Text(
                                text = "Tasks",
                                color = TextMuted,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Right Actions (Announcement + Profile)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF191C28),
                            border = BorderStroke(1.dp, Color(0xFF262B3E)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            IconButton(onClick = { /* Announcements */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Campaign,
                                    contentDescription = "Annonces",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF191C28),
                            border = BorderStroke(1.dp, Color(0xFF262B3E)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            IconButton(onClick = { /* Profile */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = "Profil",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 2. Screen Title & Filters
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tâches & Objectifs",
                            color = TextPrimary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF1A1D2B),
                            border = BorderStroke(1.dp, Color(0xFF262B3E)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            IconButton(onClick = { /* Filter menu */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Tune,
                                    contentDescription = "Filtres",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = "Organisez vos sessions de deep work et suivez vos sabliers.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    // Filter Chips Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TaskFilter.entries.forEach { filter ->
                            val isSelected = selectedFilter == filter
                            val count = when (filter) {
                                TaskFilter.ALL -> tasks.size
                                TaskFilter.IN_PROGRESS -> tasks.count { !it.isCompleted }
                                TaskFilter.PRIORITY -> tasks.count { !it.isCompleted && it.estimatedPomodoros >= 3 }
                                TaskFilter.COMPLETED -> tasks.count { it.isCompleted }
                            }
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = if (isSelected) AmberGoldPrimary else Color(0xFF191B28),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) AmberGoldPrimary else Color(0xFF24283A)
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable { selectedFilter = filter }
                            ) {
                                Text(
                                    text = "${filter.title} ($count)",
                                    color = if (isSelected) Color(0xFF0F1016) else TextSecondary,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 3. Hero Active Task Card (Or Empty State if no tasks exist)
            if (activeTask != null) {
                item {
                    val estimatedMinutes = activeTask.estimatedPomodoros * 25
                    val hours = estimatedMinutes / 60
                    val mins = estimatedMinutes % 60
                    val timeEstimateStr = if (hours > 0) "${hours}h ${mins}m estimé" else "${mins}m estimé"

                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = Color(0xFF161824),
                        border = BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.35f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(22.dp),
                                spotColor = AmberGoldPrimary.copy(alpha = 0.3f)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            // Top Badges Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF222636),
                                    border = BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(AmberGoldPrimary)
                                        )
                                        Text(
                                            text = if (activeTask.id == activeTaskId) "FOCUS EN COURS" else "MISSION RECOMMANDÉE",
                                            color = AmberGoldLight,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.8.sp
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = timeEstimateStr,
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Task Title & Category
                            Text(
                                text = activeTask.title,
                                color = TextPrimary,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Catégorie : ${activeTask.category}",
                                color = TextSecondary,
                                fontSize = 13.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Rhythm & Interval Progress
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Progression des sabliers",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${activeTask.completedPomodoros} / ${activeTask.estimatedPomodoros} sabliers",
                                    color = AmberGoldLight,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Dynamic Pill Segments
                            val totalPills = activeTask.estimatedPomodoros.coerceAtLeast(1).coerceAtMost(10)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                for (i in 0 until totalPills) {
                                    val isFilled = i < activeTask.completedPomodoros
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(7.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (isFilled) AmberGoldPrimary else Color(0xFF262B3E))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Large CTA Button: "▶ Lancer le Timer"
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = AmberGoldPrimary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = AmberGoldPrimary)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable {
                                        viewModel.setActiveTask(activeTask.id)
                                        onStartFocusOnTask()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color(0xFF0F1016),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Focaliser sur cette tâche",
                                        color = Color(0xFF0F1016),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                        }
                    }
                }
            } else if (tasks.isEmpty()) {
                // Empty State Card
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF161824),
                        border = BorderStroke(1.dp, Color(0xFF24283A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF1E2130),
                                border = BorderStroke(1.dp, Color(0xFF2C3246)),
                                modifier = Modifier.size(54.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.HourglassTop,
                                        contentDescription = null,
                                        tint = AmberGoldPrimary,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }

                            Text(
                                text = "Aucune tâche enregistrée",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Ajoutez vos objectifs pour structurer vos sessions de travail en sabliers de 25 minutes.",
                                color = TextSecondary,
                                fontSize = 13.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 18.sp
                            )

                            Button(
                                onClick = { showAddDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AmberGoldPrimary,
                                    contentColor = Color(0xFF0F1016)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Créer une première mission", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 4. Section: "Missions" Dynamic List
            if (filteredTasks.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Missions",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${filteredTasks.size} ${if (filteredTasks.size > 1) "missions" else "mission"}",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }

                // Dynamic Database Tasks
                items(filteredTasks, key = { it.id }) { task ->
                    MissionTaskCard(
                        title = task.title,
                        category = task.category,
                        progressText = "⏳ ${task.completedPomodoros}/${task.estimatedPomodoros} sabliers",
                        isCompleted = task.isCompleted,
                        icon = if (task.isCompleted) Icons.Outlined.CheckCircle else Icons.Default.HourglassTop,
                        iconTint = if (task.isCompleted) AmberGoldLight else TextSecondary,
                        hasActiveDot = task.id == activeTaskId,
                        showMoreMenu = true,
                        showPlayButton = !task.isCompleted,
                        onPlay = {
                            viewModel.setActiveTask(task.id)
                            onStartFocusOnTask()
                        },
                        onDelete = { viewModel.deleteTask(task) }
                    )
                }
            }

            // 5. Bottom "＋ Nouvelle tâche de focus" CTA Button
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF191C28),
                    border = BorderStroke(1.dp, Color(0xFF282D42)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { showAddDialog = true }
                        .testTag("add_task_button")
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = AmberGoldPrimary,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color(0xFF0F1016),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Nouvelle tâche de focus",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Add Task Dialog
    if (showAddDialog) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Travail") }
        var estimatedHours by remember { mutableIntStateOf(4) }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = Color(0xFF161824),
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    text = "Nouvelle mission de focus",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Titre de la tâche") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmberGoldPrimary,
                            unfocusedBorderColor = Color(0xFF282C40),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description courte (optionnel)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmberGoldPrimary,
                            unfocusedBorderColor = Color(0xFF282C40),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Estimation : $estimatedHours sabliers (${estimatedHours * 25} min)",
                        color = AmberGoldLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            viewModel.addTask(
                                title = title.trim(),
                                estimatedPomodoros = estimatedHours,
                                category = category
                            )
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmberGoldPrimary,
                        contentColor = Color(0xFF0F1016)
                    )
                ) {
                    Text("Créer la mission", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Annuler", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun MissionTaskCard(
    title: String,
    category: String,
    progressText: String,
    isCompleted: Boolean,
    icon: ImageVector,
    iconTint: Color,
    hasActiveDot: Boolean = false,
    showMoreMenu: Boolean = false,
    showPlayButton: Boolean = false,
    onPlay: () -> Unit = {},
    onDelete: (() -> Unit)? = null
) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF161824),
        border = BorderStroke(1.dp, Color(0xFF222638)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onPlay() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Leading Icon in circular surface
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF202332),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Title + Tags
                Column {
                    Text(
                        text = title,
                        color = if (isCompleted) TextMuted else TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Category Pill
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF222636)
                        ) {
                            Text(
                                text = category,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = progressText,
                            color = TextSecondary,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }

            // Trailing Actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (hasActiveDot) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(AmberGoldPrimary)
                    )
                }

                if (showPlayButton) {
                    IconButton(
                        onClick = onPlay,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PlayCircle,
                            contentDescription = "Lancer",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                if (showMoreMenu) {
                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Lancer le timer") },
                                onClick = {
                                    showMenu = false
                                    onPlay()
                                }
                            )
                            if (onDelete != null) {
                                DropdownMenuItem(
                                    text = { Text("Supprimer", color = RedDanger) },
                                    onClick = {
                                        showMenu = false
                                        onDelete()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
