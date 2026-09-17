package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.Waves
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.sound.AmbientSound
import com.example.ui.theme.AmberGoldDark
import com.example.ui.theme.AmberGoldLight
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.EmeraldBreak
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.RedDanger
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.PomodoroViewModel
import com.example.ui.viewmodel.SyncState

@Composable
fun SettingsScreen(
    viewModel: PomodoroViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsState()
    val syncState by viewModel.syncState.collectAsState()

    var autoStartBreaks by remember { mutableStateOf(true) }
    var hapticFeedback by remember { mutableStateOf(settings.vibrationEnabled) }
    var doNotDisturb by remember { mutableStateOf(true) }
    var isPreviewingBinaural by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Top App Bar: ChronoGlass / Settings Brand Logo & Actions
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Brand Logo + Name & Subtitle
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
                                    contentDescription = "ChronoGlass Logo",
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
                                text = "Settings",
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
                            IconButton(onClick = { }) {
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
                            IconButton(onClick = { }) {
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

            // 2. Screen Header & Version Badge
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Paramètres",
                            color = TextPrimary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // "● v2.4 Pro" Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF222534),
                            border = BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.25f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(AmberGoldPrimary)
                                )
                                Text(
                                    text = "v2.4 Pro",
                                    color = AmberGoldLight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Text(
                        text = "Personnalisez vos cycles et votre sanctuaire de focus.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            // 3. Profile & Pro Subscription Card
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF161824),
                    border = BorderStroke(1.dp, Color(0xFF222638)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // User Profile Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Avatar Image
                                Image(
                                    painter = painterResource(id = R.drawable.user_avatar_pro),
                                    contentDescription = "Avatar Alexandre V.",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .border(1.5.dp, AmberGoldPrimary.copy(alpha = 0.6f), CircleShape)
                                )

                                Column {
                                    Text(
                                        text = "Alexandre V.",
                                        color = TextPrimary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Row(
                                        modifier = Modifier.clickable { viewModel.triggerCloudSync() },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        when (syncState) {
                                            is SyncState.Syncing -> {
                                                CircularProgressIndicator(
                                                    strokeWidth = 1.5.dp,
                                                    color = AmberGoldPrimary,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Text(
                                                    text = "Synchro en cours...",
                                                    color = AmberGoldLight,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                            is SyncState.Success -> {
                                                Icon(
                                                    imageVector = Icons.Default.CloudDone,
                                                    contentDescription = null,
                                                    tint = EmeraldBreak,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Text(
                                                    text = "Cloud synchronisé",
                                                    color = TextSecondary,
                                                    fontSize = 12.sp
                                                )
                                            }
                                            is SyncState.Error -> {
                                                Icon(
                                                    imageVector = Icons.Default.Sync,
                                                    contentDescription = null,
                                                    tint = RedDanger,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Text(
                                                    text = "Réessayer la synchro",
                                                    color = RedDanger,
                                                    fontSize = 12.sp
                                                )
                                            }
                                            SyncState.Idle -> {
                                                Icon(
                                                    imageVector = Icons.Default.CloudDone,
                                                    contentDescription = null,
                                                    tint = TextMuted,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Text(
                                                    text = "Cloud synchronisé",
                                                    color = TextSecondary,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // "PRO" Badge
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = AmberGoldPrimary
                            ) {
                                Text(
                                    text = "PRO",
                                    color = Color(0xFF0F1016),
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                                )
                            }
                        }

                        // Membership Banner Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E2130))
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "ABONNEMENT",
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "Membre ChronoGlass Pro",
                                    color = AmberGoldLight,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF262B3E),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Section: "Cycles & Minuteur Pomodoro"
            item {
                SettingsSectionHeader(
                    icon = Icons.Outlined.Timer,
                    title = "Cycles & Minuteur Pomodoro"
                )
            }

            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF161824),
                    border = BorderStroke(1.dp, Color(0xFF222638)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Focus Duration Presets
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Durée de Focus",
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF222636)
                                ) {
                                    Text(
                                        text = "${settings.focusDurationMinutes} min",
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            val presets = listOf(25, 45, 50, 60)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                presets.forEach { minutes ->
                                    val isSelected = settings.focusDurationMinutes == minutes
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) AmberGoldPrimary else Color(0xFF202332),
                                        border = BorderStroke(
                                            1.dp,
                                            if (isSelected) AmberGoldPrimary else Color(0xFF282C40)
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(38.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { viewModel.updateSettings(focusMinutes = minutes) }
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${minutes}m",
                                                color = if (isSelected) Color(0xFF0F1016) else TextSecondary,
                                                fontSize = 13.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Split Stepper: Pause courte & Pause longue
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Short Break Stepper
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF1C1F2E),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Pause courte",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFF262B3E),
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .clickable {
                                                    if (settings.shortBreakDurationMinutes > 1) {
                                                        viewModel.updateSettings(shortBreakMinutes = settings.shortBreakDurationMinutes - 1)
                                                    }
                                                }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Remove,
                                                    contentDescription = "Diminuer",
                                                    tint = TextSecondary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }

                                        Text(
                                            text = "${settings.shortBreakDurationMinutes} min",
                                            color = TextPrimary,
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFF262B3E),
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .clickable {
                                                    if (settings.shortBreakDurationMinutes < 30) {
                                                        viewModel.updateSettings(shortBreakMinutes = settings.shortBreakDurationMinutes + 1)
                                                    }
                                                }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = "Augmenter",
                                                    tint = TextSecondary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Long Break Stepper
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF1C1F2E),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Pause longue",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFF262B3E),
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .clickable {
                                                    if (settings.longBreakDurationMinutes > 5) {
                                                        viewModel.updateSettings(longBreakMinutes = settings.longBreakDurationMinutes - 1)
                                                    }
                                                }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Remove,
                                                    contentDescription = "Diminuer",
                                                    tint = TextSecondary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }

                                        Text(
                                            text = "${settings.longBreakDurationMinutes} min",
                                            color = TextPrimary,
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFF262B3E),
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .clickable {
                                                    if (settings.longBreakDurationMinutes < 60) {
                                                        viewModel.updateSettings(longBreakMinutes = settings.longBreakDurationMinutes + 1)
                                                    }
                                                }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = "Augmenter",
                                                    tint = TextSecondary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Auto-start breaks Switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Démarrage automatique des pauses",
                                    color = TextPrimary,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Enchaîner sans toucher l'écran",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }

                            AmberCustomSwitch(
                                checked = autoStartBreaks,
                                onCheckedChange = { autoStartBreaks = it }
                            )
                        }
                    }
                }
            }

            // 5. Section: "Ambiance Sensorielle & Immersion"
            item {
                SettingsSectionHeader(
                    icon = Icons.Default.GraphicEq,
                    title = "Ambiance Sensorielle & Immersion"
                )
            }

            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF161824),
                    border = BorderStroke(1.dp, Color(0xFF222638)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Waves Item
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF202332),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.Waves,
                                            contentDescription = null,
                                            tint = TextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "Ondes Alpha Binaurales",
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "40Hz • Synchronicité corticale",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF202332),
                                border = BorderStroke(1.dp, Color(0xFF282C40)),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        isPreviewingBinaural = !isPreviewingBinaural
                                        if (isPreviewingBinaural) {
                                            viewModel.setAmbientSound(AmbientSound.BINAURAL_FOCUS)
                                        } else {
                                            viewModel.setAmbientSound(AmbientSound.NONE)
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = if (isPreviewingBinaural) "Arrêter" else "Aperçu",
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // White Noise Item
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.setAmbientSound(AmbientSound.RAIN) },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF202332),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.WaterDrop,
                                            contentDescription = null,
                                            tint = TextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "Bruit blanc d'ambiance",
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Pluie douce sur verre",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Haptic Feedback Switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF202332),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.Vibration,
                                            contentDescription = null,
                                            tint = TextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "Retour haptique",
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Pulsation sablier réaliste",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            AmberCustomSwitch(
                                checked = hapticFeedback,
                                onCheckedChange = {
                                    hapticFeedback = it
                                    viewModel.updateSettings(vibrationEnabled = it)
                                }
                            )
                        }
                    }
                }
            }

            // 6. Section: "Esthétique du Sablier"
            item {
                SettingsSectionHeader(
                    icon = Icons.Outlined.Palette,
                    title = "Esthétique du Sablier"
                )
            }

            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF161824),
                    border = BorderStroke(1.dp, Color(0xFF222638)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Modèle 3D Verre & Métal",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Sélectionné",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        // Luxury 3D Hourglass Hero Preview Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, Color(0xFF2C3246), RoundedCornerShape(14.dp))
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.hourglass_3d_preview),
                                contentDescription = "Modèle 3D Sablier",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Subtle bottom gradient
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.Transparent,
                                                Color(0xAA0B0C12),
                                                Color(0xEE0B0C12)
                                            )
                                        )
                                    )
                            )

                            // Banner Bottom Overlay Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomStart)
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Verre Soufflé & Titane Brossé",
                                    color = TextPrimary,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF1E2130),
                                    border = BorderStroke(1.dp, AmberGoldPrimary),
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Sélectionné",
                                            tint = AmberGoldLight,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Sand Color Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(AmberGoldPrimary)
                                        .shadow(6.dp, CircleShape, spotColor = AmberGoldPrimary)
                                )

                                Column {
                                    Text(
                                        text = "Couleur du sable",
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Ambre Incandescent",
                                        color = AmberGoldLight,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE89327))
                                )
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFDE68A))
                                )
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 7. Section: "Système & Données"
            item {
                SettingsSectionHeader(
                    icon = Icons.Outlined.Terminal,
                    title = "Système & Données"
                )
            }

            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF161824),
                    border = BorderStroke(1.dp, Color(0xFF222638)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Do Not Disturb Item
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF202332),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.DoNotDisturbOn,
                                            contentDescription = null,
                                            tint = TextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "Mode Ne Pas Déranger",
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "S'active automatiquement en session",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            AmberCustomSwitch(
                                checked = doNotDisturb,
                                onCheckedChange = { doNotDisturb = it }
                            )
                        }

                        // Backup & Export Item with Async Cloud Sync
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.triggerCloudSync() }
                                .padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF202332),
                                        modifier = Modifier.size(38.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Outlined.Share,
                                                contentDescription = null,
                                                tint = TextSecondary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Column {
                                        Text(
                                            text = "Sauvegarde & Synchronisation Cloud",
                                            color = TextPrimary,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = when (syncState) {
                                                is SyncState.Syncing -> (syncState as SyncState.Syncing).step
                                                is SyncState.Success -> "Dernière synchro : Réussie (AES-256)"
                                                is SyncState.Error -> (syncState as SyncState.Error).message
                                                SyncState.Idle -> "Format CSV, JSON chiffré (AES-256)"
                                            },
                                            color = if (syncState is SyncState.Syncing) AmberGoldLight else TextSecondary,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                if (syncState is SyncState.Syncing) {
                                    CircularProgressIndicator(
                                        strokeWidth = 2.dp,
                                        color = AmberGoldPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            if (syncState is SyncState.Syncing) {
                                LinearProgressIndicator(
                                    progress = { (syncState as SyncState.Syncing).progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp)),
                                    color = AmberGoldPrimary,
                                    trackColor = Color(0xFF24283A),
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(
    icon: ImageVector,
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AmberGoldPrimary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = title,
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AmberCustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color(0xFF0F1016),
            checkedTrackColor = AmberGoldPrimary,
            uncheckedThumbColor = TextMuted,
            uncheckedTrackColor = Color(0xFF24283A),
            uncheckedBorderColor = Color(0xFF2E334A)
        )
    )
}
