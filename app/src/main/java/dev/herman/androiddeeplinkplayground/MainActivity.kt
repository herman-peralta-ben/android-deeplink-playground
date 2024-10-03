package dev.herman.androiddeeplinkplayground

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import dev.herman.androiddeeplinkplayground.ui.theme.AndroidDeeplinkPlaygroundTheme

private val examples =
    mapOf(
        "Launch testing:// intent" to "testing://www.testing.com/abc?name=herman&hobbies=coding,drums,games&age=36",
        "Launch http:// intent" to "http://www.example.com/testing",
        "Launch https:// intent" to "https://www.example.com/testing",
    )

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AndroidDeeplinkPlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                    ) {
                        items(examples.entries.toList()) { (label, deeplink) ->
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    launchDeeplink(deeplink)
                                }) {
                                Text(text = label)
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {

                            }) {
                            Text(text = "Launch testing:// intent")
                        }
                    }
                }
            }
        }
    }

    private fun launchDeeplink(deeplink: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(deeplink)
        )
        startActivity(intent)
    }
}
