package com.subhadip.subhuplay

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.compose.material3.darkColorScheme
import coil.compose.AsyncImage
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject

private val Canvas = Color(0xFF0B1020)
private val Panel = Color(0xFF151D32)
private val PanelSoft = Color(0xFF1C2841)
private val Violet = Color(0xFF9B8CFF)
private val Mint = Color(0xFF62E6C5)
private val Muted = Color(0xFFA4AEC6)

data class UserProfile(val displayName: String, val email: String)
data class VideoResult(
    val videoId: String,
    val title: String,
    val channel: String,
    val description: String,
    val thumbnail: String
)

enum class MainTab(val label: String) {
    DISCOVER("Discover"),
    NOTES("Study notes"),
    SAVED("Saved"),
    PROFILE("Profile")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("subhuplay_profile", Context.MODE_PRIVATE)
        val initialProfile = prefs.getString("email", null)?.let {
            UserProfile(prefs.getString("name", "Viewer") ?: "Viewer", it)
        }

        setContent {
            var profile by remember { mutableStateOf(initialProfile) }
            SubhuPlayTheme {
                if (profile == null) {
                    SignInScreen(
                        onSignedIn = { signedIn ->
                            prefs.edit()
                                .putString("name", signedIn.displayName)
                                .putString("email", signedIn.email)
                                .apply()
                            profile = signedIn
                        }
                    )
                } else {
                    MainApp(
                        profile = profile!!,
                        onSignOut = {
                            prefs.edit().clear().apply()
                            profile = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SubhuPlayTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Violet,
            secondary = Mint,
            background = Canvas,
            surface = Panel,
            onBackground = Color.White,
            onSurface = Color.White
        ),
        content = content
    )
}

@Composable
private fun SignInScreen(onSignedIn: (UserProfile) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var busy by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("Sign in to keep your learning space on this device.") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF171C38), Canvas, Color(0xFF0B1020))))
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(PanelSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_subhuplay_foreground),
                    contentDescription = "SubhuPlay mark",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(86.dp)
                )
            }
            Spacer(Modifier.height(22.dp))
            Text("SubhuPlay", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.ExtraBold)
            Text("Discover videos. Keep what you learn.", color = Muted, fontSize = 14.sp)
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    if (BuildConfig.GOOGLE_WEB_CLIENT_ID.isBlank()) {
                        message = "Google sign-in is not configured yet. Add GOOGLE_WEB_CLIENT_ID to your local Gradle properties."
                    } else {
                        scope.launch {
                            busy = true
                            message = "Connecting securely to Google…"
                            try {
                                val option = GetSignInWithGoogleOption.Builder(
                                    serverClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
                                ).build()
                                val request = GetCredentialRequest.Builder()
                                    .addCredentialOption(option)
                                    .build()
                                val result = CredentialManager.create(context).getCredential(
                                    context = context,
                                    request = request
                                )
                                val credential = result.credential
                                if (credential is CustomCredential &&
                                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                                ) {
                                    val google = GoogleIdTokenCredential.createFrom(credential.data)
                                    val displayName = google.displayName?.takeIf { it.isNotBlank() } ?: "Viewer"
                                    onSignedIn(UserProfile(displayName, google.id))
                                } else {
                                    message = "Google returned an unsupported sign-in credential. Please try again."
                                }
                            } catch (error: Exception) {
                                message = error.localizedMessage ?: "Sign-in was cancelled or could not be completed."
                            } finally {
                                busy = false
                            }
                        }
                    }
                },
                enabled = !busy,
                colors = ButtonDefaults.buttonColors(containerColor = Violet, contentColor = Canvas),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth().height(54.dp)
            ) {
                if (busy) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Canvas,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Continue with Google", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(14.dp))
            Text(message, color = Muted, fontSize = 12.sp)
            Spacer(Modifier.height(28.dp))
            Text(
                "SubhuPlay is an independent video-discovery and study companion. Video playback is provided by the original content platform.",
                color = Muted,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun MainApp(profile: UserProfile, onSignOut: () -> Unit) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.DISCOVER) }

    Scaffold(
        containerColor = Canvas,
        bottomBar = {
            NavigationBar(containerColor = Panel) {
                MainTab.entries.forEach { tab ->
                    val icon = when (tab) {
                        MainTab.DISCOVER -> Icons.Default.Explore
                        MainTab.NOTES -> Icons.Default.MenuBook
                        MainTab.SAVED -> Icons.Default.BookmarkBorder
                        MainTab.PROFILE -> Icons.Default.Person
                    }
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(icon, contentDescription = tab.label) },
                        label = { Text(tab.label, fontSize = 10.sp) }
                    )
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            MainTab.DISCOVER -> DiscoverScreen(Modifier.padding(padding))
            MainTab.NOTES -> NotesScreen(Modifier.padding(padding))
            MainTab.SAVED -> SavedScreen(Modifier.padding(padding))
            MainTab.PROFILE -> ProfileScreen(profile, onSignOut, Modifier.padding(padding))
        }
    }
}

@Composable
private fun DiscoverScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var query by rememberSaveable { mutableStateOf("") }
    var results by remember { mutableStateOf<List<VideoResult>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    fun search(term: String) {
        if (term.isBlank()) return
        if (BuildConfig.YOUTUBE_DATA_API_KEY.isBlank()) {
            error = "Video search needs a YouTube Data API key. Add YOUTUBE_DATA_API_KEY to your local Gradle properties."
            return
        }
        scope.launch {
            loading = true
            error = null
            try {
                results = YouTubeDataRepository.search(term.trim())
            } catch (exception: Exception) {
                error = exception.localizedMessage ?: "Search could not be completed."
            } finally {
                loading = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_subhuplay_foreground),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(42.dp)
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("SubhuPlay", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
                Text("A more thoughtful way to explore", color = Muted, fontSize = 11.sp)
            }
            Surface(color = PanelSoft, shape = CircleShape) {
                Text("SP", color = Mint, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Find your next deep dive.", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Text("Search across public videos and build your own learning trail.", color = Muted, fontSize = 13.sp)
        Spacer(Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                placeholder = { Text("Search videos, topics, creators…", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Violet,
                    unfocusedBorderColor = PanelSoft,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Mint
                )
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { search(query) },
                enabled = !loading && query.isNotBlank(),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 16.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }

        Spacer(Modifier.height(15.dp))
        Text("Explore a topic", color = Color.White, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Physics", "Coding", "Space", "History").forEach { topic ->
                FilterChip(
                    selected = false,
                    onClick = {
                        query = topic
                        search(topic)
                    },
                    label = { Text(topic) }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Design", "Mathematics", "Documentaries").forEach { topic ->
                FilterChip(
                    selected = false,
                    onClick = {
                        query = topic
                        search(topic)
                    },
                    label = { Text(topic) }
                )
            }
        }

        Spacer(Modifier.height(22.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = Panel),
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("YOUR LEARNING SPACE", color = Mint, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                Spacer(Modifier.height(7.dp))
                Text("Watch with intention.", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Keep private study notes alongside the videos that inspire you. Your notes stay on this device.",
                    color = Muted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }

        if (loading) {
            Box(Modifier.fillMaxWidth().padding(28.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Mint)
            }
        }

        error?.let {
            Text(it, color = Color(0xFFFFB4A9), fontSize = 12.sp, modifier = Modifier.padding(top = 14.dp))
        }

        if (results.isNotEmpty()) {
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Search results", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text("Source: YouTube", color = Muted, fontSize = 11.sp)
            }
            Spacer(Modifier.height(10.dp))
            results.forEach { video ->
                VideoResultCard(video = video, onClick = {
                    context.startActivity(
                        Intent(context, VideoPlayerActivity::class.java)
                            .putExtra(VideoPlayerActivity.EXTRA_VIDEO_ID, video.videoId)
                    )
                })
                Spacer(Modifier.height(14.dp))
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun VideoResultCard(video: VideoResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Column {
            AsyncImage(
                model = video.thumbnail,
                contentDescription = video.title,
                modifier = Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            )
            Column(Modifier.padding(14.dp)) {
                Text(video.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(5.dp))
                Text(video.channel, color = Mint, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(5.dp))
                Text(video.description, color = Muted, fontSize = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(8.dp))
                Text("Open official video player  →", color = Violet, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun NotesScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("subhuplay_notes", Context.MODE_PRIVATE) }
    var note by remember { mutableStateOf(prefs.getString("study_note", "") ?: "") }
    var saved by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(22.dp)
    ) {
        Text("Study notes", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Text("Capture the useful parts while you watch.", color = Muted, fontSize = 13.sp)
        Spacer(Modifier.height(20.dp))
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(22.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("MY NOTES", color = Mint, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it; saved = false },
                    modifier = Modifier.fillMaxWidth().height(230.dp),
                    placeholder = { Text("Write an idea, formula, timestamp, or question…") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Violet,
                        unfocusedBorderColor = PanelSoft,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Mint
                    )
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        prefs.edit().putString("study_note", note).apply()
                        saved = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save notes")
                }
                if (saved) {
                    Text("Saved on this device.", color = Mint, fontSize = 11.sp, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        Text("Tip: add a video title and timestamp to your notes so you can return to the exact moment later.", color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
    }
}

@Composable
private fun SavedScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(22.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = Violet, modifier = Modifier.size(48.dp))
        Spacer(Modifier.height(12.dp))
        Text("Your saved learning trail", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
        Text("Saved collections are coming next. You can already keep notes in Study notes.", color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun ProfileScreen(profile: UserProfile, onSignOut: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(22.dp)
    ) {
        Text("Your profile", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(22.dp)) {
            Column(Modifier.padding(18.dp)) {
                Text(profile.displayName, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(profile.email, color = Muted, fontSize = 12.sp)
                Spacer(Modifier.height(16.dp))
                Text("Google sign-in is used for your local app profile. Cloud sync and channel-specific permissions will be added only with explicit Google OAuth consent.", color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
                TextButton(onClick = onSignOut) {
                    Text("Sign out", color = Violet)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(22.dp)) {
            Column(Modifier.padding(18.dp)) {
                Text("Playback & downloads", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(7.dp))
                Text(
                    "Videos from YouTube use the official embedded player. Its controls, branding, and ads remain intact. Offline downloads will be limited to content you own or are licensed to download.",
                    color = Muted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

private object YouTubeDataRepository {
    suspend fun search(query: String): List<VideoResult> =
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            val uri = Uri.parse("https://www.googleapis.com/youtube/v3/search").buildUpon()
                .appendQueryParameter("part", "snippet")
                .appendQueryParameter("type", "video")
                .appendQueryParameter("maxResults", "15")
                .appendQueryParameter("safeSearch", "moderate")
                .appendQueryParameter("q", query)
                .appendQueryParameter("key", BuildConfig.YOUTUBE_DATA_API_KEY)
                .build()

            val connection = (URL(uri.toString()).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 15000
                readTimeout = 20000
            }

            try {
                val status = connection.responseCode
                val stream = if (status in 200..299) connection.inputStream else connection.errorStream
                val payload = stream.bufferedReader().use { it.readText() }
                if (status !in 200..299) {
                    val reason = runCatching {
                        JSONObject(payload).optJSONObject("error")?.optString("message")
                    }.getOrNull()
                    throw IOException(reason?.takeIf { it.isNotBlank() } ?: "YouTube search failed (HTTP " + status + ").")
                }

                val items = JSONObject(payload).optJSONArray("items") ?: return@withContext emptyList()
                buildList {
                    for (index in 0 until items.length()) {
                        val item = items.optJSONObject(index) ?: continue
                        val videoId = item.optJSONObject("id")?.optString("videoId").orEmpty()
                        val snippet = item.optJSONObject("snippet") ?: continue
                        val thumbs = snippet.optJSONObject("thumbnails")
                        val thumbnail = thumbs?.optJSONObject("high")?.optString("url")
                            ?: thumbs?.optJSONObject("medium")?.optString("url")
                            ?: thumbs?.optJSONObject("default")?.optString("url")
                            ?: ""
                        if (videoId.isNotBlank() && thumbnail.isNotBlank()) {
                            add(
                                VideoResult(
                                    videoId = videoId,
                                    title = snippet.optString("title", "Untitled video"),
                                    channel = snippet.optString("channelTitle", "Unknown channel"),
                                    description = snippet.optString("description", ""),
                                    thumbnail = thumbnail
                                )
                            )
                        }
                    }
                }
            } finally {
                connection.disconnect()
            }
        }
}
