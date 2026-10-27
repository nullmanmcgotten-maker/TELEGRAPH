package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.TelegramBadgeGreen
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.TelegramDarkBlue
import com.example.ui.theme.TelegramLightBlue
import com.example.ui.theme.TelegramWhite
import com.example.viewmodel.TeleGrabViewModel

@Composable
fun AuthScreen(
    viewModel: TeleGrabViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Phone, 1: Bot Token, 2: Public Channel

    // Phone fields
    var phoneNumber by remember { mutableStateOf("+1 555 019 2834") }
    var smsCode by remember { mutableStateOf("") }
    var codeSent by remember { mutableStateOf(false) }

    // Bot Token field
    var botToken by remember { mutableStateOf("") }

    // Public link field
    var publicHandle by remember { mutableStateOf("@android_dev_hub") }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Image Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.telegrab_banner_1793095811055),
                contentDescription = "TeleGrab Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                androidx.compose.ui.graphics.Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(TelegramBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = "TeleGrab",
                            tint = TelegramWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "TeleGrab",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "High-speed Telegram Group Media & Batch Downloader",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Demo Connect Card (Instant one-tap entry)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .testTag("demo_login_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = TelegramBlue.copy(alpha = 0.12f)
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, TelegramBlue.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(TelegramBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Fast Demo",
                        tint = TelegramWhite
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Quick Demo Access",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Instant connect with preloaded groups, channels & media",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                }
                Button(
                    onClick = { viewModel.loginDemo() },
                    modifier = Modifier.testTag("quick_demo_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Enter", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Login Method Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Connect to Telegram",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Select your preferred connection method",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(14.dp))

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = TelegramBlue,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = TelegramBlue
                        )
                    },
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Phone Login", style = MaterialTheme.typography.labelSmall) },
                        icon = { Icon(Icons.Default.Phone, contentDescription = "Phone", modifier = Modifier.size(16.dp)) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Bot Token", style = MaterialTheme.typography.labelSmall) },
                        icon = { Icon(Icons.Default.SmartToy, contentDescription = "Bot Token", modifier = Modifier.size(16.dp)) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Public Channel", style = MaterialTheme.typography.labelSmall) },
                        icon = { Icon(Icons.Default.Link, contentDescription = "Public", modifier = Modifier.size(16.dp)) }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                when (selectedTab) {
                    0 -> {
                        // Phone Login
                        Column {
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = { Text("Telegram Phone Number") },
                                placeholder = { Text("+1 234 567 8900") },
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = "Phone", tint = TelegramBlue)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("phone_input_field"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            if (codeSent) {
                                OutlinedTextField(
                                    value = smsCode,
                                    onValueChange = { smsCode = it },
                                    label = { Text("Telegram Login Code (5 digits)") },
                                    placeholder = { Text("12345") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Lock, contentDescription = "Code", tint = TelegramBadgeGreen)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("sms_code_field"),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = { viewModel.loginPhone(phoneNumber, smsCode.ifBlank { "99999" }) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("verify_code_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Verify & Open Telegram Groups", fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Button(
                                    onClick = { codeSent = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("send_code_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Send Login Code", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    1 -> {
                        // Bot Token Login
                        Column {
                            Text(
                                text = "Use a Telegram Bot Token from @BotFather to access groups where the bot is added.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = botToken,
                                onValueChange = { botToken = it },
                                label = { Text("Telegram Bot Token") },
                                placeholder = { Text("123456789:ABCdefGHIjklMNOpqrSTUvwxYZ") },
                                leadingIcon = {
                                    Icon(Icons.Default.Key, contentDescription = "Token", tint = TelegramLightBlue)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("bot_token_field"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    val tokenToUse = botToken.ifBlank { "819284712:AAH9fkl128X_TeleGrabBotKey" }
                                    viewModel.loginBotToken(tokenToUse)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("bot_token_login_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Connect Bot & Sync Groups", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    2 -> {
                        // Public Channel Lookup
                        Column {
                            Text(
                                text = "Enter any public channel username or link (e.g. @android_dev_hub or t.me/channel) to load and batch download all its media files directly.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = publicHandle,
                                onValueChange = { publicHandle = it },
                                label = { Text("Public Channel / Group Handle") },
                                placeholder = { Text("@channel_name or t.me/group") },
                                leadingIcon = {
                                    Icon(Icons.Default.Link, contentDescription = "Link", tint = TelegramBlue)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("public_handle_field"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    viewModel.addPublicChannel(publicHandle.ifBlank { "android_dev_hub" })
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("explore_public_channel_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Extract Files & Media", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
