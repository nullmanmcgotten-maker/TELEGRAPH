package com.example.data

import com.example.model.ChatType
import com.example.model.MediaType
import com.example.model.TelegramGroup
import com.example.model.TelegramMediaItem
import com.example.model.TelegramUser
import com.example.model.SessionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class TelegramDataRepository {

    private val _currentUser = MutableStateFlow<TelegramUser?>(null)
    val currentUser: StateFlow<TelegramUser?> = _currentUser.asStateFlow()

    private val sampleGroups = mutableListOf(
        TelegramGroup(
            id = "grp_android_devs",
            title = "Android & Kotlin Devs Community",
            username = "android_dev_hub",
            description = "Daily open-source libraries, Compose snippets, APK builds, architectural PDFs and technical keynote videos.",
            memberCount = 142800,
            avatarUrl = "https://images.unsplash.com/photo-1607252650355-f7fd0460ccdb?w=200",
            chatType = ChatType.SUPERGROUP,
            totalMediaCount = 164,
            totalSizeBytes = 3_420_000_000L,
            hasUnreadMedia = true,
            isPinned = true
        ),
        TelegramGroup(
            id = "grp_cinema_trailers",
            title = "Cinema 4K Trailers & Visuals",
            username = "cinemavault_official",
            description = "High bitrate ProRes & HEVC movie clips, film posters, behind-the-scenes B-roll and HDR wallpapers.",
            memberCount = 890400,
            avatarUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=200",
            chatType = ChatType.CHANNEL,
            totalMediaCount = 285,
            totalSizeBytes = 8_940_000_000L,
            hasUnreadMedia = true,
            isPinned = true
        ),
        TelegramGroup(
            id = "grp_tech_books",
            title = "Tech Books & Academic Papers",
            username = "tech_reading_vault",
            description = "Computer Science textbooks, modern software engineering references, PDF cheatsheets and research whitepapers.",
            memberCount = 54200,
            avatarUrl = "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=200",
            chatType = ChatType.GROUP,
            totalMediaCount = 98,
            totalSizeBytes = 1_820_000_000L,
            hasUnreadMedia = false,
            isPinned = false
        ),
        TelegramGroup(
            id = "grp_lofi_music",
            title = "Lo-Fi Beats & Soundtracks Vault",
            username = "lofi_chill_vault",
            description = "Uncompressed FLAC & 320kbps MP3 beats, soothing ambient tracks, audio stems and relaxing cover art.",
            memberCount = 310500,
            avatarUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=200",
            chatType = ChatType.CHANNEL,
            totalMediaCount = 142,
            totalSizeBytes = 2_150_000_000L,
            hasUnreadMedia = true,
            isPinned = false
        ),
        TelegramGroup(
            id = "grp_design_assets",
            title = "UI/UX & Graphic Design Kits",
            username = "figma_design_assets",
            description = "Vector icon packs, 3D illustrations, typography bundles, Figma UI templates and high-res mockups.",
            memberCount = 215400,
            avatarUrl = "https://images.unsplash.com/photo-1542744094-3a31f272c490?w=200",
            chatType = ChatType.SUPERGROUP,
            totalMediaCount = 110,
            totalSizeBytes = 4_580_000_000L,
            hasUnreadMedia = true,
            isPinned = false
        ),
        TelegramGroup(
            id = "grp_wallpapers_4k",
            title = "OLED & Minimal 8K Wallpapers",
            username = "oled_minimal_art",
            description = "Crisp, uncompressed photography, abstract generative art, landscapes and space exploration imagery.",
            memberCount = 478900,
            avatarUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=200",
            chatType = ChatType.CHANNEL,
            totalMediaCount = 320,
            totalSizeBytes = 1_450_000_000L,
            hasUnreadMedia = false,
            isPinned = false
        )
    )

    private val _groups = MutableStateFlow<List<TelegramGroup>>(sampleGroups)
    val groups: StateFlow<List<TelegramGroup>> = _groups.asStateFlow()

    private val mediaItemsDatabase = mutableMapOf<String, List<TelegramMediaItem>>()

    init {
        generateInitialMedia()
    }

    private fun generateInitialMedia() {
        mediaItemsDatabase["grp_android_devs"] = listOf(
            TelegramMediaItem(
                id = "m_and_1",
                groupId = "grp_android_devs",
                groupName = "Android & Kotlin Devs Community",
                title = "Jetpack Compose Performance Handbook 2026",
                fileName = "Compose_Performance_Guide_v3.pdf",
                extension = "pdf",
                mediaType = MediaType.DOCUMENT,
                sizeBytes = 24_850_000L,
                messageDate = "Today at 09:42 AM",
                timestamp = System.currentTimeMillis() - 3600_000 * 3,
                senderName = "Google DevRel Bot",
                downloadUrl = "https://raw.githubusercontent.com/android/architecture-samples/main/README.md",
                thumbnailUrl = null,
                mimeType = "application/pdf"
            ),
            TelegramMediaItem(
                id = "m_and_2",
                groupId = "grp_android_devs",
                groupName = "Android & Kotlin Devs Community",
                title = "Kotlin Coroutines & Flow Internals Masterclass",
                fileName = "Coroutines_Deep_Dive_1080p.mp4",
                extension = "mp4",
                mediaType = MediaType.VIDEO,
                sizeBytes = 142_500_000L,
                messageDate = "Today at 08:15 AM",
                timestamp = System.currentTimeMillis() - 3600_000 * 5,
                senderName = "Roman E.",
                downloadUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                thumbnailUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=400",
                durationSeconds = 1420,
                resolution = "1080p • 60fps",
                mimeType = "video/mp4"
            ),
            TelegramMediaItem(
                id = "m_and_3",
                groupId = "grp_android_devs",
                groupName = "Android & Kotlin Devs Community",
                title = "CameraX & MLKit Scanner Preview Sample APK",
                fileName = "CameraX_Scanner_v2.4.1_release.apk",
                extension = "apk",
                mediaType = MediaType.APK,
                sizeBytes = 48_300_000L,
                messageDate = "Yesterday at 11:30 PM",
                timestamp = System.currentTimeMillis() - 3600_000 * 12,
                senderName = "Build Bot (CI)",
                downloadUrl = "https://raw.githubusercontent.com/android/architecture-samples/main/gradlew",
                thumbnailUrl = null,
                mimeType = "application/vnd.android.package-archive"
            ),
            TelegramMediaItem(
                id = "m_and_4",
                groupId = "grp_android_devs",
                groupName = "Android & Kotlin Devs Community",
                title = "Material 3 Adaptive Layouts Architecture Blueprint",
                fileName = "M3_Adaptive_WindowSizeClasses.png",
                extension = "png",
                mediaType = MediaType.PHOTO,
                sizeBytes = 5_240_000L,
                messageDate = "Yesterday at 04:12 PM",
                timestamp = System.currentTimeMillis() - 3600_000 * 20,
                senderName = "Sarah Chen (UI Lead)",
                downloadUrl = "https://images.unsplash.com/photo-1581291518857-4e27b48ff24e?w=1200",
                thumbnailUrl = "https://images.unsplash.com/photo-1581291518857-4e27b48ff24e?w=400",
                resolution = "3840x2160",
                mimeType = "image/png"
            ),
            TelegramMediaItem(
                id = "m_and_5",
                groupId = "grp_android_devs",
                groupName = "Android & Kotlin Devs Community",
                title = "Full Clean Architecture Starter Kit Template",
                fileName = "CleanArchitecture_Compose_KMP.zip",
                extension = "zip",
                mediaType = MediaType.ARCHIVE,
                sizeBytes = 86_200_000L,
                messageDate = "Oct 25 at 02:00 PM",
                timestamp = System.currentTimeMillis() - 3600_000 * 48,
                senderName = "OpenSource Contributor",
                downloadUrl = "https://raw.githubusercontent.com/android/architecture-samples/main/gradle/libs.versions.toml",
                thumbnailUrl = null,
                mimeType = "application/zip"
            ),
            TelegramMediaItem(
                id = "m_and_6",
                groupId = "grp_android_devs",
                groupName = "Android & Kotlin Devs Community",
                title = "Android Dev Podcast Ep 245 - The Future of Android 16",
                fileName = "ADB_Ep245_Android16_Preview.mp3",
                extension = "mp3",
                mediaType = MediaType.AUDIO,
                sizeBytes = 64_100_000L,
                messageDate = "Oct 24 at 10:15 AM",
                timestamp = System.currentTimeMillis() - 3600_000 * 72,
                senderName = "Chet & Tor",
                downloadUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                thumbnailUrl = "https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=400",
                durationSeconds = 2740,
                mimeType = "audio/mpeg"
            )
        )

        mediaItemsDatabase["grp_cinema_trailers"] = listOf(
            TelegramMediaItem(
                id = "m_cin_1",
                groupId = "grp_cinema_trailers",
                groupName = "Cinema 4K Trailers & Visuals",
                title = "Interstellar 10th Anniversary IMAX Re-Release Clip",
                fileName = "Interstellar_IMAX_4K_ProRes.mp4",
                extension = "mp4",
                mediaType = MediaType.VIDEO,
                sizeBytes = 412_000_000L,
                messageDate = "Today at 10:00 AM",
                timestamp = System.currentTimeMillis() - 3600_000 * 2,
                senderName = "CinemaVault Admin",
                downloadUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                thumbnailUrl = "https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=600",
                durationSeconds = 195,
                resolution = "3840x2160 • HDR10",
                mimeType = "video/mp4"
            ),
            TelegramMediaItem(
                id = "m_cin_2",
                groupId = "grp_cinema_trailers",
                groupName = "Cinema 4K Trailers & Visuals",
                title = "Cyberpunk Neo-Tokyo Official Teaser 4K",
                fileName = "NeoTokyo_Teaser_DolbyVision.mp4",
                extension = "mp4",
                mediaType = MediaType.VIDEO,
                sizeBytes = 285_400_000L,
                messageDate = "Today at 07:30 AM",
                timestamp = System.currentTimeMillis() - 3600_000 * 6,
                senderName = "Studio G-Force",
                downloadUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                thumbnailUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600",
                durationSeconds = 142,
                resolution = "3840x2160 • 60fps",
                mimeType = "video/mp4"
            ),
            TelegramMediaItem(
                id = "m_cin_3",
                groupId = "grp_cinema_trailers",
                groupName = "Cinema 4K Trailers & Visuals",
                title = "Official Theatrical Poster Key Art Pack (8K)",
                fileName = "Theatrical_Poster_Original_8K.png",
                extension = "png",
                mediaType = MediaType.PHOTO,
                sizeBytes = 38_500_000L,
                messageDate = "Yesterday at 09:10 PM",
                timestamp = System.currentTimeMillis() - 3600_000 * 15,
                senderName = "Poster Archive Bot",
                downloadUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1600",
                thumbnailUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=400",
                resolution = "7680x4320",
                mimeType = "image/png"
            ),
            TelegramMediaItem(
                id = "m_cin_4",
                groupId = "grp_cinema_trailers",
                groupName = "Cinema 4K Trailers & Visuals",
                title = "Director's Production Notes & Concept Art Booklet",
                fileName = "Directors_Vision_Script_Booklet.pdf",
                extension = "pdf",
                mediaType = MediaType.DOCUMENT,
                sizeBytes = 74_200_000L,
                messageDate = "Yesterday at 01:20 PM",
                timestamp = System.currentTimeMillis() - 3600_000 * 22,
                senderName = "Christopher N.",
                downloadUrl = "https://raw.githubusercontent.com/android/architecture-samples/main/README.md",
                thumbnailUrl = null,
                mimeType = "application/pdf"
            ),
            TelegramMediaItem(
                id = "m_cin_5",
                groupId = "grp_cinema_trailers",
                groupName = "Cinema 4K Trailers & Visuals",
                title = "Original Orchestral Score Main Theme (24-bit FLAC)",
                fileName = "Main_Title_Symphonic_Suite_FLAC.wav",
                extension = "wav",
                mediaType = MediaType.AUDIO,
                sizeBytes = 92_600_000L,
                messageDate = "Oct 25 at 06:45 PM",
                timestamp = System.currentTimeMillis() - 3600_000 * 42,
                senderName = "Hans Z.",
                downloadUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
                thumbnailUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400",
                durationSeconds = 385,
                mimeType = "audio/wav"
            )
        )

        mediaItemsDatabase["grp_tech_books"] = listOf(
            TelegramMediaItem(
                id = "m_tb_1",
                groupId = "grp_tech_books",
                groupName = "Tech Books & Academic Papers",
                title = "Designing Data-Intensive Applications (2nd Ed Summary)",
                fileName = "DDIA_Comprehensive_Notes.pdf",
                extension = "pdf",
                mediaType = MediaType.DOCUMENT,
                sizeBytes = 18_400_000L,
                messageDate = "Today at 06:10 AM",
                timestamp = System.currentTimeMillis() - 3600_000 * 4,
                senderName = "Martin K.",
                downloadUrl = "https://raw.githubusercontent.com/android/architecture-samples/main/README.md",
                thumbnailUrl = null,
                mimeType = "application/pdf"
            ),
            TelegramMediaItem(
                id = "m_tb_2",
                groupId = "grp_tech_books",
                groupName = "Tech Books & Academic Papers",
                title = "Distributed Systems Consensus Algorithms Whitepaper",
                fileName = "Raft_Paxos_Consensus_Whitepaper.pdf",
                extension = "pdf",
                mediaType = MediaType.DOCUMENT,
                sizeBytes = 12_800_000L,
                messageDate = "Yesterday at 10:20 AM",
                timestamp = System.currentTimeMillis() - 3600_000 * 25,
                senderName = "Stanford Distributed Lab",
                downloadUrl = "https://raw.githubusercontent.com/android/architecture-samples/main/README.md",
                thumbnailUrl = null,
                mimeType = "application/pdf"
            ),
            TelegramMediaItem(
                id = "m_tb_3",
                groupId = "grp_tech_books",
                groupName = "Tech Books & Academic Papers",
                title = "Computer Graphics Principles & Vulkan Shaders",
                fileName = "Vulkan_Pipeline_Handbook.pdf",
                extension = "pdf",
                mediaType = MediaType.DOCUMENT,
                sizeBytes = 42_100_000L,
                messageDate = "Oct 24 at 03:40 PM",
                timestamp = System.currentTimeMillis() - 3600_000 * 65,
                senderName = "Khronos Group Bot",
                downloadUrl = "https://raw.githubusercontent.com/android/architecture-samples/main/README.md",
                thumbnailUrl = null,
                mimeType = "application/pdf"
            )
        )

        mediaItemsDatabase["grp_lofi_music"] = listOf(
            TelegramMediaItem(
                id = "m_lofi_1",
                groupId = "grp_lofi_music",
                groupName = "Lo-Fi Beats & Soundtracks Vault",
                title = "Midnight Coffee in Shibuya (Chillhop Edition)",
                fileName = "Midnight_Coffee_Shibuya_320k.mp3",
                extension = "mp3",
                mediaType = MediaType.AUDIO,
                sizeBytes = 14_200_000L,
                messageDate = "Today at 11:15 AM",
                timestamp = System.currentTimeMillis() - 3600_000 * 1,
                senderName = "Nujabes Tribute",
                downloadUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
                thumbnailUrl = "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?w=400",
                durationSeconds = 245,
                mimeType = "audio/mpeg"
            ),
            TelegramMediaItem(
                id = "m_lofi_2",
                groupId = "grp_lofi_music",
                groupName = "Lo-Fi Beats & Soundtracks Vault",
                title = "Rainy Window Coding Session (Lo-Fi Tape Vol 4)",
                fileName = "Rainy_Window_LoFi_Tape_Vol4.mp3",
                extension = "mp3",
                mediaType = MediaType.AUDIO,
                sizeBytes = 28_400_000L,
                messageDate = "Yesterday at 08:30 PM",
                timestamp = System.currentTimeMillis() - 3600_000 * 14,
                senderName = "Chillhop Records",
                downloadUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
                thumbnailUrl = "https://images.unsplash.com/photo-1518495973542-4542c06a5843?w=400",
                durationSeconds = 480,
                mimeType = "audio/mpeg"
            ),
            TelegramMediaItem(
                id = "m_lofi_3",
                groupId = "grp_lofi_music",
                groupName = "Lo-Fi Beats & Soundtracks Vault",
                title = "Neon Vinyl Cover Art High Resolution Artwork",
                fileName = "Vinyl_Cover_Neon_LoFi.jpg",
                extension = "jpg",
                mediaType = MediaType.PHOTO,
                sizeBytes = 8_900_000L,
                messageDate = "Oct 25 at 11:00 AM",
                timestamp = System.currentTimeMillis() - 3600_000 * 45,
                senderName = "Art Director",
                downloadUrl = "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?w=1200",
                thumbnailUrl = "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?w=400",
                resolution = "3000x3000",
                mimeType = "image/jpeg"
            )
        )

        mediaItemsDatabase["grp_design_assets"] = listOf(
            TelegramMediaItem(
                id = "m_ds_1",
                groupId = "grp_design_assets",
                groupName = "UI/UX & Graphic Design Kits",
                title = "3D Glassmorphism Icon Set (240 Icons)",
                fileName = "3D_Glassmorphic_Icons_Pack.zip",
                extension = "zip",
                mediaType = MediaType.ARCHIVE,
                sizeBytes = 185_000_000L,
                messageDate = "Today at 05:40 AM",
                timestamp = System.currentTimeMillis() - 3600_000 * 6,
                senderName = "VectorLab Studio",
                downloadUrl = "https://raw.githubusercontent.com/android/architecture-samples/main/README.md",
                thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=400",
                mimeType = "application/zip"
            ),
            TelegramMediaItem(
                id = "m_ds_2",
                groupId = "grp_design_assets",
                groupName = "UI/UX & Graphic Design Kits",
                title = "SaaS Analytics Dashboard UI Kit for Mobile & Tablet",
                fileName = "SaaS_Analytics_Figma_Kit.zip",
                extension = "zip",
                mediaType = MediaType.ARCHIVE,
                sizeBytes = 124_000_000L,
                messageDate = "Yesterday at 06:15 PM",
                timestamp = System.currentTimeMillis() - 3600_000 * 18,
                senderName = "UI Masterclass",
                downloadUrl = "https://raw.githubusercontent.com/android/architecture-samples/main/README.md",
                thumbnailUrl = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=400",
                mimeType = "application/zip"
            )
        )

        mediaItemsDatabase["grp_wallpapers_4k"] = listOf(
            TelegramMediaItem(
                id = "m_wp_1",
                groupId = "grp_wallpapers_4k",
                groupName = "OLED & Minimal 8K Wallpapers",
                title = "James Webb Deep Space Nebula 8K Wallpaper",
                fileName = "Webb_Carina_Nebula_8K_OLED.jpg",
                extension = "jpg",
                mediaType = MediaType.PHOTO,
                sizeBytes = 18_400_000L,
                messageDate = "Today at 10:45 AM",
                timestamp = System.currentTimeMillis() - 3600_000 * 1,
                senderName = "Cosmos Bot",
                downloadUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=1600",
                thumbnailUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=400",
                resolution = "7680x4320",
                mimeType = "image/jpeg"
            ),
            TelegramMediaItem(
                id = "m_wp_2",
                groupId = "grp_wallpapers_4k",
                groupName = "OLED & Minimal 8K Wallpapers",
                title = "Aurora Borealis over Icelandic Fjord",
                fileName = "Iceland_Aurora_Reflections_4K.jpg",
                extension = "jpg",
                mediaType = MediaType.PHOTO,
                sizeBytes = 12_600_000L,
                messageDate = "Yesterday at 03:00 PM",
                timestamp = System.currentTimeMillis() - 3600_000 * 20,
                senderName = "Nordic Lens",
                downloadUrl = "https://images.unsplash.com/photo-1517411032315-54ef2cb783bb?w=1600",
                thumbnailUrl = "https://images.unsplash.com/photo-1517411032315-54ef2cb783bb?w=400",
                resolution = "3840x2160",
                mimeType = "image/jpeg"
            )
        )
    }

    fun getGroupMedia(groupId: String): List<TelegramMediaItem> {
        return mediaItemsDatabase[groupId] ?: emptyList()
    }

    fun loginWithPhone(phoneNumber: String): Boolean {
        val cleanNumber = phoneNumber.trim()
        val digits = cleanNumber.filter { it.isDigit() }
        val displayPhone = if (cleanNumber.startsWith("+")) cleanNumber else "+$cleanNumber"
        val suffix = if (digits.length >= 4) digits.takeLast(4) else "user"
        _currentUser.value = TelegramUser(
            id = Math.abs(cleanNumber.hashCode().toLong()).coerceAtLeast(100000L),
            firstName = "Telegram User",
            lastName = "",
            username = "tg_$suffix",
            phone = displayPhone,
            avatarUrl = "",
            sessionType = SessionType.PHONE_SESSION
        )
        return true
    }

    fun loginWithBotToken(token: String): Boolean {
        val botName = if (token.contains(":")) "TeleGrab_Helper_Bot" else "TelegramBot"
        _currentUser.value = TelegramUser(
            id = 9988776655L,
            firstName = botName,
            username = "telegrab_bot",
            avatarUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=150",
            sessionType = SessionType.BOT_TOKEN
        )
        return true
    }

    fun addPublicGroupOrChannel(handleOrUrl: String): TelegramGroup {
        val cleanHandle = handleOrUrl.trim()
            .removePrefix("https://t.me/")
            .removePrefix("http://t.me/")
            .removePrefix("t.me/")
            .removePrefix("@")

        val newId = "grp_public_${UUID.randomUUID().toString().take(6)}"
        val formattedTitle = cleanHandle.replace("_", " ").split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
            .ifBlank { "Telegram Channel" }

        val newGroup = TelegramGroup(
            id = newId,
            title = "$formattedTitle (Public)",
            username = cleanHandle,
            description = "Public Telegram channel loaded via @$cleanHandle. Ready for batch media extraction.",
            memberCount = (5000..95000).random(),
            avatarUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200",
            chatType = ChatType.CHANNEL,
            totalMediaCount = 18,
            totalSizeBytes = 540_000_000L,
            hasUnreadMedia = true,
            isPinned = false
        )

        // Generate synthetic public media for this channel
        val items = listOf(
            TelegramMediaItem(
                id = "m_${newId}_1",
                groupId = newId,
                groupName = newGroup.title,
                title = "$formattedTitle Announcement & Media Reel",
                fileName = "${cleanHandle}_Promo_1080p.mp4",
                extension = "mp4",
                mediaType = MediaType.VIDEO,
                sizeBytes = 78_400_000L,
                messageDate = "Just now",
                timestamp = System.currentTimeMillis(),
                senderName = "@$cleanHandle Admin",
                downloadUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                thumbnailUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=400",
                durationSeconds = 120,
                resolution = "1080p",
                mimeType = "video/mp4"
            ),
            TelegramMediaItem(
                id = "m_${newId}_2",
                groupId = newId,
                groupName = newGroup.title,
                title = "$formattedTitle High Resolution Banner",
                fileName = "${cleanHandle}_Banner_Art.jpg",
                extension = "jpg",
                mediaType = MediaType.PHOTO,
                sizeBytes = 6_200_000L,
                messageDate = "10 mins ago",
                timestamp = System.currentTimeMillis() - 600_000,
                senderName = "@$cleanHandle Admin",
                downloadUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=1200",
                thumbnailUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=400",
                resolution = "2560x1440",
                mimeType = "image/jpeg"
            ),
            TelegramMediaItem(
                id = "m_${newId}_3",
                groupId = newId,
                groupName = newGroup.title,
                title = "$formattedTitle Resource Pack & Documentation",
                fileName = "${cleanHandle}_Official_Guide.pdf",
                extension = "pdf",
                mediaType = MediaType.DOCUMENT,
                sizeBytes = 15_800_000L,
                messageDate = "1 hour ago",
                timestamp = System.currentTimeMillis() - 3600_000,
                senderName = "@$cleanHandle Admin",
                downloadUrl = "https://raw.githubusercontent.com/android/architecture-samples/main/README.md",
                thumbnailUrl = null,
                mimeType = "application/pdf"
            ),
            TelegramMediaItem(
                id = "m_${newId}_4",
                groupId = newId,
                groupName = newGroup.title,
                title = "$formattedTitle Release Archive",
                fileName = "${cleanHandle}_Archive_v1.0.zip",
                extension = "zip",
                mediaType = MediaType.ARCHIVE,
                sizeBytes = 45_200_000L,
                messageDate = "3 hours ago",
                timestamp = System.currentTimeMillis() - 3600_000 * 3,
                senderName = "@$cleanHandle Admin",
                downloadUrl = "https://raw.githubusercontent.com/android/architecture-samples/main/gradle/libs.versions.toml",
                thumbnailUrl = null,
                mimeType = "application/zip"
            )
        )

        mediaItemsDatabase[newId] = items
        val current = _groups.value.toMutableList()
        current.add(0, newGroup)
        _groups.value = current
        return newGroup
    }

    fun logout() {
        _currentUser.value = null
    }
}
