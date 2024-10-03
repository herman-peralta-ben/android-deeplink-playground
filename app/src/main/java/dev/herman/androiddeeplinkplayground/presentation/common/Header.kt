package dev.herman.androiddeeplinkplayground.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(header: String, color: Color = Color.Blue, fontSize: TextUnit = 16.sp) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color)
    ) {
        Text(
            text = header,
            style = TextStyle(color = Color.White, fontSize, fontStyle = FontStyle.Italic),
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
