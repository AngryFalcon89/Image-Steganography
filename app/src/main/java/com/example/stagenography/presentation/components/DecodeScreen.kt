package com.example.stagenography.presentation.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.InputStream

@Composable
fun DecodeScreen(
    onImageSelected: (InputStream) -> Unit,
    onDecode: () -> Unit,
    imageBitmap: Bitmap?,
    decodedMessage: String
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val inputStream: InputStream? = context.contentResolver.openInputStream(it)
            inputStream?.let { stream ->
                onImageSelected(stream)
            }
        }
    }

    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { launcher.launch("image/*") }) {
                Text("Select Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onDecode) {
                Text("Decode")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = decodedMessage)
        }
    }
}

@Composable
fun DecodeScreen() {
    val viewModel: DecodeScreenViewModel = viewModel()

    DecodeScreen(
        onImageSelected = viewModel::onImageSelected,
        onDecode = viewModel::onDecodeClick,
        imageBitmap = viewModel.imageBitmap.value,
        decodedMessage = viewModel.decodedMessage.value
    )
}

