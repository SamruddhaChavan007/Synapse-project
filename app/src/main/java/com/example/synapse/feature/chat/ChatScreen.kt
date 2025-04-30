@file:Suppress("DEPRECATION")

package com.example.synapse.feature.chat

import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.synapse.model.Message
import com.example.synapse.ui.theme.Aqua_Island
import com.example.synapse.ui.theme.Black
import com.example.synapse.ui.theme.Green_BG
import com.example.synapse.ui.theme.PureWhite
import com.example.synapse.ui.theme.Roboto
import com.example.synapse.ui.theme.Vista_Blue
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(navController: NavController, channelId: String) {
    Scaffold {
        val viewModel: ChatViewModel = hiltViewModel()
        val chooserDialog = remember {
            mutableStateOf(false)
        }
        val cameraImageUri = remember {
            mutableStateOf<Uri?>(null)
        }
        val cameraImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                cameraImageUri.value?.let {
                    // send Image to Server.
                    viewModel.sendImageMessage(it, channelId)
                }
            }
        }

        fun createImageUri(): Uri {
            val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir =
                ContextCompat.getExternalFilesDirs(
                    navController.context,
                    Environment.DIRECTORY_PICTURES
                ).first()
            return FileProvider.getUriForFile(
                navController.context,
                "${navController.context.packageName}.provider",
                File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
                    cameraImageUri.value =
                        Uri.fromFile(this)
                }
            )
        }

        val permissionLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    cameraImageLauncher.launch(createImageUri())
                }
            }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Green_BG)
        ) {
            LaunchedEffect(key1 = true) {
                viewModel.listenForMessages(channelId)
            }
            val messages = viewModel.message.collectAsState()
            ChatMessages(
                messages = messages.value, onSendMessage = { message ->
                    viewModel.sendMessage(channelId, message)
                }, onImageClicked = {
                    chooserDialog.value = true
                })
        }
        if (chooserDialog.value) {
            ContentSelectionDialog(onCameraSelected = {
                chooserDialog.value = false
                if (navController.context.checkSelfPermission(android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    cameraImageLauncher.launch(createImageUri())
                } else {
                    //request permission
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }, onGallerySelected = {
                chooserDialog.value = false
            })
        }
    }
}

@Composable
fun ContentSelectionDialog(onCameraSelected: () -> Unit, onGallerySelected: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = {/*ToDo*/ },
        confirmButton = {
            TextButton(onClick = onCameraSelected) {
                Text(
                    text = "Camera",
                    color = Black
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onGallerySelected) {
                Text(
                    text = "Gallery",
                    color = Black
                )
            }
        },
        title = { Text(text = "Select your source") },
        text = { Text(text = "Would you like to pick an image from gallery or use the camera") }
    )
}

@Composable
fun ChatMessages(
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
    onImageClicked: () -> Unit,

    ) {
    val hideKeyboardController = LocalSoftwareKeyboardController.current
    val msg = remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(messages) { message ->
                ChatBubble(message = message)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(color = Color.LightGray)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Black
                ), onClick = {
                    msg.value = ""
                    onImageClicked()
                }) {
                Icon(imageVector = Icons.Filled.AttachFile, contentDescription = "attach_file")
            }
            TextField(
                value = msg.value,
                onValueChange = { msg.value = it },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                placeholder = { Text(text = "Type a message") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        hideKeyboardController?.hide()
                    })
            )
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Black
                ), onClick = {
                    /*ToDo*/
                    onSendMessage(msg.value)
                    msg.value = ""
                },
                enabled = msg.value.trim().isNotEmpty()
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "send")
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) {
        Vista_Blue
    } else {
        Aqua_Island
    }
    val bubbleCharacter = if (isCurrentUser) {
        Vista_Blue
    } else {
        PureWhite
    }
    val alignment = if (isCurrentUser) {
        Alignment.CenterEnd
    } else {
        Alignment.CenterStart
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .align(alignment),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isCurrentUser) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(bubbleCharacter)
                ) {
                    Text(
                        text = message.senderName[0].toString().uppercase(),
                        color = Black,
                        style = TextStyle(fontSize = 20.sp, fontFamily = Roboto),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(bubbleColor)
                        .padding(8.dp)
                ) {
                    if (message.imageUrl != null) {
                        AsyncImage(
                            model = message.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                    }
                    Text(
                        text = message.message?.trim() ?: "",
                        color = Black,
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(bubbleColor)
                        .padding(8.dp)
                ) {
                    if (message.imageUrl != null) {
                        AsyncImage(
                            model = message.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                    }
                    Text(
                        text = message.message?.trim() ?: "",
                        color = Black,
                    )
                }
            }
        }
    }
}