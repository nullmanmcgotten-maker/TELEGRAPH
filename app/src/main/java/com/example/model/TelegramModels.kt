package com.example.model

enum class SessionType(val label: String) {
    PHONE_SESSION("Telegram Account"),
    BOT_TOKEN("Bot Token"),
    PUBLIC_LOOKUP("Public Group/Channel")
}

data class TelegramUser(
    val id: Long,
    val firstName: String,
    val lastName: String = "",
    val username: String = "",
    val phone: String = "",
    val avatarUrl: String = "",
    val sessionType: SessionType = SessionType.PHONE_SESSION,
    val isConnected: Boolean = true
) {
    val displayName: String
        get() = if (lastName.isNotBlank()) "$firstName $lastName" else firstName
}

enum class ChatType(val label: String) {
    GROUP("Group"),
    SUPERGROUP("Supergroup"),
    CHANNEL("Channel")
}

enum class MediaType(val label: String) {
    ALL("All Files"),
    PHOTO("Photos"),
    VIDEO("Videos"),
    DOCUMENT("Documents"),
    AUDIO("Audio & Music"),
    ARCHIVE("Archives"),
    APK("APKs")
}

data class TelegramGroup(
    val id: String,
    val title: String,
    val username: String,
    val description: String,
    val memberCount: Int,
    val avatarUrl: String,
    val chatType: ChatType,
    val totalMediaCount: Int,
    val totalSizeBytes: Long,
    val hasUnreadMedia: Boolean = true,
    val isPinned: Boolean = false
) {
    val formattedMembers: String
        get() = when {
            memberCount >= 1_000_000 -> String.format("%.1fM members", memberCount / 1_000_000f)
            memberCount >= 1_000 -> String.format("%.1fK members", memberCount / 1_000f)
            else -> "$memberCount members"
        }

    val formattedTotalSize: String
        get() = formatBytes(totalSizeBytes)
}

data class TelegramMediaItem(
    val id: String,
    val groupId: String,
    val groupName: String,
    val title: String,
    val fileName: String,
    val extension: String,
    val mediaType: MediaType,
    val sizeBytes: Long,
    val messageDate: String,
    val timestamp: Long,
    val senderName: String,
    val downloadUrl: String,
    val thumbnailUrl: String? = null,
    val durationSeconds: Int? = null,
    val resolution: String? = null,
    val mimeType: String,
    val isDownloaded: Boolean = false,
    val localFilePath: String? = null
) {
    val formattedSize: String
        get() = formatBytes(sizeBytes)

    val formattedDuration: String?
        get() = durationSeconds?.let { sec ->
            val m = sec / 60
            val s = sec % 60
            String.format("%02d:%02d", m, s)
        }
}

enum class DownloadStatus {
    PENDING,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    FAILED,
    CANCELLED
}

data class DownloadTask(
    val id: String,
    val mediaItem: TelegramMediaItem,
    val progress: Float = 0f,
    val downloadedBytes: Long = 0L,
    val totalBytes: Long = mediaItem.sizeBytes,
    val speedBytesPerSec: Long = 0L,
    val status: DownloadStatus = DownloadStatus.PENDING,
    val startedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val error: String? = null,
    val localFilePath: String? = null
) {
    val formattedSpeed: String
        get() = "${formatBytes(speedBytesPerSec)}/s"

    val formattedDownloaded: String
        get() = "${formatBytes(downloadedBytes)} / ${formatBytes(totalBytes)}"

    val etaSeconds: Long
        get() = if (speedBytesPerSec > 0 && downloadedBytes < totalBytes) {
            (totalBytes - downloadedBytes) / speedBytesPerSec
        } else 0L

    val formattedEta: String
        get() = if (etaSeconds > 0) "${etaSeconds}s remaining" else "Calculating..."
}

data class LocalDownloadedFile(
    val id: String,
    val fileName: String,
    val originalGroupName: String,
    val mediaType: MediaType,
    val sizeBytes: Long,
    val downloadedAt: Long,
    val filePath: String,
    val mimeType: String
) {
    val formattedSize: String
        get() = formatBytes(sizeBytes)
}

fun formatBytes(bytes: Long): String {
    if (bytes <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(bytes.toDouble()) / Math.log10(1024.0)).toInt().coerceIn(0, units.size - 1)
    val value = bytes / Math.pow(1024.0, digitGroups.toDouble())
    return if (digitGroups == 0) "$bytes B" else String.format("%.2f %s", value, units[digitGroups])
}
