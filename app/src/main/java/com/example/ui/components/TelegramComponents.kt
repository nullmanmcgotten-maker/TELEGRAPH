package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.model.MediaType
import com.example.model.TelegramGroup
import com.example.model.TelegramMediaItem
import com.example.model.formatBytes
import com.example.ui.theme.TelegramBadgeGreen
import com.example.ui.theme.TelegramBadgeOrange
import com.example.ui.theme.TelegramBadgePurple
import com.example.ui.theme.TelegramBadgeRed
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.TelegramDarkBlue
import com.example.ui.theme.TelegramLightBlue
import com.example.ui.theme.TelegramNavy
import com.example.ui.theme.TelegramWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeleGrabTopBar(
    title: String,
    subtitle: String? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun MediaTypeChip(
    type: MediaType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val (icon, tint) = getMediaTypeIconAndColor(type)

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("media_type_chip_${type.name.lowercase()}"),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = type.label,
                tint = if (isSelected) TelegramWhite else tint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = type.label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) TelegramWhite else MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun getMediaTypeIconAndColor(type: MediaType): Pair<ImageVector, Color> {
    return when (type) {
        MediaType.ALL -> Pair(Icons.Default.Folder, TelegramBlue)
        MediaType.PHOTO -> Pair(Icons.Default.Image, TelegramBadgeGreen)
        MediaType.VIDEO -> Pair(Icons.Default.Videocam, TelegramBadgeRed)
        MediaType.DOCUMENT -> Pair(Icons.Default.Description, TelegramBadgeOrange)
        MediaType.AUDIO -> Pair(Icons.Default.AudioFile, TelegramBadgePurple)
        MediaType.ARCHIVE -> Pair(Icons.Default.FolderZip, TelegramLightBlue)
        MediaType.APK -> Pair(Icons.Default.Archive, Color(0xFF009688))
    }
}

@Composable
fun TelegramGroupCard(
    group: TelegramGroup,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("group_card_${group.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Group Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(TelegramBlue, TelegramDarkBlue)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (group.avatarUrl.isNotBlank()) {
                    AsyncImage(
                        model = group.avatarUrl,
                        contentDescription = group.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = group.title.take(2).uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TelegramWhite,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = group.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (group.hasUnreadMedia) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(TelegramBlue.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "NEW MEDIA",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TelegramBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = group.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = group.formattedMembers,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Text(
                        text = "${group.totalMediaCount} files (${group.formattedTotalSize})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TelegramLightBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun MediaListItem(
    item: TelegramMediaItem,
    isSelected: Boolean,
    onToggleSelect: () -> Unit,
    onDownloadSingle: () -> Unit,
    onPreview: () -> Unit
) {
    val (icon, tint) = getMediaTypeIconAndColor(item.mediaType)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onToggleSelect)
            .testTag("media_item_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) TelegramBlue.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, TelegramBlue) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selection Checkbox
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggleSelect() },
                colors = CheckboxDefaults.colors(
                    checkedColor = TelegramBlue,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("checkbox_${item.id}")
            )

            Spacer(modifier = Modifier.width(6.dp))

            // Thumbnail or Media Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(tint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                if (!item.thumbnailUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = item.thumbnailUrl,
                        contentDescription = item.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = item.mediaType.label,
                        tint = tint,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.fileName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(tint.copy(alpha = 0.15f))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = item.extension.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = tint,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        )
                    }

                    Text(
                        text = item.formattedSize,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    if (item.formattedDuration != null) {
                        Text(
                            text = "• ${item.formattedDuration}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    if (item.resolution != null) {
                        Text(
                            text = "• ${item.resolution}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${item.senderName} • ${item.messageDate}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    ),
                    maxLines = 1
                )
            }

            // Quick Actions: Details / Preview & Download
            IconButton(
                onClick = onPreview,
                modifier = Modifier.testTag("preview_button_${item.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Details",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = onDownloadSingle,
                modifier = Modifier.testTag("download_button_${item.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download",
                    tint = TelegramBlue,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun StickySelectionBottomBar(
    selectedCount: Int,
    totalSizeBytes: Long,
    totalCountInView: Int,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onDownloadSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = selectedCount > 0,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 10.dp,
            shadowElevation = 12.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, TelegramBlue.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(TelegramBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$selectedCount",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TelegramWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Selected Files",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Total payload: ${formatBytes(totalSizeBytes)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TelegramLightBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        TextButton(
                            onClick = if (selectedCount == totalCountInView) onDeselectAll else onSelectAll,
                            modifier = Modifier.testTag("selection_bar_select_all_btn")
                        ) {
                            Text(
                                text = if (selectedCount == totalCountInView) "Deselect" else "Select All",
                                style = MaterialTheme.typography.labelMedium.copy(color = TelegramBlue)
                            )
                        }

                        IconButton(
                            onClick = onDeselectAll,
                            modifier = Modifier.testTag("selection_bar_close_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Selection",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDownloadSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("download_all_amount_selection_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download All Selection",
                        tint = TelegramWhite
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Download All Selection ($selectedCount files)",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = TelegramWhite,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun MediaItemDetailDialog(
    item: TelegramMediaItem,
    onDismiss: () -> Unit,
    onDownload: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "File Details",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!item.thumbnailUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = item.thumbnailUrl,
                        contentDescription = item.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                DetailRow(label = "File Name", value = item.fileName)
                DetailRow(label = "Group", value = item.groupName)
                DetailRow(label = "Type", value = item.mediaType.label)
                DetailRow(label = "Size", value = item.formattedSize)
                DetailRow(label = "Sender", value = item.senderName)
                DetailRow(label = "Date", value = item.messageDate)
                DetailRow(label = "MIME Type", value = item.mimeType)

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        onDownload()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dialog_download_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = "Download")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Download File (${item.formattedSize})")
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}
