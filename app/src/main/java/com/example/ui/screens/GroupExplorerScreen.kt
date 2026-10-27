package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MediaType
import com.example.model.TelegramMediaItem
import com.example.model.formatBytes
import com.example.ui.components.MediaItemDetailDialog
import com.example.ui.components.MediaListItem
import com.example.ui.components.MediaTypeChip
import com.example.ui.components.StickySelectionBottomBar
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.TelegramLightBlue
import com.example.ui.theme.TelegramWhite
import com.example.viewmodel.SizeFilter
import com.example.viewmodel.SortMode
import com.example.viewmodel.TeleGrabViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupExplorerScreen(
    viewModel: TeleGrabViewModel,
    modifier: Modifier = Modifier
) {
    val selectedGroup by viewModel.selectedGroup.collectAsState()
    val mediaItems by viewModel.currentGroupMedia.collectAsState()
    val selectedItemIds by viewModel.selectedItemIds.collectAsState()
    val selectionMetrics by viewModel.selectionMetrics.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedMediaType by viewModel.selectedMediaType.collectAsState()
    val sizeFilter by viewModel.sizeFilter.collectAsState()
    val sortMode by viewModel.sortMode.collectAsState()
    val previewItem by viewModel.previewItem.collectAsState()

    var showSortMenu by remember { mutableStateOf(false) }
    var showSizeMenu by remember { mutableStateOf(false) }

    // Intercept hardware or software back press to return to groups list
    BackHandler {
        viewModel.navigateBack()
    }

    if (selectedGroup == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No group selected")
        }
        return
    }

    val (selectedCount, totalSelectedBytes) = selectionMetrics
    val totalCountInView = mediaItems.size

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(TelegramBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedGroup!!.avatarUrl.isNotBlank()) {
                                AsyncImage(
                                    model = selectedGroup!!.avatarUrl,
                                    contentDescription = selectedGroup!!.title,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text(
                                    text = selectedGroup!!.title.take(1),
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = TelegramWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = selectedGroup!!.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${selectedGroup!!.formattedMembers} • ${selectedGroup!!.totalMediaCount} files",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("group_explorer_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Groups"
                        )
                    }
                },
                actions = {
                    // Quick Select All / Deselect All Icon Button
                    IconButton(
                        onClick = {
                            if (selectedCount == totalCountInView && totalCountInView > 0) {
                                viewModel.deselectAll()
                            } else {
                                viewModel.selectAll()
                            }
                        },
                        modifier = Modifier.testTag("group_select_all_header_btn")
                    ) {
                        Icon(
                            imageVector = if (selectedCount == totalCountInView && totalCountInView > 0) {
                                Icons.Default.Deselect
                            } else {
                                Icons.Default.SelectAll
                            },
                            contentDescription = "Select / Deselect All",
                            tint = TelegramBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // Sticky Bulk Download bar when items are selected
            StickySelectionBottomBar(
                selectedCount = selectedCount,
                totalSizeBytes = totalSelectedBytes,
                totalCountInView = totalCountInView,
                onSelectAll = { viewModel.selectAll() },
                onDeselectAll = { viewModel.deselectAll() },
                onDownloadSelected = { viewModel.downloadSelectedItems() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Search inside group
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("search_group_media_field"),
                placeholder = { Text("Search files, videos, documents in group...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = TelegramBlue)
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Media Type Filter Scrollable Chips
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MediaType.values().forEach { type ->
                    MediaTypeChip(
                        type = type,
                        isSelected = selectedMediaType == type,
                        onClick = { viewModel.setSelectedMediaType(type) }
                    )
                }
            }

            // Bulk Selection & Tool Actions Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Bulk Selection Controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { viewModel.selectAll() },
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("select_all_btn"),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SelectAll,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Select All", fontSize = 12.sp)
                        }

                        if (selectedCount > 0) {
                            TextButton(
                                onClick = { viewModel.deselectAll() },
                                modifier = Modifier
                                    .height(36.dp)
                                    .testTag("deselect_all_btn")
                            ) {
                                Text("Clear", fontSize = 12.sp)
                            }

                            TextButton(
                                onClick = { viewModel.invertSelection() },
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Invert", fontSize = 12.sp)
                            }
                        }
                    }

                    // Filters & Sorting Menus
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Size Filter Dropdown
                        Box {
                            IconButton(
                                onClick = { showSizeMenu = true },
                                modifier = Modifier.size(36.dp).testTag("size_filter_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterAlt,
                                    contentDescription = "Size Filter",
                                    tint = if (sizeFilter != SizeFilter.ALL) TelegramBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = showSizeMenu,
                                onDismissRequest = { showSizeMenu = false }
                            ) {
                                SizeFilter.values().forEach { filter ->
                                    DropdownMenuItem(
                                        text = { Text(filter.label) },
                                        onClick = {
                                            viewModel.setSizeFilter(filter)
                                            showSizeMenu = false
                                        }
                                    )
                                }
                            }
                        }

                        // Sort Menu Dropdown
                        Box {
                            IconButton(
                                onClick = { showSortMenu = true },
                                modifier = Modifier.size(36.dp).testTag("sort_mode_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Sort,
                                    contentDescription = "Sort",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false }
                            ) {
                                SortMode.values().forEach { mode ->
                                    DropdownMenuItem(
                                        text = { Text(mode.label) },
                                        onClick = {
                                            viewModel.setSortMode(mode)
                                            showSortMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Items Count and Selected status line
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Showing ${mediaItems.size} files (${sizeFilter.label})",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )

                if (selectedCount > 0) {
                    Text(
                        text = "$selectedCount selected (${formatBytes(totalSelectedBytes)})",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TelegramLightBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            // Media Files List
            if (mediaItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No files match the selected filter",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try switching media tabs or resetting search filters",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                viewModel.setSearchQuery("")
                                viewModel.setSelectedMediaType(MediaType.ALL)
                                viewModel.setSizeFilter(SizeFilter.ALL)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue)
                        ) {
                            Text("Reset All Filters")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mediaItems, key = { it.id }) { item ->
                        MediaListItem(
                            item = item,
                            isSelected = selectedItemIds.contains(item.id),
                            onToggleSelect = { viewModel.toggleItemSelection(item.id) },
                            onDownloadSingle = { viewModel.downloadSingleItem(item) },
                            onPreview = { viewModel.setPreviewItem(item) }
                        )
                    }

                    item {
                        // Extra bottom spacing so sticky bottom bar doesn't obscure the last items
                        Spacer(modifier = Modifier.height(140.dp))
                    }
                }
            }
        }
    }

    // Details / Preview Dialog
    previewItem?.let { item ->
        MediaItemDetailDialog(
            item = item,
            onDismiss = { viewModel.setPreviewItem(null) },
            onDownload = { viewModel.downloadSingleItem(item) }
        )
    }
}
