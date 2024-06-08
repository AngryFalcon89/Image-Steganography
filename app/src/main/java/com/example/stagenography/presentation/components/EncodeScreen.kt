package com.example.stagenography.presentation.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

@Composable
fun EncodeScreen(
    onImageSelected: (InputStream) -> Unit,
    onEncode: () -> Unit,
    onSaveImage: (Uri) -> Unit,
    encodedBitmap: Bitmap?,
    onMessageChanged: (String) -> Unit,
    message: String
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
    val saveLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument("image/png")) { uri: Uri? ->
        uri?.let {
            onSaveImage(it)
        }
    }

    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            encodedBitmap?.let {
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

            BasicTextField(
                value = message,
                onValueChange = onMessageChanged,
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        if (message.isEmpty()) {
                            Text("Enter Message")
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onEncode) {
                Text("Encode")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { saveLauncher.launch("encoded_image.png") }) {
                Text("Save Encoded Image")
            }
        }
    }
}

@Composable
fun EncodeScreen() {
    val viewModel: EncodeScreenViewModel = viewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    EncodeScreen(
        onImageSelected = viewModel::onImageSelected,
        onEncode = viewModel::onEncodeClick,
        onSaveImage = { uri ->
            val file = File(context.cacheDir, "encoded_image.png")
            viewModel.saveEncodedImage(file)
            coroutineScope.launch {
                val outputStream = context.contentResolver.openOutputStream(uri)
                outputStream?.use {
                    file.inputStream().copyTo(it)
                }
            }
        },
        encodedBitmap = viewModel.encodedBitmap.value,
        onMessageChanged = viewModel::onMessageChanged,
        message = viewModel.message.value
    )
}
