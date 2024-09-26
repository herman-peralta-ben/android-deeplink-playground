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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.herman.androiddeeplinkplayground.domain.models.generalParser
import dev.herman.androiddeeplinkplayground.domain.models.parseUserProfile
import dev.herman.androiddeeplinkplayground.ui.theme.AndroidDeeplinkPlaygroundTheme

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

    /** Requires */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        parseDeeplink("onNewIntent")
    }

    private fun parseDeeplink(source: String) {

        val action = intent.action
        val uri = intent.data

        viewModel.parseDeeplink(uri)

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
    Card {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(text = state.deeplink)
            HorizontalDivider(color = Color.Blue)
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                items(items = state.data.entries.toList()) { (key, value) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = key)
                        Spacer(Modifier.weight(1f))
                        Text(text = value)
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
