package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.TelegramBadgeRed
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.TelegramLightBlue
import com.example.ui.theme.TelegramWhite
import com.example.viewmodel.TeleGrabViewModel

@Composable
fun SettingsScreen(
    viewModel: TeleGrabViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val scrollState = rememberScrollState()

    var wifiOnly by remember { mutableStateOf(false) }
    var highSpeedTurbo by remember { mutableStateOf(true) }
    var maxThreads by remember { mutableIntStateOf(3) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(TelegramBlue),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentUser?.avatarUrl?.isNotBlank() == true) {
                        AsyncImage(
                            model = currentUser!!.avatarUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User",
                            tint = TelegramWhite,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentUser?.displayName ?: "Guest User",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "@${currentUser?.username ?: "telegrab_user"}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TelegramLightBlue)
                    )
                    Text(
                        text = "Session: ${currentUser?.sessionType?.label ?: "Disconnected"}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.logout() },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("logout_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = TelegramBadgeRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Logout", color = TelegramBadgeRed, fontSize = 12.sp)
                }
            }
        }

        // Download Preferences
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Download Engine Settings",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Speed, contentDescription = null, tint = TelegramBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Turbo Chunk Acceleration", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Multi-part connection for maximum speed", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }
                    }
                    Switch(
                        checked = highSpeedTurbo,
                        onCheckedChange = { highSpeedTurbo = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = TelegramBlue)
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Wifi, contentDescription = null, tint = TelegramLightBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Download over Wi-Fi Only", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Prevent mobile data usage on large batch files", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }
                    }
                    Switch(
                        checked = wifiOnly,
                        onCheckedChange = { wifiOnly = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = TelegramBlue)
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CloudDownload, contentDescription = null, tint = TelegramBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Concurrent Streams", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Number of files downloaded in parallel ($maxThreads)", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(1, 3, 5).forEach { count ->
                            Button(
                                onClick = { maxThreads = count },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (maxThreads == count) TelegramBlue else MaterialTheme.colorScheme.surfaceVariant
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("$count", fontSize = 12.sp, color = if (maxThreads == count) TelegramWhite else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // Storage & Cache Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Storage & App Cache",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Folder, contentDescription = null, tint = TelegramBlue)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Download Directory", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text("/Internal Storage/Android/data/TeleGrab/files/telegram_downloads", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        viewModel.clearCompleted()
                        viewModel.showToast("Temporary cache cleaned")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.CleaningServices, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Clear Temporary Cache", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        // About Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = TelegramBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("About TeleGrab v1.0", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Engineered for high-volume Telegram group media downloading, bulk document archiving, and media vault management.",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}
