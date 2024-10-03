package dev.herman.androiddeeplinkplayground.presentation.deeplink

import android.net.Uri
import androidx.lifecycle.ViewModel
import dev.herman.androiddeeplinkplayground.domain.models.generalParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class DeeplinkSource {
    onCreate, onNewIntent;
}

sealed interface DeeplinkState {
    data class DebugDeeplinkState(
        val deeplink: String,
        val source: DeeplinkSource,
        val host: String,
        val pathSegments: String,
        val data: Map<String, String>
    ) : DeeplinkState

    data object NoDeeplink : DeeplinkState
}

class DeeplinkViewModel : ViewModel() {
    private val _state = MutableStateFlow<DeeplinkState>(DeeplinkState.NoDeeplink)
    val state = _state.asStateFlow()

    fun parseDeeplink(uri: Uri?, source: DeeplinkSource) {
        _state.value = if (uri == null) {
            DeeplinkState.NoDeeplink
        } else {
            DeeplinkState.DebugDeeplinkState(
                deeplink = uri.toString(),
                source = source,
                host = uri.host.toString(),
                pathSegments = uri.pathSegments.joinToString(),
                data = generalParser(uri).mapValues { it.value.toString() }
            )
        }
    }
}
