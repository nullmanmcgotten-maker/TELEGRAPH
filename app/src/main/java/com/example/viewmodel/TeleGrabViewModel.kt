package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BatchDownloadManager
import com.example.data.TelegramDataRepository
import com.example.model.DownloadTask
import com.example.model.LocalDownloadedFile
import com.example.model.MediaType
import com.example.model.TelegramGroup
import com.example.model.TelegramMediaItem
import com.example.model.TelegramUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    AUTH,
    GROUPS,
    GROUP_EXPLORER,
    DOWNLOAD_QUEUE,
    DOWNLOADED_FILES,
    SETTINGS
}

enum class SizeFilter(val label: String) {
    ALL("Any Size"),
    UNDER_10MB("< 10 MB"),
    FROM_10_TO_100MB("10 - 100 MB"),
    OVER_100MB("> 100 MB")
}

enum class SortMode(val label: String) {
    DATE_NEWEST("Newest First"),
    DATE_OLDEST("Oldest First"),
    SIZE_LARGEST("Largest First"),
    SIZE_SMALLEST("Smallest First"),
    NAME("Name A-Z")
}

class TeleGrabViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TelegramDataRepository()
    private val downloadManager = BatchDownloadManager(application.applicationContext)

    val currentUser: StateFlow<TelegramUser?> = repository.currentUser
    val groups: StateFlow<List<TelegramGroup>> = repository.groups

    val downloadTasks: StateFlow<List<DownloadTask>> = downloadManager.tasks
    val completedFiles: StateFlow<List<LocalDownloadedFile>> = downloadManager.completedFiles

    // Navigation
    private val _currentScreen = MutableStateFlow(
        if (currentUser.value != null) AppScreen.GROUPS else AppScreen.AUTH
    )
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Screen stack for back navigation
    private val screenStack = mutableListOf<AppScreen>()

    // Current selected group
    private val _selectedGroup = MutableStateFlow<TelegramGroup?>(null)
    val selectedGroup: StateFlow<TelegramGroup?> = _selectedGroup.asStateFlow()

    // Filters for group explorer
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedMediaType = MutableStateFlow(MediaType.ALL)
    val selectedMediaType: StateFlow<MediaType> = _selectedMediaType.asStateFlow()

    private val _sizeFilter = MutableStateFlow(SizeFilter.ALL)
    val sizeFilter: StateFlow<SizeFilter> = _sizeFilter.asStateFlow()

    private val _sortMode = MutableStateFlow(SortMode.DATE_NEWEST)
    val sortMode: StateFlow<SortMode> = _sortMode.asStateFlow()

    private val _isGridView = MutableStateFlow(false)
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    // Multi-selection set
    private val _selectedItemIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedItemIds: StateFlow<Set<String>> = _selectedItemIds.asStateFlow()

    // Notification toast / message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Preview item in dialog
    private val _previewItem = MutableStateFlow<TelegramMediaItem?>(null)
    val previewItem: StateFlow<TelegramMediaItem?> = _previewItem.asStateFlow()

    // Filtered media items flow
    val currentGroupMedia: StateFlow<List<TelegramMediaItem>> = combine(
        _selectedGroup,
        _searchQuery,
        _selectedMediaType,
        _sizeFilter,
        _sortMode
    ) { group, query, type, size, sort ->
        if (group == null) return@combine emptyList<TelegramMediaItem>()
        var items = repository.getGroupMedia(group.id)

        // Type filter
        if (type != MediaType.ALL) {
            items = items.filter { it.mediaType == type }
        }

        // Query filter
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            items = items.filter {
                it.title.lowercase().contains(q) ||
                it.fileName.lowercase().contains(q) ||
                it.senderName.lowercase().contains(q)
            }
        }

        // Size filter
        items = when (size) {
            SizeFilter.ALL -> items
            SizeFilter.UNDER_10MB -> items.filter { it.sizeBytes < 10_000_000L }
            SizeFilter.FROM_10_TO_100MB -> items.filter { it.sizeBytes in 10_000_000L..100_000_000L }
            SizeFilter.OVER_100MB -> items.filter { it.sizeBytes > 100_000_000L }
        }

        // Sort
        when (sort) {
            SortMode.DATE_NEWEST -> items.sortedByDescending { it.timestamp }
            SortMode.DATE_OLDEST -> items.sortedBy { it.timestamp }
            SortMode.SIZE_LARGEST -> items.sortedByDescending { it.sizeBytes }
            SortMode.SIZE_SMALLEST -> items.sortedBy { it.sizeBytes }
            SortMode.NAME -> items.sortedBy { it.fileName.lowercase() }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Selection metrics (count & total size)
    val selectionMetrics: StateFlow<Pair<Int, Long>> = combine(
        currentGroupMedia,
        _selectedItemIds
    ) { items, selectedIds ->
        val selectedItems = items.filter { it.id in selectedIds }
        val count = selectedItems.size
        val totalBytes = selectedItems.sumOf { it.sizeBytes }
        Pair(count, totalBytes)
    }.stateIn(viewModelScope, SharingStarted.Lazily, Pair(0, 0L))

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            screenStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            val prev = screenStack.removeAt(screenStack.size - 1)
            _currentScreen.value = prev
            return true
        }
        return false
    }

    fun openGroup(group: TelegramGroup) {
        _selectedGroup.value = group
        _selectedItemIds.value = emptySet()
        _searchQuery.value = ""
        _selectedMediaType.value = MediaType.ALL
        _sizeFilter.value = SizeFilter.ALL
        navigateTo(AppScreen.GROUP_EXPLORER)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedMediaType(type: MediaType) {
        _selectedMediaType.value = type
    }

    fun setSizeFilter(filter: SizeFilter) {
        _sizeFilter.value = filter
    }

    fun setSortMode(mode: SortMode) {
        _sortMode.value = mode
    }

    fun toggleGridView() {
        _isGridView.value = !_isGridView.value
    }

    fun toggleItemSelection(itemId: String) {
        val current = _selectedItemIds.value.toMutableSet()
        if (current.contains(itemId)) {
            current.remove(itemId)
        } else {
            current.add(itemId)
        }
        _selectedItemIds.value = current
    }

    fun selectAll() {
        val allIds = currentGroupMedia.value.map { it.id }.toSet()
        _selectedItemIds.value = allIds
        showToast("Selected all ${allIds.size} files")
    }

    fun deselectAll() {
        _selectedItemIds.value = emptySet()
        showToast("Cleared selection")
    }

    fun invertSelection() {
        val current = _selectedItemIds.value
        val allIds = currentGroupMedia.value.map { it.id }.toSet()
        _selectedItemIds.value = allIds - current
    }

    fun selectAllOfType(type: MediaType) {
        val matchingIds = currentGroupMedia.value.filter { it.mediaType == type }.map { it.id }.toSet()
        val current = _selectedItemIds.value.toMutableSet()
        current.addAll(matchingIds)
        _selectedItemIds.value = current
        showToast("Selected all ${type.label} (${matchingIds.size})")
    }

    fun downloadSelectedItems() {
        val selectedIds = _selectedItemIds.value
        if (selectedIds.isEmpty()) {
            showToast("Please select items to download first")
            return
        }
        val itemsToDownload = currentGroupMedia.value.filter { it.id in selectedIds }
        downloadManager.enqueueDownloads(itemsToDownload)
        _selectedItemIds.value = emptySet()
        showToast("Queued ${itemsToDownload.size} files for download")
    }

    fun downloadSingleItem(item: TelegramMediaItem) {
        downloadManager.enqueueDownloads(listOf(item))
        showToast("Queued '${item.fileName}' for download")
    }

    fun setPreviewItem(item: TelegramMediaItem?) {
        _previewItem.value = item
    }

    // Auth methods
    fun loginPhone(phone: String) {
        val trimmed = phone.trim()
        if (trimmed.isBlank() || trimmed.filter { it.isDigit() }.length < 4) {
            showToast("Please enter a valid phone number")
            return
        }
        repository.loginWithPhone(trimmed)
        _currentScreen.value = AppScreen.GROUPS
        showToast("Logged in to Telegram ($trimmed)")
    }

    fun loginBotToken(token: String) {
        val trimmed = token.trim()
        if (trimmed.isBlank()) {
            showToast("Please enter a Telegram Bot Token")
            return
        }
        repository.loginWithBotToken(trimmed)
        _currentScreen.value = AppScreen.GROUPS
        showToast("Telegram Bot Connected")
    }

    fun addPublicChannel(handle: String) {
        val group = repository.addPublicGroupOrChannel(handle)
        openGroup(group)
        showToast("Loaded public channel: @${group.username}")
    }

    fun logout() {
        repository.logout()
        _selectedGroup.value = null
        _selectedItemIds.value = emptySet()
        _currentScreen.value = AppScreen.AUTH
        showToast("Logged out from Telegram")
    }

    // Download controls
    fun pauseTask(taskId: String) = downloadManager.pauseTask(taskId)
    fun resumeTask(taskId: String) = downloadManager.resumeTask(taskId)
    fun cancelTask(taskId: String) = downloadManager.cancelTask(taskId)
    fun pauseAll() = downloadManager.pauseAll()
    fun resumeAll() = downloadManager.resumeAll()
    fun clearCompleted() = downloadManager.clearCompleted()
    fun deleteDownloadedFile(id: String) = downloadManager.deleteDownloadedFile(id)

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
