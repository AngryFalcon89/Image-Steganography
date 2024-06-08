package com.example.stagenography.presentation.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(onEncodeClick: () -> Unit, onDecodeClick: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { onEncodeClick() }) {
                Text("Encode")
            }
            Button(onClick = { onDecodeClick() }) {
                Text("Decode")
            }
        }
    }
}