package com.example.data

import android.content.Context
import android.util.Log
import com.example.model.DownloadStatus
import com.example.model.DownloadTask
import com.example.model.LocalDownloadedFile
import com.example.model.TelegramMediaItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class BatchDownloadManager(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    private val _tasks = MutableStateFlow<List<DownloadTask>>(emptyList())
    val tasks: StateFlow<List<DownloadTask>> = _tasks.asStateFlow()

    private val _completedFiles = MutableStateFlow<List<LocalDownloadedFile>>(emptyList())
    val completedFiles: StateFlow<List<LocalDownloadedFile>> = _completedFiles.asStateFlow()

    private val activeJobs = mutableMapOf<String, Job>()
    private val downloadFolder = File(context.filesDir, "telegram_downloads").apply {
        if (!exists()) mkdirs()
    }

    init {
        // Load any existing files in folder
        loadExistingFiles()
    }

    private fun loadExistingFiles() {
        val files = downloadFolder.listFiles() ?: return
        val list = files.map { f ->
            val ext = f.extension.lowercase()
            val type = when (ext) {
                "mp4", "mkv" -> com.example.model.MediaType.VIDEO
                "jpg", "jpeg", "png", "webp" -> com.example.model.MediaType.PHOTO
                "mp3", "wav", "flac" -> com.example.model.MediaType.AUDIO
                "apk" -> com.example.model.MediaType.APK
                "zip", "rar", "7z" -> com.example.model.MediaType.ARCHIVE
                else -> com.example.model.MediaType.DOCUMENT
            }
            LocalDownloadedFile(
                id = f.name,
                fileName = f.name,
                originalGroupName = "Telegram Vault",
                mediaType = type,
                sizeBytes = f.length().coerceAtLeast(1024L),
                downloadedAt = f.lastModified(),
                filePath = f.absolutePath,
                mimeType = when (type) {
                    com.example.model.MediaType.VIDEO -> "video/mp4"
                    com.example.model.MediaType.PHOTO -> "image/jpeg"
                    com.example.model.MediaType.AUDIO -> "audio/mpeg"
                    com.example.model.MediaType.APK -> "application/vnd.android.package-archive"
                    com.example.model.MediaType.ARCHIVE -> "application/zip"
                    com.example.model.MediaType.DOCUMENT -> "application/pdf"
                    else -> "*/*"
                }
            )
        }
        _completedFiles.value = list
    }

    fun enqueueDownloads(items: List<TelegramMediaItem>) {
        val newTasks = items.map { item ->
            DownloadTask(
                id = UUID.randomUUID().toString(),
                mediaItem = item,
                progress = 0f,
                downloadedBytes = 0L,
                totalBytes = item.sizeBytes,
                status = DownloadStatus.PENDING
            )
        }
        _tasks.value = _tasks.value + newTasks

        processNextPendingTasks()
    }

    private fun processNextPendingTasks() {
        val currentTasks = _tasks.value
        val downloadingCount = currentTasks.count { it.status == DownloadStatus.DOWNLOADING }
        val maxConcurrent = 3

        if (downloadingCount >= maxConcurrent) return

        val slotsAvailable = maxConcurrent - downloadingCount
        val pending = currentTasks.filter { it.status == DownloadStatus.PENDING }.take(slotsAvailable)

        pending.forEach { task ->
            startDownloadJob(task.id)
        }
    }

    private fun startDownloadJob(taskId: String) {
        val task = _tasks.value.find { it.id == taskId } ?: return
        if (task.status == DownloadStatus.DOWNLOADING) return

        updateTask(taskId) { it.copy(status = DownloadStatus.DOWNLOADING) }

        val job = scope.launch {
            try {
                val media = task.mediaItem
                val targetFile = File(downloadFolder, media.fileName)

                val totalSize = media.sizeBytes
                var currentBytes = task.downloadedBytes

                // Real write to file to guarantee valid file on disk
                val out = FileOutputStream(targetFile, currentBytes > 0)

                // Simulated high-speed network chunking (2 MB/s to 8 MB/s)
                val bufferChunk = 65536 // 64 KB
                val dummyBytes = ByteArray(bufferChunk) { (it % 128).toByte() }

                var lastSpeedCalcTime = System.currentTimeMillis()
                var bytesSinceLastCalc = 0L
                var currentSpeed = 3_500_000L // 3.5 MB/s baseline

                while (isActive && currentBytes < totalSize) {
                    val taskStatus = _tasks.value.find { it.id == taskId }?.status
                    if (taskStatus == DownloadStatus.PAUSED || taskStatus == DownloadStatus.CANCELLED) {
                        out.flush()
                        out.close()
                        return@launch
                    }

                    val toWrite = minOf(bufferChunk.toLong(), totalSize - currentBytes).toInt()
                    out.write(dummyBytes, 0, toWrite)
                    currentBytes += toWrite
                    bytesSinceLastCalc += toWrite

                    val now = System.currentTimeMillis()
                    val timeDiff = now - lastSpeedCalcTime
                    if (timeDiff >= 400) {
                        currentSpeed = (bytesSinceLastCalc * 1000L) / timeDiff.coerceAtLeast(1L)
                        bytesSinceLastCalc = 0L
                        lastSpeedCalcTime = now

                        val progress = (currentBytes.toFloat() / totalSize.toFloat()).coerceIn(0f, 1f)
                        updateTask(taskId) {
                            it.copy(
                                progress = progress,
                                downloadedBytes = currentBytes,
                                speedBytesPerSec = currentSpeed
                            )
                        }
                    }

                    // Throttle for realistic smooth streaming download (approx 4-6 MB/s)
                    delay(12)
                }

                out.flush()
                out.close()

                // Mark completed
                updateTask(taskId) {
                    it.copy(
                        progress = 1f,
                        downloadedBytes = totalSize,
                        speedBytesPerSec = 0L,
                        status = DownloadStatus.COMPLETED,
                        completedAt = System.currentTimeMillis(),
                        localFilePath = targetFile.absolutePath
                    )
                }

                // Add to local completed files
                val localFile = LocalDownloadedFile(
                    id = targetFile.name,
                    fileName = targetFile.name,
                    originalGroupName = media.groupName,
                    mediaType = media.mediaType,
                    sizeBytes = totalSize,
                    downloadedAt = System.currentTimeMillis(),
                    filePath = targetFile.absolutePath,
                    mimeType = media.mimeType
                )
                _completedFiles.value = listOf(localFile) + _completedFiles.value

            } catch (e: Exception) {
                Log.e("BatchDownloadManager", "Download error", e)
                updateTask(taskId) {
                    it.copy(
                        status = DownloadStatus.FAILED,
                        error = e.localizedMessage ?: "Network error"
                    )
                }
            } finally {
                activeJobs.remove(taskId)
                processNextPendingTasks()
            }
        }

        activeJobs[taskId] = job
    }

    fun pauseTask(taskId: String) {
        activeJobs[taskId]?.cancel()
        activeJobs.remove(taskId)
        updateTask(taskId) { it.copy(status = DownloadStatus.PAUSED, speedBytesPerSec = 0L) }
        processNextPendingTasks()
    }

    fun resumeTask(taskId: String) {
        val task = _tasks.value.find { it.id == taskId } ?: return
        if (task.status == DownloadStatus.PAUSED || task.status == DownloadStatus.FAILED) {
            updateTask(taskId) { it.copy(status = DownloadStatus.PENDING) }
            processNextPendingTasks()
        }
    }

    fun cancelTask(taskId: String) {
        activeJobs[taskId]?.cancel()
        activeJobs.remove(taskId)
        _tasks.value = _tasks.value.filterNot { it.id == taskId }
        processNextPendingTasks()
    }

    fun pauseAll() {
        _tasks.value.filter { it.status == DownloadStatus.DOWNLOADING || it.status == DownloadStatus.PENDING }
            .forEach { pauseTask(it.id) }
    }

    fun resumeAll() {
        _tasks.value.filter { it.status == DownloadStatus.PAUSED }
            .forEach { resumeTask(it.id) }
    }

    fun clearCompleted() {
        _tasks.value = _tasks.value.filterNot { it.status == DownloadStatus.COMPLETED }
    }

    fun deleteDownloadedFile(fileId: String) {
        val file = _completedFiles.value.find { it.id == fileId }
        if (file != null) {
            try {
                File(file.filePath).delete()
            } catch (_: Exception) {}
            _completedFiles.value = _completedFiles.value.filterNot { it.id == fileId }
        }
    }

    private fun updateTask(taskId: String, transform: (DownloadTask) -> DownloadTask) {
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) transform(it) else it
        }
    }
}
