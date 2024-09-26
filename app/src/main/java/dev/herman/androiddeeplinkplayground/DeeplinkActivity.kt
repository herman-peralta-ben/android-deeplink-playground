package dev.herman.androiddeeplinkplayground

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import dev.herman.androiddeeplinkplayground.domain.models.generalParser
import dev.herman.androiddeeplinkplayground.domain.models.parseUserProfile
import dev.herman.androiddeeplinkplayground.ui.theme.AndroidDeeplinkPlaygroundTheme

class DeeplinkActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseDeeplink("onCreate")

        enableEdgeToEdge()
        setContent {
            AndroidDeeplinkPlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(text = "Deeplink", modifier = Modifier.padding(innerPadding))
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
