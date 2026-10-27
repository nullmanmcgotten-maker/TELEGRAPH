package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DownloadStatus
import com.example.model.DownloadTask
import com.example.model.formatBytes
import com.example.ui.components.getMediaTypeIconAndColor
import com.example.ui.theme.TelegramBadgeGreen
import com.example.ui.theme.TelegramBadgeOrange
import com.example.ui.theme.TelegramBadgeRed
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.TelegramLightBlue
import com.example.ui.theme.TelegramWhite
import com.example.viewmodel.AppScreen
import com.example.viewmodel.TeleGrabViewModel

@Composable
fun DownloadQueueScreen(
    viewModel: TeleGrabViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.downloadTasks.collectAsState()

    val activeCount = tasks.count { it.status == DownloadStatus.DOWNLOADING }
    val pendingCount = tasks.count { it.status == DownloadStatus.PENDING }
    val completedCount = tasks.count { it.status == DownloadStatus.COMPLETED }
    val totalSpeed = tasks.filter { it.status == DownloadStatus.DOWNLOADING }.sumOf { it.speedBytesPerSec }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Queue Summary Dashboard Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Batch Download Queue",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$activeCount active • $pendingCount queued • $completedCount completed",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }

                    if (totalSpeed > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TelegramBlue.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = TelegramBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${formatBytes(totalSpeed)}/s",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TelegramBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }

                if (tasks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.pauseAll() },
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            Icon(Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pause All", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.resumeAll() },
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Resume All", fontSize = 12.sp)
                        }

                        if (completedCount > 0) {
                            TextButton(
                                onClick = { viewModel.clearCompleted() },
                                modifier = Modifier.height(38.dp)
                            ) {
                                Text("Clear Done", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Tasks List
        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(TelegramBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Queue Empty",
                            tint = TelegramBlue,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Download Queue is Empty",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Select a Telegram group and choose files to batch download",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.GROUPS) },
                        colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("browse_groups_from_queue_btn")
                    ) {
                        Text("Browse Groups & Files")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    DownloadTaskCard(
                        task = task,
                        onPause = { viewModel.pauseTask(task.id) },
                        onResume = { viewModel.resumeTask(task.id) },
                        onCancel = { viewModel.cancelTask(task.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun DownloadTaskCard(
    task: DownloadTask,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit
) {
    val (icon, tint) = getMediaTypeIconAndColor(task.mediaItem.mediaType)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("download_task_${task.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(tint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.mediaItem.fileName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = task.mediaItem.groupName,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        ),
                        maxLines = 1
                    )
                }

                // Status Badge & Controls
                when (task.status) {
                    DownloadStatus.DOWNLOADING -> {
                        IconButton(onClick = onPause, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.PauseCircle, contentDescription = "Pause", tint = TelegramBlue)
                        }
                    }
                    DownloadStatus.PAUSED, DownloadStatus.FAILED -> {
                        IconButton(onClick = onResume, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.PlayCircle, contentDescription = "Resume", tint = TelegramBadgeGreen)
                        }
                    }
                    DownloadStatus.PENDING -> {
                        Icon(
                            imageVector = Icons.Default.HourglassTop,
                            contentDescription = "Pending",
                            tint = TelegramBadgeOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    DownloadStatus.COMPLETED -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = TelegramBadgeGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    DownloadStatus.CANCELLED -> {}
                }

                if (task.status != DownloadStatus.COMPLETED) {
                    IconButton(onClick = onCancel, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Cancel",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { task.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = when (task.status) {
                    DownloadStatus.COMPLETED -> TelegramBadgeGreen
                    DownloadStatus.PAUSED -> TelegramBadgeOrange
                    DownloadStatus.FAILED -> TelegramBadgeRed
                    else -> TelegramBlue
                },
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Metrics row: Progress % • downloaded/total • Speed / ETA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${(task.progress * 100).toInt()}% • ${task.formattedDownloaded}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

                Text(
                    text = when (task.status) {
                        DownloadStatus.DOWNLOADING -> "${task.formattedSpeed} • ${task.formattedEta}"
                        DownloadStatus.PAUSED -> "Paused"
                        DownloadStatus.PENDING -> "Queued..."
                        DownloadStatus.COMPLETED -> "Saved to device"
                        DownloadStatus.FAILED -> task.error ?: "Download failed"
                        DownloadStatus.CANCELLED -> "Cancelled"
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = when (task.status) {
                            DownloadStatus.DOWNLOADING -> TelegramLightBlue
                            DownloadStatus.COMPLETED -> TelegramBadgeGreen
                            DownloadStatus.PAUSED -> TelegramBadgeOrange
                            DownloadStatus.FAILED -> TelegramBadgeRed
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}
