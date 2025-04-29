package com.example.synapse.feature.chat

import androidx.compose.foundation.Image
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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.synapse.model.Message
import com.example.synapse.ui.theme.Aqua_Island
import com.example.synapse.ui.theme.Black
import com.example.synapse.ui.theme.Green_BG
import com.example.synapse.ui.theme.Hit_Gray
import com.example.synapse.ui.theme.Pacifico
import com.example.synapse.ui.theme.PureWhite
import com.example.synapse.ui.theme.Roboto
import com.example.synapse.ui.theme.Vista_Blue
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ChatScreen(navController: NavController, channelId: String) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Green_BG)
        ) {
            val viewModel: ChatViewModel = hiltViewModel()
            LaunchedEffect(key1 = true) {
                viewModel.listenForMessages(channelId)
            }
            val messages = viewModel.message.collectAsState()
            ChatMessages(
                messages = messages.value, onSendMessage = { message ->
                    viewModel.sendMessage(channelId, message)
                })
        }
    }
}

@Composable
fun ChatMessages(
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
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
                .background(color = Hit_Gray)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Black
                ), onClick = {
                    /*ToDo*/
                }) {
                Icon(imageVector = Icons.Filled.AttachFile, contentDescription = "attach_file")
            }
            TextField(
                value = msg.value,
                onValueChange = { msg.value = it },
                modifier = Modifier.weight(1f)
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
                }) {
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
                Text(
                    text = message.message.trim(),
                    color = Black,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(bubbleColor)
                        .padding(8.dp)
                )
            } else {
                Text(
                    text = message.message.trim(),
                    color = Black,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(bubbleColor)
                        .padding(8.dp)
                )
            }
        }
    }
}