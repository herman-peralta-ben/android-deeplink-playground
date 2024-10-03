package dev.herman.androiddeeplinkplayground.presentation.deeplink

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.herman.androiddeeplinkplayground.domain.models.generalParser
import dev.herman.androiddeeplinkplayground.domain.models.parseUserProfile
import dev.herman.androiddeeplinkplayground.presentation.common.Header
import dev.herman.androiddeeplinkplayground.ui.theme.AndroidDeeplinkPlaygroundTheme

private val monoStyle = TextStyle(fontFamily = FontFamily.Monospace)

class DeeplinkActivity : ComponentActivity() {
    private val viewModel: DeeplinkViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseDeeplink("onCreate")

        enableEdgeToEdge()
        setContent {
            AndroidDeeplinkPlaygroundTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Incoming Deeplink")
                            }
                        )
                    },
                ) { innerPadding ->
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        DeeplinkScreenContent(state)
                    }
                }
            }
        }
    }

    /** Called if the activity was visible, onCreate is not called on such case. Requires [android:launchMode="singleTask"] on the manifest.*/
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        parseDeeplink("onNewIntent")
    }

    private fun parseDeeplink(source: String) {
        val action = intent.action
        val uri = intent.data

        viewModel.parseDeeplink(uri, DeeplinkSource.valueOf(source))

        Log.d("DeeplinkPlayground", "$source: action: $action")
        if (uri == null) {
            Log.d("DeeplinkPlayground", "$source: got null url")
        } else {
            val debugData = generalParser(uri)
            val profile = parseUserProfile(uri)
            Log.d("DeeplinkPlayground", "$source: userProfile: $profile")
        }
    }
}

@Composable
private fun DeeplinkScreenContent(state: DeeplinkState) {
    Box(modifier = Modifier.padding(16.dp)) {
        when (state) {
            is DeeplinkState.NoDeeplink -> {
                Text(text = "No Deeplink")
            }

            is DeeplinkState.DebugDeeplinkState -> {
                DeeplinkDataComposable(state)
            }
        }
    }
}

@Composable
private fun DeeplinkDataComposable(state: DeeplinkState.DebugDeeplinkState) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Header(header = "Deeplink")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = state.deeplink, style = monoStyle)
            CommonRow {
                Header(header = "Source", color = Color.Black, fontSize = 10.sp)
                Header(
                    header = state.source.name,
                    color = if (state.source == DeeplinkSource.onNewIntent) Color.Red else
                        Color.Magenta,
                    fontSize = 10.sp
                )
            }
            CommonRow {
                Header(header = "Host", color = Color.Black, fontSize = 10.sp)
                Text(text = state.host, style = monoStyle)
            }
            CommonRow {
                Header(header = "PathSegments", color = Color.Black, fontSize = 10.sp)
                Text(text = state.pathSegments, style = monoStyle)
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Header(header = "Data")
            Spacer(modifier = Modifier.height(8.dp))
            CommonRow {
                Header(header = "Key", color = Color.Black, fontSize = 10.sp)
                Spacer(Modifier.weight(1f))
                Header(header = "Value", color = Color.Black, fontSize = 10.sp)
            }
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                items(items = state.data.entries.toList()) { (key, value) ->
                    CommonRow {
                        Text(text = key, style = monoStyle)
                        Spacer(Modifier.weight(1f))
                        Text(text = value, style = monoStyle)
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun CommonRow(content: @Composable RowScope.() -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        content = content
    )
}
