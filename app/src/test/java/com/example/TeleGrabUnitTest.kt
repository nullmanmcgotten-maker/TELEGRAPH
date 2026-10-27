package com.example

import com.example.data.TelegramDataRepository
import com.example.model.MediaType
import com.example.model.formatBytes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TeleGrabUnitTest {

    @Test
    fun testFormatBytes() {
        assertEquals("0 B", formatBytes(0L))
        assertEquals("500 B", formatBytes(500L))
        assertEquals("1.00 KB", formatBytes(1024L))
        assertEquals("1.00 MB", formatBytes(1024L * 1024L))
        assertEquals("1.50 GB", formatBytes((1.5 * 1024 * 1024 * 1024).toLong()))
    }

    @Test
    fun testRepositoryInitialData() {
        val repo = TelegramDataRepository()
        val groups = repo.groups.value
        assertTrue("Should have initial groups", groups.isNotEmpty())

        val firstGroup = groups.first()
        val media = repo.getGroupMedia(firstGroup.id)
        assertTrue("First group should have media files", media.isNotEmpty())
    }

    @Test
    fun testAddPublicChannel() {
        val repo = TelegramDataRepository()
        val group = repo.addPublicGroupOrChannel("@tech_daily_updates")
        assertNotNull(group)
        assertEquals("tech_daily_updates", group.username)

        val media = repo.getGroupMedia(group.id)
        assertTrue("Synthetic media generated for public group", media.isNotEmpty())
    }
}
