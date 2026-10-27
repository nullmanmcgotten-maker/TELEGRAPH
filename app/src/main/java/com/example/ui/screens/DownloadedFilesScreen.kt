package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LocalDownloadedFile
import com.example.model.MediaType
import com.example.model.formatBytes
import com.example.ui.components.MediaTypeChip
import com.example.ui.components.getMediaTypeIconAndColor
import com.example.ui.theme.TelegramBadgeRed
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.TelegramLightBlue
import com.example.viewmodel.AppScreen
import com.example.viewmodel.TeleGrabViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DownloadedFilesScreen(
    viewModel: TeleGrabViewModel,
    modifier: Modifier = Modifier
) {
    val completedFiles by viewModel.completedFiles.collectAsState()
    val context = LocalContext.current

    var selectedFilter by remember { mutableStateOf(MediaType.ALL) }
    var fileToDelete by remember { mutableStateOf<LocalDownloadedFile?>(null) }

    val filtered = completedFiles.filter {
        selectedFilter == MediaType.ALL || it.mediaType == selectedFilter
    }

    val totalStorage = completedFiles.sumOf { it.sizeBytes }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Storage Header Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(TelegramBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderOpen,
                        contentDescription = "Downloads",
                        tint = TelegramBlue,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Downloaded Media Vault",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${completedFiles.size} files • Total storage: ${formatBytes(totalStorage)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TelegramLightBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        // Filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(MediaType.ALL, MediaType.VIDEO, MediaType.PHOTO, MediaType.DOCUMENT, MediaType.AUDIO).forEach { type ->
                MediaTypeChip(
                    type = type,
                    isSelected = selectedFilter == type,
                    onClick = { selectedFilter = type }
                )
            }
        }

        // File List
        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No Downloaded Files Found",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Downloaded items from your Telegram groups will appear here.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.GROUPS) },
                        colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Browse Telegram Groups")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered, key = { it.id }) { file ->
                    val (icon, tint) = getMediaTypeIconAndColor(file.mediaType)
                    val dateFormatted = remember(file.downloadedAt) {
                        SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date(file.downloadedAt))
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("downloaded_file_${file.id}"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(tint.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = tint,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = file.fileName,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "${file.formattedSize} • $dateFormatted",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                )

                                Text(
                                    text = "From ${file.originalGroupName}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TelegramLightBlue,
                                        fontSize = 11.sp
                                    ),
                                    maxLines = 1
                                )
                            }

                            // Share Action
                            IconButton(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "Downloaded via TeleGrab: ${file.fileName}")
                                        type = file.mimeType
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share ${file.fileName}"))
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = TelegramBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Delete Action
                            IconButton(
                                onClick = { fileToDelete = file },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    // Confirm Delete Dialog
    fileToDelete?.let { file ->
        AlertDialog(
            onDismissRequest = { fileToDelete = null },
            title = { Text("Delete Downloaded File?") },
            text = { Text("Are you sure you want to remove '${file.fileName}' from your device?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteDownloadedFile(file.id)
                        fileToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBadgeRed)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { fileToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
