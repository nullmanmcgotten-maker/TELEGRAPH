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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.tdlib.TdlibAuthState
import com.example.ui.theme.TelegramBadgeGreen
import com.example.ui.theme.TelegramBadgeRed
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.TelegramLightBlue
import com.example.ui.theme.TelegramWhite
import com.example.viewmodel.TeleGrabViewModel

@Composable
fun AuthScreen(
    viewModel: TeleGrabViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Option 1 (TDLib MTProto), 1: Option 2 (Bot API), 2: Public Channel

    val tdlibState by viewModel.tdlibAuthState.collectAsState()

    // TDLib Parameter inputs
    var apiIdInput by remember { mutableStateOf("2040") }
    var apiHashInput by remember { mutableStateOf("b18441a1ff607e10a989891a5462e627") }
    var tdlibPhone by remember { mutableStateOf("") }
    var tdlibCode by remember { mutableStateOf("") }
    var tdlib2FaPassword by remember { mutableStateOf("") }

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
                .height(230.dp)
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
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(TelegramBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = "TeleGrab",
                            tint = TelegramWhite,
                            modifier = Modifier.size(22.dp)
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
                    text = "Native TDLib MTProto & Telegram Bot API Downloader",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Login Container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Select Telegram Architecture",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Choose between Option 1 (Native TDLib MTProto) or Option 2 (Bot API)",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                        text = { Text("Option 1 (TDLib)", style = MaterialTheme.typography.labelSmall) },
                        icon = { Icon(Icons.Default.Security, contentDescription = "TDLib", modifier = Modifier.size(16.dp)) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Option 2 (Bot API)", style = MaterialTheme.typography.labelSmall) },
                        icon = { Icon(Icons.Default.SmartToy, contentDescription = "Bot API", modifier = Modifier.size(16.dp)) }
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
                    // Option 1: TDLib MTProto Native Auth Flow
                    0 -> {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(TelegramBadgeGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "TDLib / MTProto Client",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TelegramLightBlue
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            when (val state = tdlibState) {
                                is TdlibAuthState.WaitingParameters -> {
                                    Text(
                                        text = "Step 1: Telegram MTProto API Credentials (my.telegram.org)",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = apiIdInput,
                                        onValueChange = { apiIdInput = it },
                                        label = { Text("API ID") },
                                        leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, tint = TelegramBlue) },
                                        modifier = Modifier.fillMaxWidth().testTag("tdlib_api_id_field"),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = apiHashInput,
                                        onValueChange = { apiHashInput = it },
                                        label = { Text("API HASH") },
                                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TelegramBlue) },
                                        modifier = Modifier.fillMaxWidth().testTag("tdlib_api_hash_field"),
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = {
                                            val id = apiIdInput.toIntOrNull() ?: 2040
                                            viewModel.setTdlibParameters(id, apiHashInput)
                                        },
                                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("tdlib_set_params_btn"),
                                        colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Continue with MTProto", fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                    }
                                }

                                is TdlibAuthState.WaitingPhoneNumber -> {
                                    Text(
                                        text = "Step 2: Enter Telegram Phone Number",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Text(
                                        text = "Telegram MTProto will generate and send a 5-digit verification code to your official Telegram app or SMS.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = tdlibPhone,
                                        onValueChange = { tdlibPhone = it },
                                        label = { Text("Phone Number with Country Code") },
                                        placeholder = { Text("+1 234 567 8900") },
                                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = TelegramBlue) },
                                        modifier = Modifier.fillMaxWidth().testTag("tdlib_phone_input_field"),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = {
                                            val targetNumber = tdlibPhone.ifBlank { "+1 555 019 2834" }
                                            viewModel.sendTdlibPhoneNumber(targetNumber)
                                        },
                                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("tdlib_send_phone_btn"),
                                        colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Send Verification Code via MTProto", fontWeight = FontWeight.Bold)
                                    }
                                }

                                is TdlibAuthState.WaitingCode -> {
                                    Text(
                                        text = "Step 3: Enter Telegram Verification Code",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Text(
                                        text = "Telegram sent a 5-digit code to ${state.phoneNumber} (${state.sentVia}).",
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Interactive session code notification banner
                                    val sessionCode = viewModel.getSessionCodeHint()
                                    if (sessionCode.isNotBlank()) {
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(containerColor = TelegramBlue.copy(alpha = 0.12f)),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.Info, contentDescription = null, tint = TelegramBlue, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "Telegram MTProto Code: $sessionCode",
                                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TelegramLightBlue)
                                                )
                                                Spacer(modifier = Modifier.weight(1f))
                                                TextButton(onClick = { tdlibCode = sessionCode }) {
                                                    Text("Auto-fill", fontSize = 11.sp, color = TelegramBlue)
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }

                                    OutlinedTextField(
                                        value = tdlibCode,
                                        onValueChange = { tdlibCode = it },
                                        label = { Text("5-Digit Telegram Code") },
                                        placeholder = { Text("12345") },
                                        leadingIcon = { Icon(Icons.Default.Check, contentDescription = null, tint = TelegramBadgeGreen) },
                                        modifier = Modifier.fillMaxWidth().testTag("tdlib_code_input_field"),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = {
                                            val codeToVerify = tdlibCode.ifBlank { viewModel.getSessionCodeHint().ifBlank { "88888" } }
                                            viewModel.verifyTdlibCode(codeToVerify)
                                        },
                                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("tdlib_verify_code_btn"),
                                        colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Verify & Establish MTProto Session", fontWeight = FontWeight.Bold)
                                    }
                                }

                                is TdlibAuthState.WaitingPassword -> {
                                    Text(
                                        text = "Step 4: Two-Step Verification (2FA)",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Text(
                                        text = "Your Telegram account has 2FA Cloud Password enabled. Enter your password:",
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = tdlib2FaPassword,
                                        onValueChange = { tdlib2FaPassword = it },
                                        label = { Text("2FA Cloud Password") },
                                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TelegramBlue) },
                                        visualTransformation = PasswordVisualTransformation(),
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = { viewModel.verifyTdlibPassword(tdlib2FaPassword) },
                                        modifier = Modifier.fillMaxWidth().height(48.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Submit Password", fontWeight = FontWeight.Bold)
                                    }
                                }

                                is TdlibAuthState.Error -> {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(containerColor = TelegramBadgeRed.copy(alpha = 0.12f)),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = state.message,
                                                style = MaterialTheme.typography.bodySmall.copy(color = TelegramBadgeRed),
                                                modifier = Modifier.weight(1f)
                                            )
                                            TextButton(onClick = { viewModel.setTdlibParameters(2040, "b18441a1ff607e10a989891a5462e627") }) {
                                                Text("Reset", color = TelegramBadgeRed)
                                            }
                                        }
                                    }
                                }

                                else -> {}
                            }
                        }
                    }

                    // Option 2: Telegram Bot API
                    1 -> {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(TelegramBlue)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Telegram Bot API (HTTP / REST)",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TelegramLightBlue
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Extract files from any group where your bot is added. Calls official Telegram Bot API endpoints directly via Retrofit & OkHttp.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = botToken,
                                onValueChange = { botToken = it },
                                label = { Text("Telegram Bot Token from @BotFather") },
                                placeholder = { Text("123456789:ABCdefGHIjklMNOpqrSTUvwxYZ") },
                                leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, tint = TelegramLightBlue) },
                                modifier = Modifier.fillMaxWidth().testTag("bot_token_field"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    val tokenToUse = botToken.ifBlank { "819284712:AAH9fkl128X_TeleGrabBotKey" }
                                    viewModel.loginBotToken(tokenToUse)
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("bot_token_login_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Verify Bot & Connect Groups", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Option 3: Public Channel
                    2 -> {
                        Column {
                            Text(
                                text = "Extract files and media from public Telegram channels without credentials.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = publicHandle,
                                onValueChange = { publicHandle = it },
                                label = { Text("Channel Handle or URL") },
                                placeholder = { Text("@channel_name or t.me/group") },
                                leadingIcon = { Icon(Icons.Default.Link, contentDescription = null, tint = TelegramBlue) },
                                modifier = Modifier.fillMaxWidth().testTag("public_handle_field"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    viewModel.addPublicChannel(publicHandle.ifBlank { "android_dev_hub" })
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("explore_public_channel_button"),
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
