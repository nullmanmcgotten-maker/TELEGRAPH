package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.DownloadStatus
import com.example.ui.components.TeleGrabTopBar
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DownloadQueueScreen
import com.example.ui.screens.DownloadedFilesScreen
import com.example.ui.screens.GroupExplorerScreen
import com.example.ui.screens.GroupsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.TeleGrabTheme
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.TelegramWhite
import com.example.viewmodel.AppScreen
import com.example.viewmodel.TeleGrabViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TeleGrabViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeleGrabTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: TeleGrabViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val downloadTasks by viewModel.downloadTasks.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearToast()
        }
    }

    val activeDownloadsCount = downloadTasks.count {
        it.status == DownloadStatus.DOWNLOADING || it.status == DownloadStatus.PENDING
    }

    val showBottomNav = currentUser != null && currentScreen != AppScreen.AUTH && currentScreen != AppScreen.GROUP_EXPLORER

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentScreen != AppScreen.GROUP_EXPLORER && currentScreen != AppScreen.AUTH) {
                TeleGrabTopBar(
                    title = when (currentScreen) {
                        AppScreen.GROUPS -> "Telegram Groups"
                        AppScreen.DOWNLOAD_QUEUE -> "Download Manager"
                        AppScreen.DOWNLOADED_FILES -> "Downloaded Vault"
                        AppScreen.SETTINGS -> "Settings & Preferences"
                        else -> "TeleGrab"
                    },
                    subtitle = when (currentScreen) {
                        AppScreen.GROUPS -> "Select a group to extract and download media"
                        AppScreen.DOWNLOAD_QUEUE -> "$activeDownloadsCount active downloads"
                        AppScreen.DOWNLOADED_FILES -> "Saved offline files on device"
                        else -> null
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomNav) {
                NavigationBar(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_nav_bar"),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.GROUPS,
                        onClick = { viewModel.navigateTo(AppScreen.GROUPS) },
                        icon = {
                            Icon(Icons.Default.Groups, contentDescription = "Groups")
                        },
                        label = { Text("Groups") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TelegramBlue,
                            selectedTextColor = TelegramBlue,
                            indicatorColor = TelegramBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_groups")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.DOWNLOAD_QUEUE,
                        onClick = { viewModel.navigateTo(AppScreen.DOWNLOAD_QUEUE) },
                        icon = {
                            if (activeDownloadsCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(containerColor = TelegramBlue, contentColor = TelegramWhite) {
                                            Text("$activeDownloadsCount")
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Download, contentDescription = "Downloads")
                                }
                            } else {
                                Icon(Icons.Default.Download, contentDescription = "Downloads")
                            }
                        },
                        label = { Text("Queue") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TelegramBlue,
                            selectedTextColor = TelegramBlue,
                            indicatorColor = TelegramBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_queue")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.DOWNLOADED_FILES,
                        onClick = { viewModel.navigateTo(AppScreen.DOWNLOADED_FILES) },
                        icon = {
                            Icon(Icons.Default.FolderOpen, contentDescription = "Saved Files")
                        },
                        label = { Text("Vault") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TelegramBlue,
                            selectedTextColor = TelegramBlue,
                            indicatorColor = TelegramBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_vault")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.SETTINGS,
                        onClick = { viewModel.navigateTo(AppScreen.SETTINGS) },
                        icon = {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        },
                        label = { Text("Settings") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TelegramBlue,
                            selectedTextColor = TelegramBlue,
                            indicatorColor = TelegramBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_settings")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
                when (screen) {
                    AppScreen.AUTH -> AuthScreen(viewModel = viewModel)
                    AppScreen.GROUPS -> GroupsScreen(viewModel = viewModel)
                    AppScreen.GROUP_EXPLORER -> GroupExplorerScreen(viewModel = viewModel)
                    AppScreen.DOWNLOAD_QUEUE -> DownloadQueueScreen(viewModel = viewModel)
                    AppScreen.DOWNLOADED_FILES -> DownloadedFilesScreen(viewModel = viewModel)
                    AppScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }
}
